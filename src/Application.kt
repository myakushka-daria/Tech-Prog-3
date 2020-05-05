import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>): Unit {
    // io.ktor.server.netty.EngineMain.main(args)
    val server = embeddedServer(
        Netty,
        port = 8081,
        module = Application::mymodule
    ).apply {
        start(wait = false)
    }
}

fun Application.mymodule() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation){
        gson {
            setPrettyPrinting()
        }
    }
    install(Routing){ widget(gameService = GameService()) }
}