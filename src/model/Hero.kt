package com.myakushko.model

import kotlin.random.Random

class Hero(val name: String, val strength: Int, val agility: Int, val intelligence: Int) {

    override fun equals(other: Any?): Boolean {
        return name == (other as Hero).name
    }

    constructor(hero: Hero): this(hero.name, hero.strength, hero.agility, hero.intelligence)

    companion object {
        private val firstNames = arrayOf("Sokrat", "Gektor", "Pompey", "Franczisk", "Lyudovik",
        "Tiberiy", "Zenon", "Platon", "Narcziss", "Germiy", "Emilian", "Gerakl", "Karl", "Timur", "Zigfrid", "Tristan",
         "Gay", "Ryurik", "Klavdiy", "Bova")

        private val secondNames = arrayOf("Negodyaev","Prikolskiy","Motorov","Zhaba","Netudyhata",
         "Ubeysobakin","Zabodaev","Novorusskiy","Vulf","Zmiev", "Komar","Zhuk","Bishop","Koks","Skinner","Buker","Rich",
         "Griffin","Gudmen","Griffin"
        )

        fun generateName(exclude: List<String>): String {
            var name = ""
            do {
                name = firstNames[Random.nextInt(firstNames.size)]
                name += " " + secondNames[Random.nextInt(secondNames.size)]
            } while(exclude.contains(name))
            return name
        }
    }
}