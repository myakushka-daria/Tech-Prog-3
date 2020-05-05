
import com.myakushko.model.Hero
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
import kotlinx.css.input
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException

fun printHeroes(heroes: ArrayList<Hero>) {
    println("Your heroes:\nstr\tagi\tint")
    for (i in 0 until heroes.size)
        println("$i) ${heroes[i].name}\n${heroes[i].strength}\t${heroes[i].agility}\t${heroes[i].intelligence}")
}
fun chooseHero(heroes: ArrayList<Hero>): String {
    var heroName = ""
    do {
        try {
            println("Enter hero number: ")
            val position = readLine()?.toInt()
            heroName = heroes[position!!].name
        } catch (e: NumberFormatException) {
            println("Try again")
        } catch (e: IndexOutOfBoundsException) {
            println("Try again")
        }
    } while (heroName == "")
    return heroName
}

fun main(args: Array<String>) {
    runBlocking {
        println("Heroes Online")
        print("Connection...")
        var player: Player = Api.createPlayer()
        println("You are connected. Waiting other users...")
        var room: Room? = null
        while (room == null) {
            Thread.sleep(5000)
            try {
                room = Api.checkOrCreateRoom(player.playerId!!)
            } catch (e: ClientRequestException) {
                print("")
                // 404 NotFound while all users dont connect to the server
            }
        }
        // game started
        println("game started")
        var round = 0
        gameloop@while (room!!.winner == null) {
            if (player.playerId == room.player1.playerId)
                player = room.player1
            else
                player = room.player2
            println("________________NEXT ROUND________________")
            printHeroes(player.heroes)
            val heroName = chooseHero(player.heroes)
            Api.fight(room.roomId, player.playerId!!, heroName)
            println("Waiting other users...")
            do  {
                Thread.sleep(5000)
                room = Api.getRoom(room!!.roomId)
                if (room.winner != null) break@gameloop
            } while (room!!.fightHistory.size == round)
            val fightInfo = room.fightHistory[round]
            println("${fightInfo.first!!.name} vs ${fightInfo.second!!.name}")
            println("str: ${fightInfo.first!!.strength}\t${fightInfo.second!!.strength}")
            println("agi: ${fightInfo.first!!.agility}\t${fightInfo.second!!.agility}")
            println("int: ${fightInfo.first!!.intelligence}\t${fightInfo.second!!.intelligence}")
            if (fightInfo.third == player.playerId) {
                println("Your hero win!")
            } else if (fightInfo.third == null) {
                println("Draw")
            } else println("Hero died!")
            round++
        }
        if (room.winner == player.playerId) {
            println("You win!")
        } else if (room.winner == "draw"){
            println("DRAW!")
        } else {
            println("You lose!")
        }
        Api.close()
    }
}