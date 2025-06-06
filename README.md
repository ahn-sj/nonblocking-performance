## 1. 문제 정의

- 최근 대규모 트래픽을 다루는 기업들의 채용 공고에서 WebFlux, Coroutine 등 **비동기 웹 프레임워크 경험을 우대**하는 경향을 보았음
- 이와 관련하여 동시 처리량(throughput)을 향상시키기 위한 방법으로 비동기 프레임워크를 사용한다고 생각하는데 이 가설을 검증하기 위해 throughput 요구가 많은 API Gateway를 구현 및 비교 테스트하고자 함 

1. Servlet 기반: Blocking I/O 기반, 전통적인 스프링 처리 모델
2. WebFlux 기반: Reactive Streams 기반, 완전한 non-blocking 처리
3. Coroutine 기반: Structured Concurrency 기반의 경량 비동기 처리 모델

## 2. 목표

- 비동기 프레임워크(WebFlux, Coroutine)가 실제로 동기 방식(Servlet) 대비 더 높은 throughput을 제공하는지를 성능 테스트를 통해 검증한다.
- 외부로부터 HTTP 요청을 받아서, 내부 서비스(목표 API)로 Forward만 하는 단순 Gateway 역할을 한다.
- API 서버는 목표 평균 응답시간인 100ms 이내로 응답할 수 있도록 10~100ms 랜덤 응답 지연을 적용한다.

> API Gateway는 외부 요청을 라우팅하는 엔드포인트로서 throughput 테스트에 이상적이고 WebFlux 기반의 대표적인 API Gateway로 SCG도 있기 때문에 적절한듯

- 3가지 아키텍처(Servlet, WebFlux, Coroutine) 기반 API Gateway를 구현
- 컨테이너 환경에서 동일한 조건으로 k6를 활용한 부하 테스트 수행
- 성능 지표 정의:
    - Requests Per Second (RPS)
    - 평균 및 최대 응답 시간 (Latency)
    - CPU 및 메모리 사용량
    - 에러율

## 3. 벤치마크 기준

| 항목                       | 설명                                            |
|------------------------------|-------------------------------------------------|
| RPS (Requests per Second)    | 초당 처리 요청 수                               |
| Average Latency              | 평균 응답 시간 (ms)                             |
| 95th, 99th Percentile Latency| 최악의 경우 응답 시간 (ms) (상위 5%, 1%)        |
| CPU 사용률                   | 평균 CPU 사용량 (%)                             |
| Memory 사용량                | 평균 메모리 사용량 (MB)                         |

## 4. 최종 결론


### 테스트 조건 요약

| 항목               | 값                            |
|--------------------|-------------------------------|
| 테스트 툴          | k6 (Docker 기반)              |
| 테스트 방식        | Gradual Load (단계별 증가)    |
| 최대 동시 사용자   | 1,200 VUs                     |
| 테스트 총 시간     | 70초                          |
| 요청 타겟          | `/api/hello` (10~100ms delay) |
| 환경               | Docker Compose로 동일한 조건  |

### 테스트 결과 요약

| 아키텍처    | 평균 응답 시간 | P95 응답 시간 | 100ms 미만 비율 | 실패율 | RPS (처리량) |
|-------------|----------------|----------------|------------------|--------|--------------|
| Servlet     | 189.36ms       | 308.62ms       | 16.6%            | 0%     | 3,771 req/s  |
| WebFlux     | 55.53ms        | 95.97ms        | 99.49%           | 0%     | 12,840 req/s |
| Coroutine   | 55.44ms        | 95.94ms        | 99.58%           | 0%     | 12,862 req/s |

### 테스트 스크립트

```js
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
    const res = http.get('http://<target-host>:8080/api/hello');
    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 100ms': (r) => r.timings.duration < 100,
    });
}
```
