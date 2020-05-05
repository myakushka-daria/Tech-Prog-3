

import com.myakushko.model.Player
import com.myakushko.model.Room
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class GameService {
    val rooms: ArrayList<Room> = ArrayList()
    val playersPool: ArrayList<Player> = ArrayList()

    fun findPlayerRoom(playerId: String): String? {
        for (room in rooms) {
            if (room.player1.playerId == playerId || room.player2.playerId == playerId)
                return room.roomId
        }
        return null
    }

    fun findPlayerPositionById(id: String): Int? {
        for (position in 0 until playersPool.size)
            if (playersPool[position].playerId == id) return position
        return null
    }

    fun findRoomPositionById(id: String): Int? {
        for (position in 0 until rooms.size)
            if (rooms[position].roomId == id) return position
        return null
    }

    suspend fun getRoom(roomId: String): Room? {
        findRoomPositionById(roomId)?.let {
            return rooms[it]
        }
        return null
    }

    suspend fun createPlayer(player: Player): Player {
        player.playerId = UUID.randomUUID().toString()
        playersPool.add(player)
        return player
    }

    //---
    suspend fun fight(roomId: String, playerId: String, heroName: String): Room? {
        findRoomPositionById(roomId)?.let {
            val room = rooms[it]
            if (room.player1.playerId == playerId) {
                room.hero1 = room.player1.findHero(heroName)
            }
            if (room.player2.playerId == playerId) {
                room.hero2 = room.player2.findHero(heroName)
            }
            if (room.hero1 != null && room.hero2 != null) {
                room.fight(room.hero1!!, room.hero2!!)
                room.checkWinner()
            }
            return room
        }
        return null
    }

    suspend fun checkRoom(userId: String): Room? {
        findPlayerRoom(userId)?.let { roomId ->
            findRoomPositionById(roomId)?.let { roomPosition ->
                return rooms[roomPosition]
            }
        }
        return if (playersPool.size >= playersInOneRoom) {
            findPlayerPositionById(userId)?.let {
                val player1 = playersPool[it]
                var player2Position = -1
                do {
                    player2Position = Random.nextInt(playersPool.size)
                } while (it == player2Position)
                val player2 = playersPool[player2Position]
                val roomId = UUID.randomUUID().toString()
                player1.roomId = roomId
                player2.roomId = roomId
                val room = Room(UUID.randomUUID().toString(), player1, player2)
                rooms.add(room)
                playersPool.remove(player1)
                playersPool.remove(player2)
                room
            }
        }else null
    }

    companion object {
        const val playersInOneRoom = 2
    }
}