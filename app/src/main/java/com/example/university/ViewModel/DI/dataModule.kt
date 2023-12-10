package com.example.university.ViewModel.DI

import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.Model.WordsDB.WordsDbManager
import com.example.university.ViewModel.AddViewModel
import com.example.university.ViewModel.FutureTestsViewModel
import com.example.university.ViewModel.LoginViewModel
import com.example.university.ViewModel.MainActivityViewModel
import com.example.university.ViewModel.MainViewModel
import com.example.university.ViewModel.PickQuantityViewModel
import com.example.university.ViewModel.PickWordViewModel
import com.example.university.ViewModel.RegistrationViewModel
import com.example.university.ViewModel.SettingsViewModel
import com.example.university.ViewModel.TestViewModel
import com.example.university.ViewModel.UserWordsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    data()
    presentation()
}

private fun Module.data() {
    single<AppDbManager> {
        AppDbManager(context = get())
    }
    single<MySharedPreferences> {
        MySharedPreferences(context = get())
    }
    single<WordsDbManager> {
        WordsDbManager(context = get())
    }
}

private fun Module.presentation() {
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
    viewModel<AddViewModel> {
        AddViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<LoginViewModel> {
        LoginViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<RegistrationViewModel> {
        RegistrationViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<PickQuantityViewModel> {
        PickQuantityViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<TestViewModel> {
        TestViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<PickWordViewModel> {
        PickWordViewModel(
            adb = get(),
            wdb = get(),
            msp = get(),
        )
    }
    viewModel<FutureTestsViewModel> {
        FutureTestsViewModel(
            db = get(),
            msp = get()
        )
    }
    viewModel<UserWordsViewModel> {
        UserWordsViewModel(
            db = get(),
            msp = get()
        )
    }
}