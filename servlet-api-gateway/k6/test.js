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
        http_req_failed: ['rate<0.01'],           // 실패율 1% 이하 목표
        http_req_duration: ['avg<100', 'p(95)<100'], // 평균, P95 모두 100ms 미만
    },
};

export default function () {
    const res = http.get('http://servlet-api:8080/api/hello');

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 100ms': (r) => r.timings.duration < 100,
    });
}
