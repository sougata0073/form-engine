const url = "http://localhost:9092/api/v1/forms/b91cb39f-a944-4c89-b47d-43da924cef6f/details";

const TOTAL_REQUESTS = 10000;
const CONCURRENCY = 10000;

async function sendRequest(index) {
    const authJwt = crypto.randomUUID();

    try {
        const response = await fetch(url, {
            headers: {
                "auth-jwt": authJwt
            }
        });

        console.log(
            `Request ${index}: ${response.status} | auth-jwt: ${authJwt}`
        );

        return response.ok;
    } catch (error) {
        console.error(`Request ${index} failed:`, error);
        return false;
    }
}

async function run() {
    let nextRequest = 0;

    async function worker() {
        while (true) {
            const index = nextRequest++;

            if (index >= TOTAL_REQUESTS) {
                return;
            }

            await sendRequest(index + 1);
        }
    }

    const workers = Array.from(
        { length: CONCURRENCY },
        () => worker()
    );

    await Promise.all(workers);

    console.log(`All ${TOTAL_REQUESTS} requests completed`);
}

run();