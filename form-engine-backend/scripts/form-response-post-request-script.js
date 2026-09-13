const POST_URL =
    "http://localhost:9093/api/v1/forms/33efc36c-89bf-4056-a672-b946afe1e654/responses";

const NUMBER_OF_REQUESTS = 2000;
const CONCURRENCY = 2000;

const HEADERS = {
    "Content-Type": "application/json"
};

const QUESTION_IDS = {
    SHORT_ANSWER: "887002514037696897",
    PARAGRAPH: "887002523416162277",
    MULTIPLE_CHOICE: "887002533369243874",
    CHECKBOX: "887002538305937679",
    DROPDOWN: "887002542810623024",
    FILE_UPLOAD: "887002548703620972",
    LINEAR_SCALE: "887002553078276576",
    RATING: "887002557150947751",
    MULTIPLE_CHOICE_GRID: "887002561139730133",
    TICK_BOX_GRID: "887002565350810928",
    DATE: "887002569385730481",
    TIME: "887002573403876272",
    DATE_TIME: "887002576939671820",
    DURATION: "887002580748101407"
};

const MULTIPLE_CHOICE_OPTIONS = [
    "887002773266653986",
    "887002870754862087",
    "887002870754862088",
    "887002870754862089",
    "887002870754862090"
];

const CHECKBOX_OPTIONS = [
    "887002887712432421",
    "887002934709609141",
    "887002934709609142",
    "887002934709609143"
];

const DROPDOWN_OPTIONS = [
    "887002949054128855",
    "887003000669236383",
    "887003000669236384",
    "887003000669236385",
    "887003000669236386"
];

const MCG_ROWS = [
    "887003183813518990",
    "887003298322214065",
    "887003298322214066",
    "887003298322214067",
    "887003298322214068"
];

const MCG_COLUMNS = [
    "887003183813518991",
    "887003298330600709",
    "887003298330600710",
    "887003298330600711",
    "887003298330600712",
    "887003298330600713",
    "887003298330600714",
    "887003298330600715",
    "887003298330600716",
    "887003298330600717"
];

const TBG_ROWS = [
    "887003318668781575",
    "887003414126944840",
    "887003414126944841",
    "887003414126944842",
    "887003414126944843"
];

const TBG_COLUMNS = [
    "887003318668781576",
    "887003414131140422",
    "887003414131140423",
    "887003414131140424",
    "887003414131140425",
    "887003414131140426",
    "887003414131140427",
    "887003414131140428",
    "887003414131140429",
    "887003414131140430"
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
                fileSize:
                    Math.floor(
                        Math.random() * (10 * 1024 * 1024 - 100 * 1024 + 1)
                    ) + 100 * 1024,
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