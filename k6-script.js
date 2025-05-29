import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 10000,           // 100명의 가상 사용자
  duration: '30s',     // 1분간 부하
};

export default function () {
  http.get('http://webflux-gateway:8080/api/dp');  // 여기만 바꿔서 테스트 (8082, 8083 번갈아 가면서)
  sleep(1);
}
