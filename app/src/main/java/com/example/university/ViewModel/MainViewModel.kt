package com.example.university.ViewModel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.DBManager
import com.example.university.usefull_stuff.getTodayDate
import kotlinx.coroutines.launch

class MainViewModel(val db: DBManager, val sharedPreferences: SharedPreferences): ViewModel(){
    var user = sharedPreferences.getInt("user", 1)

    var isGoingToLogin = MutableLiveData<Boolean>() // отправляет пользователя на логин

    var todayTest = MutableLiveData<Int>() // количество слов для теста на сегодня
    var todayLearn = MutableLiveData<Int>() // оставшееся количество слов для изучения на сегодня

    var statLearned = MutableLiveData<Int>() // количество изученных слов
    var statLearning = MutableLiveData<Int>() // количество изучающихся слов
    var statAverage = MutableLiveData<Int>() // Среднее количество изучаемых слов в день

    init {
        var session = sharedPreferences.getBoolean("session", false)
        val needPass = sharedPreferences.getBoolean("needPassword", false)

        if (!needPass)
            session = true
        if (!session)
            toLogin()


    }

    fun toLogin(){
        isGoingToLogin.value = true
    }

    fun setSLearned(count: Int){
        statLearned.value = count
    }

    fun setSLearning(count: Int){
        statLearning.value = count
    }

    fun setSAverage(count: Int){
        statAverage.value = count
    }

    fun getStatistic(){
        viewModelScope.launch {
            setSLearned(db.getLearnedCount())
            setSLearning(db.getLerningCount())
            setSAverage(db.getAverage())
        }
    }

    fun setTest(count: Int){
        todayTest.value = count
    }

    fun setLearn(count: Int){
        todayLearn.value = count
    }

    fun checkTodayWords(){
        viewModelScope.launch {
            setTest(db.getSizeFromDate(getTodayDate(), user)!!)
            var learnCount = db.getTodayLearnedCount(getTodayDate(), user)!! - sharedPreferences.getInt("studiedPerDay", 10)
            if(learnCount < 0)
                learnCount = 0
            setLearn(learnCount)
        }
    }
}