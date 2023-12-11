package com.example.university.View.Main

sealed class MainScreens(val route: String) {
    object Main : MainScreens("main_screen")
    object AddNew : MainScreens("add_screen")
    object Settings : MainScreens("settings_screen")
    object PickQuantity : MainScreens("quantity_screen")
    object Test : MainScreens("test_screen")
    object PickWord : MainScreens("new_words_screen")
    object FutureTests : MainScreens("future_tests_screen")
    object UserWords : MainScreens("user_words_screen")
    object Remember : MainScreens("remember_screen")
}