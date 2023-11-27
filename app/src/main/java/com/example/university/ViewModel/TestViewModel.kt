package com.example.university.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.UsefullStuff.Word
import com.example.university.ViewModel.States.TestUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    private val TAG = "TestViewModel"
    private var listId: Int = -1
    private var wordList = ArrayList<Word>()
    private lateinit var currentWord: Word
    private var iterator = wordList.iterator()

    private val _uiState = MutableStateFlow(
        TestUiState(
            colorScheme = msp.getColorScheme(),
        )
    )
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    fun setListId(id: Int){
        listId = id
        viewModelScope.launch {
            testStart()
        }
    }

    suspend private fun testStart(){
        wordList = db.getWordsFromList(listId)
        iterator = wordList.iterator()
        nextWord()
    }

    private fun testFinish(){

    }

    private fun nextWord(){
        if(!iterator.hasNext())
            testFinish()

        val word = iterator.next()
        cleanAllLabels()
        toFirstStage()
    }

    fun toFirstStage(){
        setCurrentStage(1)
        showWordLabel()

    }

    // Отсылка к моему прошлому проекту (Не кривой нейминг)
    fun showKana(){
        showTranscrLabel()
    }

    fun toSecondStage(){
        setCurrentStage(2)
        showAllLabels()
    }

    // -_- в этих методах различается ровно одна цифра, но логически, это разные случаи
    // Может быть я говнокодер?
    fun goodResultProcessing(){
        currentWord.result = 1
        viewModelScope.launch {
            saveWordResult()
        }
        nextWord()
    }

    fun badResultProcessing(){
        currentWord.result = 0
        viewModelScope.launch {
            saveWordResult()
        }
        nextWord()
    }

    private fun setCurrentStage(stage: Int){
        _uiState.update { state ->
            state.copy(
                currentStage = stage
            )
        }
    }

    private fun cleanAllLabels(){
        _uiState.update { state ->
            state.copy(
                wordLabel = "",
                transcrLabel = "",
                translLabel = ""
            )
        }
    }

    private fun showWordLabel(){
        _uiState.update { state ->
            state.copy(
                wordLabel = currentWord.word,
            )
        }
    }

    private fun showTranscrLabel(){
        _uiState.update { state ->
            state.copy(
                transcrLabel = currentWord.transcription,
            )
        }
    }

    private fun showTranslLabel(){
        _uiState.update { state ->
            state.copy(
                translLabel = currentWord.translationsToString(),
            )
        }
    }

    private fun showAllLabels(){
        _uiState.update { state ->
            state.copy(
                wordLabel = currentWord.word,
                transcrLabel = currentWord.transcription,
                translLabel = currentWord.translationsToString(),
            )
        }
    }

    suspend fun saveWordResult(word: Word = currentWord){
        when(word.result){
            0 -> word.lvl = (currentWord.lvl * 0.8).toInt()
            1 -> word.lvl *= 2
        }

        // TODO(Сохранение результата в бд)
    }
}