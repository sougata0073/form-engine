const POST_URL =
    "http://localhost:9093/api/v1/forms/d27b590f-a119-489e-871e-fd04676c88bc/responses";

const NUMBER_OF_REQUESTS = 2000;
const CONCURRENCY = 1000;

const HEADERS = {
    "Content-Type": "application/json"
};

const QUESTION_IDS = {
    SHORT_ANSWER: "891942037265799335",
    PARAGRAPH: "891942038003996839",
    MULTIPLE_CHOICE: "891942038712834228",
    CHECKBOX: "891942039719467168",
    DROPDOWN: "891942040667379902",
    FILE_UPLOAD: "891942041661429922",
    LINEAR_SCALE: "891942042424793337",
    RATING: "891942043179768043",
    MULTIPLE_CHOICE_GRID: "891942043876022522",
    TICK_BOX_GRID: "891942045125925091",
    DATE: "891942046417770669",
    TIME: "891942047101442213",
    DATE_TIME: "891942047856417013",
    DURATION: "891942048984684778"
};

const MULTIPLE_CHOICE_OPTIONS = [
    "891942038717028593",
    "891942038717028594",
    "891942038717028595",
    "891942038717028596",
    "891942038717028597"
];

const CHECKBOX_OPTIONS = [
    "891942039719467169",
    "891942039719467170",
    "891942039719467171",
    "891942039719467172"
];

const DROPDOWN_OPTIONS = [
    "891942040675768486",
    "891942040675768487",
    "891942040675768488",
    "891942040675768489",
    "891942040675768490"
];

const MCG_ROWS = [
    "891942043876022533",
    "891942043876022534",
    "891942043876022535",
    "891942043876022536",
    "891942043876022537"
];

const MCG_COLUMNS = [
    "891942043876022523",
    "891942043876022524",
    "891942043876022525",
    "891942043876022526",
    "891942043876022527",
    "891942043876022528",
    "891942043876022529",
    "891942043876022530",
    "891942043876022531",
    "891942043876022532"
];

const TBG_ROWS = [
    "891942045125925102",
    "891942045125925103",
    "891942045125925104",
    "891942045125925105",
    "891942045125925106"
];

const TBG_COLUMNS = [
    "891942045125925092",
    "891942045125925093",
    "891942045125925094",
    "891942045125925095",
    "891942045125925096",
    "891942045125925097",
    "891942045125925098",
    "891942045125925099",
    "891942045125925100",
    "891942045125925101"
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
            // {
            //     text: `Short answer ${index}`,
            //     questionId: QUESTION_IDS.SHORT_ANSWER,
            //     questionType: "SHORT_ANSWER"
            // },
            // {
            //     text: `Paragraph response ${index}.`,
            //     questionId: QUESTION_IDS.PARAGRAPH,
            //     questionType: "PARAGRAPH"
            // },
            // {
            //     responseOptionId: randomItem(MULTIPLE_CHOICE_OPTIONS),
            //     questionId: QUESTION_IDS.MULTIPLE_CHOICE,
            //     questionType: "MULTIPLE_CHOICE"
            // },
            {
                responseOptionIds: randomSubset(CHECKBOX_OPTIONS),
                questionId: QUESTION_IDS.CHECKBOX,
                questionType: "CHECKBOX"
            },
            // {
            //     responseOptionId: randomItem(DROPDOWN_OPTIONS),
            //     questionId: QUESTION_IDS.DROPDOWN,
            //     questionType: "DROPDOWN"
            // },
            // {
            //     fileName: `image_${index}.jpg`,
            //     fileUrl: `https://picsum.photos/seed/${index}/1200/800`,
            //     fileSize:
            //         Math.floor(
            //             Math.random() * (10 * 1024 * 1024 - 100 * 1024 + 1)
            //         ) + 100 * 1024,
            //     fileMimeType: "image/jpeg",
            //     questionId: QUESTION_IDS.FILE_UPLOAD,
            //     questionType: "FILE_UPLOAD"
            // },
            // {
            //     scale: Math.floor(Math.random() * 5) + 1,
            //     questionId: QUESTION_IDS.LINEAR_SCALE,
            //     questionType: "LINEAR_SCALE"
            // },
            // {
            //     rating: Math.floor(Math.random() * 10) + 1,
            //     questionId: QUESTION_IDS.RATING,
            //     questionType: "RATING"
            // },
            // {
            //     rows: MCG_ROWS.map(rowId => ({
            //         rowId,
            //         responseColumnId: randomItem(MCG_COLUMNS)
            //     })),
            //     questionId: QUESTION_IDS.MULTIPLE_CHOICE_GRID,
            //     questionType: "MULTIPLE_CHOICE_GRID"
            // },
            // {
            //     rows: TBG_ROWS.map(rowId => ({
            //         rowId,
            //         responseColumnIds: randomSubset(TBG_COLUMNS)
            //     })),
            //     questionId: QUESTION_IDS.TICK_BOX_GRID,
            //     questionType: "TICK_BOX_GRID"
            // },
            // {
            //     date: isoDate(date),
            //     questionId: QUESTION_IDS.DATE,
            //     questionType: "DATE"
            // },
            // {
            //     time: isoTime(),
            //     questionId: QUESTION_IDS.TIME,
            //     questionType: "TIME"
            // },
            // {
            //     dateTime: date.toISOString(),
            //     questionId: QUESTION_IDS.DATE_TIME,
            //     questionType: "DATE_TIME"
            // },
            // {
            //     hours: Math.floor(Math.random() * 73),
            //     minutes: Math.floor(Math.random() * 60),
            //     seconds: Math.floor(Math.random() * 60),
            //     questionId: QUESTION_IDS.DURATION,
            //     questionType: "DURATION"
            // }
        ]
    };
}

async function sendRequests() {
    console.time("⏱ Total Time");

    const failedRequests = [];
    let successCount = 0;

    for (
        let start = 1;
        start <= NUMBER_OF_REQUESTS;
        start += CONCURRENCY
    ) {
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
            `  [${Math.min(
                start + CONCURRENCY - 1,
                NUMBER_OF_REQUESTS
            )}/${NUMBER_OF_REQUESTS}]\n`
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
    console.log(
        `Success Rate   : ${(
            (successCount / NUMBER_OF_REQUESTS) *
            100
        ).toFixed(2)}%`
    );
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

sendRequests();