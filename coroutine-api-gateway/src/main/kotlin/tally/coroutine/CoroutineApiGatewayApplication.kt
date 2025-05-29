package tally.coroutine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoroutineApiGatewayApplication

fun main(args: Array<String>) {
    runApplication<CoroutineApiGatewayApplication>(*args)
}
