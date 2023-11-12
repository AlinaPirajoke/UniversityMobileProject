package com.example.university.ViewModel.DI

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.university.Model.DBManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.MainViewModel
import com.example.university.ViewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    data()
    presentation()
}

private fun Module.data(){
    single<DBManager>{
        DBManager(context = get())
    }
    /*single<SharedPreferences>{
        PreferenceManager.getDefaultSharedPreferences(get())
    }*/
    single<MySharedPreferences>{
        MySharedPreferences(context = get())
    }
}

private fun Module.presentation(){
    viewModel<SettingsViewModel> {
        SettingsViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<MainViewModel> {
        MainViewModel(
            db = get(),
            msp = get()
        )
    }
}