const POST_URL = "http://localhost:9093/api/v1/forms/b91cb39f-a944-4c89-b47d-43da924cef6f/response";

const NUMBER_OF_REQUESTS = 1000;
const CONCURRENCY = 2000;

const HEADERS = {
    "Content-Type": "application/json"
};

const QUESTION_IDS = {
    SHORT_ANSWER: "876449623228526776",
    PARAGRAPH: "876449675615383864",
    MULTIPLE_CHOICE: "876449719223564886",
    CHECKBOX: "876449793219475238",
    DROPDOWN: "876449844423540009",
    FILE_UPLOAD: "876449907124188968",
    LINEAR_SCALE: "876456807867660383",
    RATING: "876456836850301740",
    MULTIPLE_CHOICE_GRID: "876456933034079109",
    TICK_BOX_GRID: "876457279894633527",
    DATE: "876457377189902173",
    TIME: "876457407556664893",
    DATE_TIME: "876457438871335300",
    DURATION: "876457471855341746"
};

const MULTIPLE_CHOICE_OPTIONS = [
    "876449734226591324",
    "876449794976887931",
    "876449794976887932",
    "876449794976887933",
    "876449794976887934"
];

const CHECKBOX_OPTIONS = [
    "876449807488499349",
    "876449838052391958",
    "876449838052391959",
    "876449838052391960"
];

const DROPDOWN_OPTIONS = [
    "876449862542932330",
    "876449920294301745",
    "876449920294301746",
    "876449920294301747",
    "876449920294301748"
];

const MCG_ROWS = [
    "876456949660300925",
    "876457235112049447",
    "876457235112049448",
    "876457235112049449",
    "876457235112049450"
];

const MCG_COLUMNS = [
    "876456949660300926",
    "876457235372096532",
    "876457235372096533",
    "876457235372096534",
    "876457235372096535",
    "876457235372096536",
    "876457235372096537",
    "876457235372096538",
    "876457235372096539",
    "876457235372096540"
];

const TBG_ROWS = [
    "876457310940870756",
    "876457379308026341",
    "876457379308026342",
    "876457379308026343",
    "876457379308026344"
];

const TBG_COLUMNS = [
    "876457310940870757",
    "876457379568072886",
    "876457379568072887",
    "876457379568072888",
    "876457379568072889",
    "876457379568072890",
    "876457379568072891",
    "876457379568072892",
    "876457379568072893",
    "876457379568072894"
];

function randomItem(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

function randomSubset(arr) {
    const shuffled = [...arr].sort(() => Math.random() - 0.5);
    const count = Math.floor(Math.random() * arr.length) + 1;
    return shuffled.slice(0, count);
}

function randomDate() {
    const start = new Date("2000-01-01");
    const end = new Date("2030-12-31");

    return new Date(
        start.getTime() +
        Math.random() * (end.getTime() - start.getTime())
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
            0
        )
    ).toISOString();
}

function isoTime() {
    const d = new Date();

    d.setUTCHours(
        Math.floor(Math.random() * 24),
        Math.floor(Math.random() * 60),
        0,
        0
    );

    return d.toISOString();
}

function uuid() {
    return crypto.randomUUID();
}

function generatePayload(index) {

    const date = randomDate();

    return {
        responses: [

            {
                text: `Short answer ${index}`,
                questionId: QUESTION_IDS.SHORT_ANSWER,
                questionType: "SHORT_ANSWER"
            },

            {
                text: `Paragraph response ${index}.`,
                questionId: QUESTION_IDS.PARAGRAPH,
                questionType: "PARAGRAPH"
            },

            {
                responseOptionId: randomItem(MULTIPLE_CHOICE_OPTIONS),
                questionId: QUESTION_IDS.MULTIPLE_CHOICE,
                questionType: "MULTIPLE_CHOICE"
            },

            {
                responseOptionIds: randomSubset(CHECKBOX_OPTIONS),
                questionId: QUESTION_IDS.CHECKBOX,
                questionType: "CHECKBOX"
            },

            {
                responseOptionId: randomItem(DROPDOWN_OPTIONS),
                questionId: QUESTION_IDS.DROPDOWN,
                questionType: "DROPDOWN"
            },

            {
                fileName: `image_${index}.jpg`,
                fileUrl: `https://picsum.photos/seed/${index}/1200/800`,
                fileSize: Math.floor(Math.random() * (10 * 1024 * 1024 - 100 * 1024 + 1)) + 100 * 1024,
                fileMimeType: "image/jpeg",
                questionId: QUESTION_IDS.FILE_UPLOAD,
                questionType: "FILE_UPLOAD"
            },

            {
                scale: Math.floor(Math.random() * 5) + 1,
                questionId: QUESTION_IDS.LINEAR_SCALE,
                questionType: "LINEAR_SCALE"
            },

            {
                rating: Math.floor(Math.random() * 10) + 1,
                questionId: QUESTION_IDS.RATING,
                questionType: "RATING"
            },

            {
                rows: MCG_ROWS.map(rowId => ({
                    rowId,
                    responseColumnId: randomItem(MCG_COLUMNS)
                })),
                questionId: QUESTION_IDS.MULTIPLE_CHOICE_GRID,
                questionType: "MULTIPLE_CHOICE_GRID"
            },

            {
                rows: TBG_ROWS.map(rowId => ({
                    rowId,
                    responseColumnIds: randomSubset(TBG_COLUMNS)
                })),
                questionId: QUESTION_IDS.TICK_BOX_GRID,
                questionType: "TICK_BOX_GRID"
            },

            {
                date: isoDate(date),
                questionId: QUESTION_IDS.DATE,
                questionType: "DATE"
            },

            {
                time: isoTime(),
                questionId: QUESTION_IDS.TIME,
                questionType: "TIME"
            },

            {
                dateTime: date.toISOString(),
                questionId: QUESTION_IDS.DATE_TIME,
                questionType: "DATE_TIME"
            },

            {
                hours: Math.floor(Math.random() * 73),
                minutes: Math.floor(Math.random() * 60),
                seconds: Math.floor(Math.random() * 60),
                questionId: QUESTION_IDS.DURATION,
                questionType: "DURATION"
            }

        ]
    };
}

async function sendRequests() {

    console.time("⏱ Total Time");

    const failedRequests = [];
    let successCount = 0;

    for (let start = 1; start <= NUMBER_OF_REQUESTS; start += CONCURRENCY) {

        const promises = [];

        for (
            let i = start;
            i < Math.min(start + CONCURRENCY, NUMBER_OF_REQUESTS + 1);
            i++
        ) {

            const payload = generatePayload(i);

            promises.push(
                fetch(POST_URL, {
                    method: "POST",
                    headers: {
                        ...HEADERS,
                        "auth-jwt": uuid()
                    },
                    body: JSON.stringify(payload)
                })
                    .then(async response => {

                        if (response.ok) {
                            successCount++;
                            process.stdout?.write?.("✅");
                            return;
                        }

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

                        failedRequests.push({
                            request: i,
                            status: response.status,
                            reason: message || response.statusText
                        });

                        process.stdout?.write?.("❌");
                    })
                    .catch(error => {

                        failedRequests.push({
                            request: i,
                            status: "NETWORK",
                            reason: error.message
                        });

                        process.stdout?.write?.("❌");
                    })
            );
        }

        await Promise.all(promises);

        process.stdout?.write?.(
            `  [${Math.min(start + CONCURRENCY - 1, NUMBER_OF_REQUESTS)}/${NUMBER_OF_REQUESTS}]\n`
        );
    }

    console.timeEnd("⏱ Total Time");

    console.log("\n");
    console.log("══════════════════════════════════════════════");
    console.log("LOAD TEST SUMMARY");
    console.log("══════════════════════════════════════════════");
    console.log(`Total Requests : ${NUMBER_OF_REQUESTS}`);
    console.log(`Successful     : ${successCount}`);
    console.log(`Failed         : ${failedRequests.length}`);
    console.log(`Success Rate   : ${(successCount / NUMBER_OF_REQUESTS * 100).toFixed(2)}%`);
    console.log("══════════════════════════════════════════════");

    if (failedRequests.length === 0) {
        console.log("\nAll requests completed successfully.");
        return;
    }

    let grouped = {};

    for (const f of failedRequests) {
        const key = `${f.status} | ${f.reason}`;
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

    for (const f of failedRequests) {
        const key = `${f.status}`;
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

sendRequests()