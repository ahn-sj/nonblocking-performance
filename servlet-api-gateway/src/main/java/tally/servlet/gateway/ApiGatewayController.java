package tally.servlet.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class ApiGatewayController {

    @GetMapping("/api/hello")
    public String hello() throws InterruptedException {
        // 10ms ~ 100ms 랜덤 딜레이
        int delay = ThreadLocalRandom.current().nextInt(10, 100);
        Thread.sleep(delay);
        return "OK - " + delay + "ms delay";
    }
}
