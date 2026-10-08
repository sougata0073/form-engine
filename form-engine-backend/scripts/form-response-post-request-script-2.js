// ============================================================
// CONFIGURATION
// ============================================================

const FORM_IDS = [
    "b91cb39f-a944-4c89-b47d-43da924cef6f"
];

const VIEW_URL = "http://localhost:9092/api/v1/forms";
const RESPONSE_URL = "http://localhost:9093/api/v1/forms";

// TOTAL number of response requests across ALL forms
const NUMBER_OF_REQUESTS = 2000;

// Maximum requests PER FORM in each batch
const CONCURRENCY = 1000;


// ============================================================
// QUESTION RESPONSE CONFIGURATION
// ============================================================
//
// true:
//     Send response for ALL questions.
//     required flag is completely ignored.
//
// false:
//     required = true
//         -> ALWAYS send response
//
//     required = false
//         -> Randomly send response according to
//            OPTIONAL_RESPONSE_PROBABILITY
//
// ============================================================

const RESPOND_TO_ALL_QUESTIONS = true;


// Used only when RESPOND_TO_ALL_QUESTIONS = false.
//
// 1.0 = always respond to optional questions
// 0.5 = 50% chance of responding
// 0.0 = never respond to optional questions
//
const OPTIONAL_RESPONSE_PROBABILITY = 0.5;


const HEADERS = {
    "Content-Type": "application/json"
};


// ============================================================
// RANDOM HELPERS
// ============================================================

function randomItem(arr) {

    if (!arr || arr.length === 0) {
        return null;
    }

    return arr[
        Math.floor(
            Math.random() * arr.length
        )
    ];
}


function randomSubset(arr) {

    if (!arr || arr.length === 0) {
        return [];
    }

    const shuffled =
        [...arr].sort(
            () => Math.random() - 0.5
        );

    const count =
        Math.floor(
            Math.random() * arr.length
        ) + 1;

    return shuffled.slice(
        0,
        count
    );
}


function randomDate() {

    const start =
        new Date("2000-01-01");

    const end =
        new Date("2030-12-31");

    return new Date(
        start.getTime() +
        Math.random() *
        (
            end.getTime() -
            start.getTime()
        )
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
        Math.floor(
            Math.random() * 24
        ),
        Math.floor(
            Math.random() * 60
        ),
        0,
        0
    );

    return d.toISOString();
}


function uuid() {

    return crypto.randomUUID();
}


// ============================================================
// FETCH FORM SCHEMA
// ============================================================

async function fetchFormSchema(
    formId,
    userId
) {

    const url =
        `${VIEW_URL}/${formId}/view`;

    const response =
        await fetch(
            url,
            {
                method: "GET",

                headers: {
                    ...HEADERS,
                    "auth-jwt": userId
                }
            }
        );


    if (!response.ok) {

        const message =
            await response.text();

        throw new Error(
            `Failed to fetch form ${formId}. ` +
            `Status: ${response.status}. ` +
            `Response: ${message}`
        );
    }


    const form =
        await response.json();


    if (
        !form.questions ||
        !Array.isArray(
            form.questions
        )
    ) {

        throw new Error(
            `Invalid form response for ${formId}: ` +
            `questions array not found`
        );
    }


    return form;
}


// ============================================================
// SHOULD RESPOND TO QUESTION?
// ============================================================

function shouldRespondToQuestion(
    question
) {

    // ========================================================
    // MODE 1:
    // Respond to EVERY question
    // ========================================================

    if (
        RESPOND_TO_ALL_QUESTIONS === true
    ) {

        return true;
    }


    // ========================================================
    // MODE 2:
    // Respect required flag
    // ========================================================

    // Required question
    // -> ALWAYS respond

    if (
        question.required === true
    ) {

        return true;
    }


    // Optional question
    // -> Random decision

    return (
        Math.random() <
        OPTIONAL_RESPONSE_PROBABILITY
    );
}


// ============================================================
// PAYLOAD GENERATOR
// ============================================================

function generatePayload(
    form,
    index
) {

    const date =
        randomDate();

    const responses = [];


    for (
        const question
        of form.questions
    ) {

        // ====================================================
        // QUESTION RESPONSE DECISION
        // ====================================================

        if (
            !shouldRespondToQuestion(
                question
            )
        ) {

            continue;
        }


        const questionId =
            question.id;

        const questionType =
            question.questionType;

        let response;


        // ----------------------------------------------------
        // SHORT ANSWER
        // ----------------------------------------------------

        if (
            questionType ===
            "SHORT_ANSWER"
        ) {

            response = {

                text:
                    `Short answer ${index}`,

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // PARAGRAPH
        // ----------------------------------------------------

        else if (
            questionType ===
            "PARAGRAPH"
        ) {

            response = {

                text:
                    `Paragraph response ${index}.`,

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // MULTIPLE CHOICE
        // ----------------------------------------------------

        else if (
            questionType ===
            "MULTIPLE_CHOICE"
        ) {

            response = {

                responseOptionId:
                    randomItem(
                        (
                            question.options ||
                            []
                        )
                            .map(
                                option =>
                                    option.id
                            )
                    ),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // CHECKBOX
        // ----------------------------------------------------

        else if (
            questionType ===
            "CHECKBOX"
        ) {

            response = {

                responseOptionIds:
                    randomSubset(
                        (
                            question.options ||
                            []
                        )
                            .map(
                                option =>
                                    option.id
                            )
                    ),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // DROPDOWN
        // ----------------------------------------------------

        else if (
            questionType ===
            "DROPDOWN"
        ) {

            response = {

                responseOptionId:
                    randomItem(
                        (
                            question.options ||
                            []
                        )
                            .map(
                                option =>
                                    option.id
                            )
                    ),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // FILE UPLOAD
        // ----------------------------------------------------

        else if (
            questionType ===
            "FILE_UPLOAD"
        ) {

            response = {

                fileName:
                    `image_${index}.jpg`,

                fileUrl:
                    `https://picsum.photos/seed/${index}/1200/800`,

                fileSize:
                    Math.floor(
                        Math.random() *
                        (
                            10 *
                            1024 *
                            1024 -
                            100 *
                            1024 +
                            1
                        )
                    ) +
                    100 *
                    1024,

                fileMimeType:
                    "image/jpeg",

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // LINEAR SCALE
        // ----------------------------------------------------

        else if (
            questionType ===
            "LINEAR_SCALE"
        ) {

            const from =
                question.fromNumber ??
                1;

            const to =
                question.toNumber ??
                5;

            response = {

                scale:
                    Math.floor(
                        Math.random() *
                        (
                            to -
                            from +
                            1
                        )
                    ) +
                    from,

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // RATING
        // ----------------------------------------------------

        else if (
            questionType ===
            "RATING"
        ) {

            const maxRating =
                question.maxRatingNumber ??
                10;

            response = {

                rating:
                    Math.floor(
                        Math.random() *
                        maxRating
                    ) +
                    1,

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // MULTIPLE CHOICE GRID
        // ----------------------------------------------------

        else if (
            questionType ===
            "MULTIPLE_CHOICE_GRID"
        ) {

            const columns =
                question.columns ||
                [];

            const rows =
                question.rows ||
                [];

            response = {

                rows:
                    rows.map(
                        row => ({

                            rowId:
                                row.id,

                            responseColumnId:
                                randomItem(
                                    columns.map(
                                        column =>
                                            column.id
                                    )
                                )
                        })
                    ),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // TICK BOX GRID
        // ----------------------------------------------------

        else if (
            questionType ===
            "TICK_BOX_GRID"
        ) {

            const columns =
                question.columns ||
                [];

            const rows =
                question.rows ||
                [];

            response = {

                rows:
                    rows.map(
                        row => ({

                            rowId:
                                row.id,

                            responseColumnIds:
                                randomSubset(
                                    columns.map(
                                        column =>
                                            column.id
                                    )
                                )
                        })
                    ),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // DATE
        // ----------------------------------------------------

        else if (
            questionType ===
            "DATE"
        ) {

            response = {

                date:
                    isoDate(date),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // TIME
        // ----------------------------------------------------

        else if (
            questionType ===
            "TIME"
        ) {

            response = {

                time:
                    isoTime(),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // DATE TIME
        // ----------------------------------------------------

        else if (
            questionType ===
            "DATE_TIME"
        ) {

            response = {

                dateTime:
                    date.toISOString(),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // DURATION
        // ----------------------------------------------------

        else if (
            questionType ===
            "DURATION"
        ) {

            response = {

                hours:
                    Math.floor(
                        Math.random() * 73
                    ),

                minutes:
                    Math.floor(
                        Math.random() * 60
                    ),

                seconds:
                    Math.floor(
                        Math.random() * 60
                    ),

                questionId,
                questionType
            };
        }


        // ----------------------------------------------------
        // UNKNOWN QUESTION TYPE
        // ----------------------------------------------------

        else {

            console.warn(
                `Unknown question type "${questionType}" ` +
                `for question ${questionId}. Skipping.`
            );

            continue;
        }


        responses.push(
            response
        );
    }


    return {
        responses
    };
}


// ============================================================
// SUBMIT ONE RESPONSE
//
// ONE GROUP =
//     1 GET schema
//     1 POST response
//
// SAME userId is used for both GET and POST.
// ============================================================

async function submitOneResponse(
    formId,
    requestIndex
) {

    // --------------------------------------------------------
    // UNIQUE USER ID FOR THIS GET + POST GROUP
    // --------------------------------------------------------

    const userId =
        uuid();


    try {

        // ====================================================
        // 1. GET FORM SCHEMA
        // ====================================================

        const form =
            await fetchFormSchema(
                formId,
                userId
            );


        // ====================================================
        // 2. GENERATE PAYLOAD
        // ====================================================

        const payload =
            generatePayload(
                form,
                requestIndex
            );


        // ====================================================
        // 3. POST RESPONSE
        // ====================================================

        const postUrl =
            `${RESPONSE_URL}/${formId}/responses`;


        const response =
            await fetch(
                postUrl,
                {
                    method: "POST",

                    headers: {

                        ...HEADERS,

                        "auth-jwt":
                            userId
                    },

                    body:
                        JSON.stringify(
                            payload
                        )
                }
            );


        if (!response.ok) {

            let message;

            try {

                message =
                    await response.text();

            } catch {

                message =
                    "Unknown error";
            }


            message =
                message
                    .replace(
                        /\n/g,
                        " "
                    )
                    .replace(
                        /\s+/g,
                        " "
                    )
                    .trim()
                    .substring(
                        0,
                        120
                    );


            return {

                success: false,

                request:
                    requestIndex,

                formId,

                status:
                    response.status,

                reason:
                    message ||
                    response.statusText
            };
        }


        return {

            success: true,

            request:
                requestIndex,

            formId
        };


    } catch (error) {

        return {

            success: false,

            request:
                requestIndex,

            formId,

            status:
                "NETWORK",

            reason:
                error.message
        };
    }
}


// ============================================================
// RANDOM REQUEST DISTRIBUTION
//
// NUMBER_OF_REQUESTS is randomly distributed across FORM_IDS.
//
// Example:
//
// NUMBER_OF_REQUESTS = 10
// FORM_IDS = [A, B]
//
// Possible:
//
// A -> 6
// B -> 4
//
// Or:
//
// A -> 3
// B -> 7
//
// If NUMBER_OF_REQUESTS >= FORM_IDS.length,
// every form gets at least 1 request.
// ============================================================

function distributeRequests(
    formIds,
    totalRequests
) {

    const numberOfForms =
        formIds.length;


    if (
        numberOfForms === 0
    ) {

        return [];
    }


    // --------------------------------------------------------
    // Not enough requests to give every form one request.
    // --------------------------------------------------------

    if (
        totalRequests <
        numberOfForms
    ) {

        const shuffled =
            [...formIds].sort(
                () => Math.random() - 0.5
            );


        return formIds.map(
            formId => ({

                formId,

                count:
                    shuffled
                        .slice(
                            0,
                            totalRequests
                        )
                        .includes(
                            formId
                        )
                        ? 1
                        : 0
            })
        );
    }


    // --------------------------------------------------------
    // Give every form one request first.
    // --------------------------------------------------------

    const counts =
        formIds.map(
            formId => ({

                formId,
                count: 1
            })
        );


    let remaining =
        totalRequests -
        numberOfForms;


    // --------------------------------------------------------
    // Randomly distribute remaining requests.
    // --------------------------------------------------------

    while (
        remaining > 0
    ) {

        const randomIndex =
            Math.floor(
                Math.random() *
                numberOfForms
            );

        counts[
            randomIndex
        ].count++;

        remaining--;
    }


    return counts;
}


// ============================================================
// MAIN LOAD TEST
// ============================================================

async function main() {

    console.log(
        "=============================================="
    );

    console.log(
        "DYNAMIC FORM LOAD TEST"
    );

    console.log(
        "=============================================="
    );


    const numberOfForms =
        FORM_IDS.length;


    if (
        numberOfForms === 0
    ) {

        console.error(
            "No form IDs configured."
        );

        return;
    }


    if (
        NUMBER_OF_REQUESTS <= 0
    ) {

        console.error(
            "NUMBER_OF_REQUESTS must be greater than 0."
        );

        return;
    }


    if (
        OPTIONAL_RESPONSE_PROBABILITY < 0 ||
        OPTIONAL_RESPONSE_PROBABILITY > 1
    ) {

        console.error(
            "OPTIONAL_RESPONSE_PROBABILITY must be between 0 and 1."
        );

        return;
    }


    console.log(
        `Forms                  : ${numberOfForms}`
    );

    console.log(
        `Total Requests         : ${NUMBER_OF_REQUESTS}`
    );

    console.log(
        `Concurrency/Form       : ${CONCURRENCY}`
    );

    console.log(
        `Respond To All         : ${RESPOND_TO_ALL_QUESTIONS}`
    );

    if (
        RESPOND_TO_ALL_QUESTIONS === false
    ) {

        console.log(
            `Optional Response Rate : ` +
            `${OPTIONAL_RESPONSE_PROBABILITY * 100}%`
        );
    }


    // ========================================================
    // RANDOMLY DISTRIBUTE REQUESTS ACROSS FORMS
    // ========================================================

    const formRequestCounts =
        distributeRequests(
            FORM_IDS,
            NUMBER_OF_REQUESTS
        );


    console.log(
        "\nREQUEST DISTRIBUTION"
    );

    console.log(
        "----------------------------------------------"
    );


    for (
        const item
        of formRequestCounts
    ) {

        console.log(
            `${item.formId} : ${item.count} requests`
        );
    }


    console.log(
        "----------------------------------------------"
    );


    const distributedTotal =
        formRequestCounts.reduce(
            (
                total,
                item
            ) =>
                total +
                item.count,

            0
        );


    console.log(
        `Distributed Total    : ${distributedTotal}`
    );


    // ========================================================
    // GLOBAL STATISTICS
    // ========================================================

    let totalCompleted =
        0;

    let totalSuccessful =
        0;

    const failedRequests =
        [];


    console.time(
        "TOTAL LOAD TEST TIME"
    );


    // ========================================================
    // CURRENT REQUEST INDEX FOR EACH FORM
    // ========================================================

    const currentIndexes =
        FORM_IDS.map(
            () => 1
        );


    // ========================================================
    // PROCESS BATCHES
    // ========================================================

    let batchNumber = 1;


    while (
        formRequestCounts.some(
            item =>
                currentIndexes[
                    FORM_IDS.indexOf(
                        item.formId
                    )
                ] <= item.count
        )
    ) {

        console.log(
            `\nBATCH ${batchNumber}`
        );


        const batchPromises =
            [];


        // ====================================================
        // CREATE REQUESTS FOR ALL FORMS
        // ====================================================

        for (
            let formIndex = 0;

            formIndex <
            numberOfForms;

            formIndex++
        ) {

            const formId =
                FORM_IDS[
                    formIndex
                ];

            const totalForThisForm =
                formRequestCounts[
                    formIndex
                ].count;

            const startIndex =
                currentIndexes[
                    formIndex
                ];


            if (
                startIndex >
                totalForThisForm
            ) {

                continue;
            }


            const endIndex =
                Math.min(
                    startIndex +
                    CONCURRENCY -
                    1,

                    totalForThisForm
                );


            currentIndexes[
                formIndex
            ] =
                endIndex + 1;


            // =================================================
            // START REQUESTS FOR THIS FORM
            // =================================================

            for (
                let requestIndex =
                    startIndex;

                requestIndex <=
                endIndex;

                requestIndex++
            ) {

                const promise =
                    submitOneResponse(
                        formId,
                        requestIndex
                    )
                        .then(
                            result => {

                                totalCompleted++;


                                if (
                                    result.success
                                ) {

                                    totalSuccessful++;

                                    process.stdout
                                        ?.write?.(
                                            "✅ "
                                        );

                                } else {

                                    failedRequests.push(
                                        result
                                    );

                                    process.stdout
                                        ?.write?.(
                                            "❌ "
                                        );
                                }


                                process.stdout
                                    ?.write?.(
                                        `${totalCompleted}/${NUMBER_OF_REQUESTS} `
                                    );


                                return result;
                            }
                        );


                batchPromises.push(
                    promise
                );
            }
        }


        // ====================================================
        // WAIT FOR ENTIRE BATCH
        // ====================================================

        await Promise.all(
            batchPromises
        );


        // ====================================================
        // MOVE TO NEXT LINE
        // ====================================================

        process.stdout
            ?.write?.(
                "\n"
            );


        batchNumber++;
    }


    console.timeEnd(
        "TOTAL LOAD TEST TIME"
    );


    // ========================================================
    // SUMMARY
    // ========================================================

    console.log(
        "\n"
    );

    console.log(
        "══════════════════════════════════════════════"
    );

    console.log(
        "LOAD TEST SUMMARY"
    );

    console.log(
        "══════════════════════════════════════════════"
    );

    console.log(
        `Total Requests : ${NUMBER_OF_REQUESTS}`
    );

    console.log(
        `Successful     : ${totalSuccessful}`
    );

    console.log(
        `Failed         : ${failedRequests.length}`
    );

    console.log(
        `Success Rate   : ` +
        `${
            (
                totalSuccessful /
                NUMBER_OF_REQUESTS *
                100
            ).toFixed(2)
        }%`
    );

    console.log(
        "══════════════════════════════════════════════"
    );


    // ========================================================
    // FAILURE DETAILS
    // ========================================================

    if (
        failedRequests.length === 0
    ) {

        console.log(
            "\nAll requests completed successfully."
        );

        return;
    }


    // ========================================================
    // GROUP FAILURE REASONS
    // ========================================================

    let grouped = {};


    for (
        const failure
        of failedRequests
    ) {

        const key =
            `${failure.status} | ${failure.reason}`;


        grouped[key] =
            (
                grouped[key] ||
                0
            ) + 1;
    }


    console.log(
        "\nFAILED REQUESTS"
    );

    console.log(
        "══════════════════════════════════════════════"
    );


    Object.entries(
        grouped
    )

        .sort(
            (a, b) =>
                b[1] - a[1]
        )

        .forEach(
            ([message, count]) => {

                console.log(
                    `${count}x  ${message}`
                );
            }
        );


    console.log(
        "══════════════════════════════════════════════"
    );


    // ========================================================
    // STATUS BREAKDOWN
    // ========================================================

    grouped = {};


    for (
        const failure
        of failedRequests
    ) {

        const key =
            `${failure.status}`;


        grouped[key] =
            (
                grouped[key] ||
                0
            ) + 1;
    }


    console.log(
        "\n📊 FAILURE BREAKDOWN"
    );


    Object.entries(
        grouped
    )

        .sort(
            (a, b) =>
                b[1] - a[1]
        )

        .forEach(
            ([status, count]) => {

                console.log(
                    `   ${status} : ${count}`
                );
            }
        );


    console.log();
}


// ============================================================
// START
// ============================================================

main();