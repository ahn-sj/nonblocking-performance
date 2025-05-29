package tally.coroutine.gateway

import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class ApiGatewayController {

    @GetMapping("/api/hello")
    suspend fun hello(): String {
        val delayMs = Random.nextInt(10, 100)
        delay(delayMs.toLong())
        return "OK - ${delayMs}ms delay"
    }
}