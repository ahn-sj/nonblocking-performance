package tally.webflux.gateway;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;

@Component
public class ApiGatewayController {
    private final SecureRandom random = new SecureRandom();

    public Mono<ServerResponse> getDisplayItem(ServerRequest request) {
        int delay = 10 + random.nextInt(41);

        return Mono.delay(java.time.Duration.ofMillis(delay))
                .flatMap(t -> ServerResponse.ok().bodyValue("OK - " + delay + "ms delay"));
    }
}
