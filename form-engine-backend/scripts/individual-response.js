const BASE_URL =
    "http://localhost:9093/api/v1/forms/b91cb39f-a944-4c89-b47d-43da924cef6f/responses";

const START_PAGE = 0;
const END_PAGE = 39808;
const CONCURRENCY = 5000;

let nextPage = START_PAGE;
let completed = 0;
let failed = 0;

async function fetchPage(page) {
    const url = `${BASE_URL}?page=${page}`;

    try {
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(
                `HTTP ${response.status} ${response.statusText}`
            );
        }

        const data = await response.json();

        completed++;

        console.log(
            `[${completed}/${END_PAGE - START_PAGE + 1}] page=${page}`
        );

        return {
            page,
            success: true,
            data
        };

    } catch (error) {
        failed++;

        console.error(
            `page=${page} failed:`,
            error.message
        );

        return {
            page,
            success: false,
            data: null
        };
    }
}

async function worker() {
    while (true) {
        const page = nextPage++;

        if (page > END_PAGE) {
            return;
        }

        await fetchPage(page);
    }
}

async function main() {
    const startTime = Date.now();

    const totalPages = END_PAGE - START_PAGE + 1;

    console.log(`Total pages: ${totalPages}`);
    console.log(`Concurrency: ${CONCURRENCY}`);

    const workers = Array.from(
        { length: CONCURRENCY },
        () => worker()
    );

    await Promise.all(workers);

    const duration =
        ((Date.now() - startTime) / 1000).toFixed(2);

    console.log("\nFinished");
    console.log(`Successful: ${completed}`);
    console.log(`Failed: ${failed}`);
    console.log(`Time: ${duration}s`);
}

main();