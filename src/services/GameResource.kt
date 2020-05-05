

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.myakushko.model.Player
import com.myakushko.model.Room
import com.sun.org.apache.xpath.internal.operations.Bool
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.http.cio.websocket.*
import org.apache.http.HttpStatus

const val WIDGET_END_POINT_GAME = "/game"
const val WIDGET_END_POINT_USER = "/user"
val mapper = jacksonObjectMapper().apply { setSerializationInclusion(JsonInclude.Include.NON_NULL) }

fun Route.widget(gameService: GameService){

    route(WIDGET_END_POINT_GAME) {
        get("/room/{roomId}") {
            val room = gameService.getRoom(call.parameters["roomId"]?.toString()!!)
            if (room != null) call.respond(room)
            else call.respond(HttpStatusCode.NotFound)
        }

        post("/room/fight/{roomId}&{playerId}&{heroName}") {
            val roomId = call.parameters["roomId"]?.toString()!!
            val heroName = call.parameters["heroName"]?.toString()!!
            val playerId = call.parameters["playerId"]?.toString()!!
            val room = gameService.fight(roomId, playerId, heroName)
            if (room != null) call.respond(room)
            else call.respond(HttpStatusCode.NotFound)
        }
    }

    route(WIDGET_END_POINT_USER){
        post("/new") {
            val player = call.receive<Player>()
            call.respond(HttpStatusCode.Created, gameService.createPlayer(player))
        }

        get("/getRoom/{playerId}") {
            val playerId: String = call.parameters["playerId"]?.toString()!!
            val room: Room? = gameService.checkRoom(playerId)
            if (room == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(room)
        }
    }
}