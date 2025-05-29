import http from 'k6/http';
import { check } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 400 },
        { duration: '20s', target: 800 },
        { duration: '30s', target: 1200 },
        { duration: '10s', target: 0 },
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['avg<100', 'p(95)<100'],
    },
};

export default function () {
    const res = http.get('http://coroutine-api:8080/api/hello');

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 100ms': (r) => r.timings.duration < 100,
    });
}
