package tally.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import tally.webflux.gateway.ApiGatewayController;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<?> routes(ApiGatewayController handler) {
        return route(GET("/api/dp"), handler::getDisplayItem);
    }
}
