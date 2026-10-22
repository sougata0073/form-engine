const FORM_IDS = ["e5735205-3a1d-4cd4-b695-4f543e15df27"];

const REGISTER_URL = "http://localhost:8083/api/v1/auth/register/email-pass";
const VIEW_URL = "http://localhost:8083/api/v1/forms";
const RESPONSE_URL = "http://localhost:8083/api/v1/form-data";

const NUMBER_OF_REQUESTS = 1000;
const CONCURRENCY = 1500;

const RESPOND_TO_ALL_QUESTIONS = true;
const OPTIONAL_RESPONSE_PROBABILITY = 0.5;

const HEADERS = {
  "Content-Type": "application/json",
};

// ============================================================
// RANDOM HELPERS
// ============================================================

function randomItem(arr) {
  if (!arr || arr.length === 0) {
    return null;
  }

  return arr[Math.floor(Math.random() * arr.length)];
}

function randomSubset(arr) {
  if (!arr || arr.length === 0) {
    return [];
  }

  const shuffled = [...arr].sort(() => Math.random() - 0.5);
  const count = Math.floor(Math.random() * arr.length) + 1;

  return shuffled.slice(0, count);
}

function randomDate() {
  const start = new Date("2000-01-01");
  const end = new Date("2030-12-31");

  return new Date(
    start.getTime() + Math.random() * (end.getTime() - start.getTime()),
  );
}

function isoDate(date) {
  return new Date(
    Date.UTC(
      date.getUTCFullYear(),
      date.getUTCMonth(),
      date.getUTCDate(),
      18,
      30,
      0,
    ),
  ).toISOString();
}

function isoTime() {
  const d = new Date();

  d.setUTCHours(
    Math.floor(Math.random() * 24),
    Math.floor(Math.random() * 60),
    0,
    0,
  );

  return d.toISOString();
}

function uuid() {
  return crypto.randomUUID();
}

// ============================================================
// REGISTER A UNIQUE USER AND GET JWT
// ============================================================

async function registerLoadTestUser() {
  const uniqueId = `${Date.now()}-${uuid()}`;

  const payload = {
    username: `loadtest-${uniqueId}`,
    email: `loadtest-${uniqueId}@example.invalid`,
    password: "12345",
    avatarUrl: null,
  };

  const response = await fetch(REGISTER_URL, {
    method: "POST",
    headers: HEADERS,
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const message = (await response.text())
      .replace(/\s+/g, " ")
      .trim()
      .substring(0, 120);

    throw new Error(
      `Registration failed. Status: ${response.status}. Response: ${message}`,
    );
  }

  const result = await response.json();

  if (!result.jwtToken) {
    throw new Error("Registration response did not contain jwtToken.");
  }

  return result.jwtToken;
}

// ============================================================
// FETCH FORM SCHEMA
// ============================================================

async function fetchFormSchema(formId, jwtToken) {
  const url = `${VIEW_URL}/${formId}/view`;

  const response = await fetch(url, {
    method: "GET",
    headers: {
      ...HEADERS,
      Authorization: `Bearer ${jwtToken}`,
    },
  });

  if (!response.ok) {
    const message = await response.text();

    throw new Error(
      `Failed to fetch form ${formId}. ` +
        `Status: ${response.status}. ` +
        `Response: ${message}`,
    );
  }

  const form = await response.json();

  if (!form.questions || !Array.isArray(form.questions)) {
    throw new Error(
      `Invalid form response for ${formId}: questions array not found`,
    );
  }

  return form;
}

// ============================================================
// SHOULD RESPOND TO QUESTION?
// ============================================================

function shouldRespondToQuestion(question) {
  if (RESPOND_TO_ALL_QUESTIONS === true) {
    return true;
  }

  if (question.required === true) {
    return true;
  }

  return Math.random() < OPTIONAL_RESPONSE_PROBABILITY;
}

// ============================================================
// PAYLOAD GENERATOR
// ============================================================

function generatePayload(form, index) {
  const date = randomDate();
  const responses = [];

  for (const question of form.questions) {
    if (!shouldRespondToQuestion(question)) {
      continue;
    }

    const questionId = question.id;
    const questionType = question.questionType;

    let response;

    if (questionType === "SHORT_ANSWER") {
      response = {
        text: `Short answer ${index}`,
        questionId,
        questionType,
      };
    } else if (questionType === "PARAGRAPH") {
      response = {
        text: `Paragraph response ${index}.`,
        questionId,
        questionType,
      };
    } else if (questionType === "MULTIPLE_CHOICE") {
      response = {
        responseOptionId: randomItem(
          (question.options || []).map((option) => option.id),
        ),
        questionId,
        questionType,
      };
    } else if (questionType === "CHECKBOX") {
      response = {
        responseOptionIds: randomSubset(
          (question.options || []).map((option) => option.id),
        ),
        questionId,
        questionType,
      };
    } else if (questionType === "DROPDOWN") {
      response = {
        responseOptionId: randomItem(
          (question.options || []).map((option) => option.id),
        ),
        questionId,
        questionType,
      };
    } else if (questionType === "FILE_UPLOAD") {
      response = {
        fileName: `image_${index}.jpg`,
        fileUrl: `https://picsum.photos/seed/${index}/1200/800`,
        fileSize:
          Math.floor(Math.random() * (10 * 1024 * 1024 - 100 * 1024 + 1)) +
          100 * 1024,
        fileMimeType: "image/jpeg",
        questionId,
        questionType,
      };
    } else if (questionType === "LINEAR_SCALE") {
      const from = question.fromNumber ?? 1;
      const to = question.toNumber ?? 5;

      response = {
        scale: Math.floor(Math.random() * (to - from + 1)) + from,
        questionId,
        questionType,
      };
    } else if (questionType === "RATING") {
      const maxRating = question.maxRatingNumber ?? 10;

      response = {
        rating: Math.floor(Math.random() * maxRating) + 1,
        questionId,
        questionType,
      };
    } else if (questionType === "MULTIPLE_CHOICE_GRID") {
      const columns = question.columns || [];
      const rows = question.rows || [];

      response = {
        rows: rows.map((row) => ({
          rowId: row.id,
          responseColumnId: randomItem(columns.map((column) => column.id)),
        })),
        questionId,
        questionType,
      };
    } else if (questionType === "TICK_BOX_GRID") {
      const columns = question.columns || [];
      const rows = question.rows || [];

      response = {
        rows: rows.map((row) => ({
          rowId: row.id,
          responseColumnIds: randomSubset(columns.map((column) => column.id)),
        })),
        questionId,
        questionType,
      };
    } else if (questionType === "DATE") {
      response = {
        date: isoDate(date),
        questionId,
        questionType,
      };
    } else if (questionType === "TIME") {
      response = {
        time: isoTime(),
        questionId,
        questionType,
      };
    } else if (questionType === "DATE_TIME") {
      response = {
        dateTime: date.toISOString(),
        questionId,
        questionType,
      };
    } else if (questionType === "DURATION") {
      response = {
        hours: Math.floor(Math.random() * 73),
        minutes: Math.floor(Math.random() * 60),
        seconds: Math.floor(Math.random() * 60),
        questionId,
        questionType,
      };
    } else {
      console.warn(
        `Unknown question type "${questionType}" ` +
          `for question ${questionId}. Skipping.`,
      );

      continue;
    }

    responses.push(response);
  }

  return {
    formDetailsResponseSubmittedOn: form,
    responses,
  };
}

// ============================================================
// SUBMIT ONE RESPONSE
//
// 1. Register a unique user.
// 2. Extract JWT.
// 3. GET form schema using JWT.
// 4. Generate response payload.
// 5. POST response using the same JWT.
// ============================================================

async function submitOneResponse(formId, requestIndex) {
  try {
    // 1. Register user and obtain JWT.

    const jwtToken = await registerLoadTestUser();

    // 2. Fetch form schema using the registered user's JWT.

    const form = await fetchFormSchema(formId, jwtToken);

    // 3. Generate response payload.

    const payload = generatePayload(form, requestIndex);

    // 4. Submit form response using the same JWT.

    const postUrl = `${RESPONSE_URL}/${formId}/responses`;

    const response = await fetch(postUrl, {
      method: "POST",
      headers: {
        ...HEADERS,
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      let message;

      try {
        message = await response.text();
      } catch {
        message = "Unknown error";
      }

      message = message
        .replace(/\n/g, " ")
        .replace(/\s+/g, " ")
        .trim()
        .substring(0, 120);

      return {
        success: false,
        request: requestIndex,
        formId,
        status: response.status,
        reason: message || response.statusText,
      };
    }

    return {
      success: true,
      request: requestIndex,
      formId,
    };
  } catch (error) {
    return {
      success: false,
      request: requestIndex,
      formId,
      status: "NETWORK",
      reason: error.message,
    };
  }
}

// ============================================================
// RANDOM REQUEST DISTRIBUTION
// ============================================================

function distributeRequests(formIds, totalRequests) {
  const numberOfForms = formIds.length;

  if (numberOfForms === 0) {
    return [];
  }

  if (totalRequests < numberOfForms) {
    const shuffled = [...formIds].sort(() => Math.random() - 0.5);

    return formIds.map((formId) => ({
      formId,
      count: shuffled.slice(0, totalRequests).includes(formId) ? 1 : 0,
    }));
  }

  const counts = formIds.map((formId) => ({
    formId,
    count: 1,
  }));

  let remaining = totalRequests - numberOfForms;

  while (remaining > 0) {
    const randomIndex = Math.floor(Math.random() * numberOfForms);

    counts[randomIndex].count++;
    remaining--;
  }

  return counts;
}

// ============================================================
// MAIN LOAD TEST
// ============================================================

async function main() {
  console.log("==============================================");
  console.log("DYNAMIC FORM LOAD TEST");
  console.log("==============================================");

  const numberOfForms = FORM_IDS.length;

  if (numberOfForms === 0) {
    console.error("No form IDs configured.");
    return;
  }

  if (NUMBER_OF_REQUESTS <= 0) {
    console.error("NUMBER_OF_REQUESTS must be greater than 0.");
    return;
  }

  if (OPTIONAL_RESPONSE_PROBABILITY < 0 || OPTIONAL_RESPONSE_PROBABILITY > 1) {
    console.error("OPTIONAL_RESPONSE_PROBABILITY must be between 0 and 1.");
    return;
  }

  console.log(`Forms                  : ${numberOfForms}`);
  console.log(`Total Requests         : ${NUMBER_OF_REQUESTS}`);
  console.log(`Concurrency/Form       : ${CONCURRENCY}`);
  console.log(`Respond To All         : ${RESPOND_TO_ALL_QUESTIONS}`);

  if (RESPOND_TO_ALL_QUESTIONS === false) {
    console.log(
      `Optional Response Rate : ${OPTIONAL_RESPONSE_PROBABILITY * 100}%`,
    );
  }

  const formRequestCounts = distributeRequests(FORM_IDS, NUMBER_OF_REQUESTS);

  console.log("\nREQUEST DISTRIBUTION");
  console.log("----------------------------------------------");

  for (const item of formRequestCounts) {
    console.log(`${item.formId} : ${item.count} requests`);
  }

  console.log("----------------------------------------------");

  const distributedTotal = formRequestCounts.reduce(
    (total, item) => total + item.count,
    0,
  );

  console.log(`Distributed Total    : ${distributedTotal}`);

  let totalCompleted = 0;
  let totalSuccessful = 0;

  const failedRequests = [];

  console.time("TOTAL LOAD TEST TIME");

  const currentIndexes = FORM_IDS.map(() => 1);

  let batchNumber = 1;

  while (
    formRequestCounts.some(
      (item) => currentIndexes[FORM_IDS.indexOf(item.formId)] <= item.count,
    )
  ) {
    console.log(`\nBATCH ${batchNumber}`);

    const batchPromises = [];

    for (let formIndex = 0; formIndex < numberOfForms; formIndex++) {
      const formId = FORM_IDS[formIndex];
      const totalForThisForm = formRequestCounts[formIndex].count;
      const startIndex = currentIndexes[formIndex];

      if (startIndex > totalForThisForm) {
        continue;
      }

      const endIndex = Math.min(startIndex + CONCURRENCY - 1, totalForThisForm);

      currentIndexes[formIndex] = endIndex + 1;

      for (
        let requestIndex = startIndex;
        requestIndex <= endIndex;
        requestIndex++
      ) {
        const promise = submitOneResponse(formId, requestIndex).then(
          (result) => {
            totalCompleted++;

            if (result.success) {
              totalSuccessful++;
              process.stdout?.write?.("✅ ");
            } else {
              failedRequests.push(result);
              process.stdout?.write?.("❌ ");
            }

            process.stdout?.write?.(`${totalCompleted}/${NUMBER_OF_REQUESTS} `);

            return result;
          },
        );

        batchPromises.push(promise);
      }
    }

    await Promise.all(batchPromises);

    process.stdout?.write?.("\n");

    batchNumber++;
  }

  console.timeEnd("TOTAL LOAD TEST TIME");

  console.log("\n");
  console.log("══════════════════════════════════════════════");
  console.log("LOAD TEST SUMMARY");
  console.log("══════════════════════════════════════════════");

  console.log(`Total Requests : ${NUMBER_OF_REQUESTS}`);
  console.log(`Successful     : ${totalSuccessful}`);
  console.log(`Failed         : ${failedRequests.length}`);

  console.log(
    `Success Rate   : ${((totalSuccessful / NUMBER_OF_REQUESTS) * 100).toFixed(
      2,
    )}%`,
  );

  console.log("══════════════════════════════════════════════");

  if (failedRequests.length === 0) {
    console.log("\nAll requests completed successfully.");
    return;
  }

  let grouped = {};

  for (const failure of failedRequests) {
    const key = `${failure.status} | ${failure.reason}`;
    grouped[key] = (grouped[key] || 0) + 1;
  }

  console.log("\nFAILED REQUESTS");
  console.log("══════════════════════════════════════════════");

  Object.entries(grouped)
    .sort((a, b) => b[1] - a[1])
    .forEach(([message, count]) => {
      console.log(`${count}x  ${message}`);
    });

  console.log("══════════════════════════════════════════════");

  grouped = {};

  for (const failure of failedRequests) {
    const key = `${failure.status}`;
    grouped[key] = (grouped[key] || 0) + 1;
  }

  console.log("\n📊 FAILURE BREAKDOWN");

  Object.entries(grouped)
    .sort((a, b) => b[1] - a[1])
    .forEach(([status, count]) => {
      console.log(`   ${status} : ${count}`);
    });

  console.log();
}

main();
