package com.myakushko.model

import kotlin.random.Random

class Player(var playerId: String?=null, var roomId: String? = null) {
    val heroes:  ArrayList<Hero> = ArrayList<Hero>()

    init {
        generateHeroes()
    }

    fun findHero(heroName: String): Hero? {
        for (hero in heroes)
            if (hero.name == heroName)
                return hero
        return null
    }

    fun generateHeroes() {
        val heroesNames = ArrayList<String>()
        for (i in 0 until 5) {
            heroesNames.add(Hero.generateName(heroesNames))
            heroes.add(
                Hero(
                    heroesNames[i],
                    Random.nextInt(1, 11),
                    Random.nextInt(1, 11),
                    Random.nextInt(1, 11)
                )
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        return playerId == (other as Player).playerId
    }
}