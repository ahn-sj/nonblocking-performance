package tally.servlet.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;

@RestController
public class ApiGatewayController {
    private final SecureRandom random = new SecureRandom();

    @GetMapping("/api/dp")
    public String getDisplayItem() {
        // 10ms ~ 50ms 랜덤 딜레이
        int delay = 10 + random.nextInt(41);
        sleep(delay);

        return "OK - " + delay + "ms delay";
    }

    private static void sleep(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
