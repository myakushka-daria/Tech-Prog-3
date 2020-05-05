import com.myakushko.model.Player
import com.myakushko.model.Room
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.runBlocking
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parametersOf

object Api {
    var client: HttpClient = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    // .GsonBuilder
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        }

    suspend fun createPlayer(): Player = client.post<Player> {
        url("http://127.0.0.1:8081/user/new")
        contentType(ContentType.Application.Json)
        body = Player()
    }

    suspend fun checkOrCreateRoom(playerId: String): Room = client.get<Room> {
        url("http://127.0.0.1:8081/user/getRoom/{playerId}")
        parameter("playerId", playerId)
    }

    suspend fun getRoom(roomId: String): Room = client.get<Room> {
        url("http://127.0.0.1:8081/game/room/{roomId}")
        parameter("roomId", roomId)
    }

    suspend fun fight(roomId: String, playerId: String, heroName: String) = client.post<Room> {
        url("http://127.0.0.1:8081/game/room/fight/{roomId}&{playerId}&{heroName}")
        parameter("roomId", roomId)
        parameter("playerId", playerId)
        parameter("heroName", heroName)
    }

    fun reopen() {
        client = HttpClient(io.ktor.client.engine.apache.Apache) {
            install(io.ktor.client.features.json.JsonFeature) {
                serializer = io.ktor.client.features.json.GsonSerializer {
                    // .GsonBuilder
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        }
    }

    fun close() {
        client.close()
    }
}