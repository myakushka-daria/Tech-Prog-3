package com.myakushko.model

class Room(val roomId: String, val player1: Player, val player2: Player) {

    var hero1: Hero? = null
    var hero2: Hero? = null
    val fightHistory = ArrayList<Triple<Hero?, Hero?, String?>>()
    var winner: String? = null

    // args: hero1 - Player1, hero2 - Player2
    // return: player winner id
    fun fight(hero1: Hero, hero2: Hero): String? {
        var result = 0;
        if (hero1.agility > hero2.agility) result++
        else if (hero1.agility < hero2.agility)  result--
        if (hero1.strength > hero2.strength) result++
        else if (hero1.strength < hero2.strength) result--
        if(hero1.intelligence > hero2.intelligence) result++
        else if (hero1.intelligence < hero2.intelligence) result--
        fightHistory.add(
            Triple(Hero(hero1), Hero(hero2), when {
            result > 0 -> {
                player2.heroes.remove(hero2)
                player1.playerId
            }
            result < 0 -> {
                player1.heroes.remove(hero1)
                player2.playerId
            }
            else -> {
                // both heroes alive
                player1.heroes.remove(hero1)
                player2.heroes.remove(hero2)
                null
            }
        }))
        this.hero1 = null
        this.hero2 = null
        return fightHistory[fightHistory.size - 1].third
    }

    fun checkWinner() {
        winner = when {
            player1.heroes.isEmpty() && player2.heroes.isEmpty() -> {
                "draw"
            }
            player1.heroes.isEmpty() -> {
                player2.playerId
            }
            player2.heroes.isEmpty() -> {
                player1.playerId
            }
            else -> null
        }
    }
}