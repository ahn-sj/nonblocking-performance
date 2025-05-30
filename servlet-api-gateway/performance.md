# k6 성능 테스트 리포트: Servlet 기반

## 1. 테스트 개요

| 항목               | 내용                                       |
|--------------------|--------------------------------------------|
| 대상 API           | `GET /api/hello`                           |
| 서버 응답 로직     | `Thread.sleep(10~99ms)` 랜덤 지연          |
| 목표               | 평균 응답 시간 100ms 이하                  |
| 최대 동시 사용자   | 1,200 VUs                                  |
| 총 요청 수         | 264,325 건                                 |
| 테스트 도구        | k6 (Docker 기반 실행)                      |

---

## 2. 주요 메트릭 요약

| 지표                        | 측정값         | 목표      |
|-----------------------------|----------------|---------|
| 평균 응답 시간 (avg)         | 189.36ms       | < 100ms |
| P90 응답 시간               | 289.44ms       | < 100ms |
| P95 응답 시간               | 308.62ms       | < 100ms |
| 최대 응답 시간               | 369.23ms       | -       |
| 성공 응답률 (status 200)   | 100%           | 100%    |
| 100ms 미만 응답 비율         | 16.6%          | -       |
| 요청 실패율 (`http_req_failed`) | 0.00%           | < 1%    |
| 처리량 (RPS)                | 3,771 req/s    | -       |

---

## 3. 체크 조건 결과

| 조건                          | 결과                             | 통과 여부 |
|-------------------------------|----------------------------------|-----------|
| `status is 200`               | 264,325 성공 / 0 실패            | 성공      |
| `http_req_failed < 1%`        | 0.00% 실패율                     | 성공      |
| `response time < 100ms`       | 43,969 성공 / 220,356 실패 (16.6%) | 실패    |
| `http_req_duration.avg < 100` | 189.36ms                         | 실패      |
| `http_req_duration.p(95)<100` | 308.62ms                         | 실패      |

---

## 4. 상세 분석

- 100% 정상 응답률 유지
- RPS 3,700 이상으로 처리량
- 요청 실패 없음 → 안정적인 시스템 구조
- P95가 목표 100ms를 초과
- 응답 속도 하한은 10ms지만 평균이 189ms까지 상승
- Servlet은 Thread-per-request 방식이기 때문에 병목 발생 시 전체 속도 저하

---

## 5. 결론 및 제안

### 결론

현재 Servlet 구조는 1,200 VUs 부하에서 평균 응답 시간 100ms 이하 달성에 실패

---

## 6. 요약

| 항목               | 값                   |
|--------------------|----------------------|
| 평균 응답 시간     | 189.36ms             |
| P95 응답 시간      | 308.62ms             |
| 100ms 미만 비율    | 16.6%                |
| 실패율             | 0%                   |
| 테스트 시뮬레이션 | 1,200 VUs, 총 264K 요청 |
