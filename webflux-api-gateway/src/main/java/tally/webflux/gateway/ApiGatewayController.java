package tally.webflux.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class ApiGatewayController {
    @GetMapping("/api/hello")
    public Mono<String> hello() {
        int delayMs = ThreadLocalRandom.current().nextInt(10, 100);
        return Mono.delay(Duration.ofMillis(delayMs))
                .thenReturn("OK - " + delayMs + "ms delay");
    }
}
