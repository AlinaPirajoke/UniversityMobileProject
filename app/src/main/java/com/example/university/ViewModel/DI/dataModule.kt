package com.example.university.ViewModel.DI

import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.MainActivityViewModel
import com.example.university.ViewModel.AddViewModel
import com.example.university.ViewModel.LoginViewModel
import com.example.university.ViewModel.MainViewModel
import com.example.university.ViewModel.PickQuantityViewModel
import com.example.university.ViewModel.RegistrationViewModel
import com.example.university.ViewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    data()
    presentation()
}

private fun Module.data(){
    single<AppDbManager>{
        AppDbManager(context = get())
    }
    single<MySharedPreferences>{
        MySharedPreferences(context = get())
    }
}

private fun Module.presentation(){
    viewModel<MainActivityViewModel> {
        MainActivityViewModel(
            db = get(),
            msp = get()
        )
    }
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
    viewModel<AddViewModel>{
        AddViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<LoginViewModel>{
        LoginViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<RegistrationViewModel>{
        RegistrationViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<PickQuantityViewModel>{
        PickQuantityViewModel(
            db = get(),
            msp = get()
        )
    }
}