package com.example.university.View.Main

sealed class MainScreens(val route: String) {
    object Main : MainScreens("main_screen")
    object AddNew : MainScreens("add_screen")
    object Settings : MainScreens("settings_screen")
    object PickQuantity : MainScreens("quantity_screen")
}