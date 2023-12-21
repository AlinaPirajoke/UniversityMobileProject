package com.example.university.usefullStuff

class Word(
    val id: Int,
    var word: String,
    var transcription: String,
    val translations: List<String>,
    var lvl: Int,
) {
    var comming: String = ""

    var result: Int = 0
        set(value) {
            field = value
            when (value) {
                0 -> lvl = (lvl * 0.8).toInt()
                1 -> lvl = lvl * 2 + 1
            }
        }

    init {
        if (transcription.isNotBlank()) {
            transcription = transcription.removeSuffix("]")
            transcription = transcription.removePrefix("[")
            transcription = "[ $transcription ]"
        }
    }

    fun translationsToString(): String{
        /*var string = ""
        val iter = translations.listIterator()
        while (iter.hasNext()) {
            string += iter
            if (iter.hasNext())
                string += ", "
        }
        return string*/
        return translations.joinToString()
    }
}