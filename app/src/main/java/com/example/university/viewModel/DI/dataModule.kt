package com.example.university.viewModel.DI

import com.example.university.model.MySharedPreferences
import com.example.university.model.api.TranslateUseCase
import com.example.university.model.appDB.AppDbHelper
import com.example.university.model.appDB.AppDbManager
import com.example.university.model.wordsDB.WordsDbHelper
import com.example.university.model.wordsDB.WordsDbManager
import com.example.university.viewModel.AddViewModel
import com.example.university.viewModel.FutureTestsViewModel
import com.example.university.viewModel.LoginViewModel
import com.example.university.viewModel.MainActivityViewModel
import com.example.university.viewModel.MainViewModel
import com.example.university.viewModel.PickQuantityViewModel
import com.example.university.viewModel.PickWordViewModel
import com.example.university.viewModel.RegistrationViewModel
import com.example.university.viewModel.RememberViewModel
import com.example.university.viewModel.SettingsViewModel
import com.example.university.viewModel.TestViewModel
import com.example.university.viewModel.UserWordsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    data()
    presentation()
}

private fun Module.data() {
    single<MySharedPreferences> {
        MySharedPreferences(context = get())
    }
    single<AppDbHelper>{
        AppDbHelper(context = get())
    }
    single<WordsDbHelper> {
        WordsDbHelper(get())
    }
    single<AppDbManager> {
        AppDbManager(context = get(), msp = get(), dbHelper = get())
    }
    single<WordsDbManager> {
        WordsDbManager(context = get(), dbHelper = get())
    }
    single<TranslateUseCase>{
        TranslateUseCase(context = get())
    }
}

private fun Module.presentation() {
    viewModel<MainActivityViewModel> {
        MainActivityViewModel(
            adb = get(),
            wdb = get(),
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
            msp = get(),
            translator = get()
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
            db = get(),
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
    viewModel<RememberViewModel> {
        RememberViewModel(
            db = get(),
            msp = get()
        )
    }
}