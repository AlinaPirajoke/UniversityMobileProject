package com.example.university.UsefullStuff

import androidx.compose.ui.res.stringArrayResource

class Word(
    val id: Int,
    var word: String,
    var transcription: String,
    val translations: List<String>,
    var lvl: Int,
    var result: Int = 0
) {
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