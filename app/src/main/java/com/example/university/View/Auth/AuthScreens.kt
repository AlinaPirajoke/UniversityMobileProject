package com.example.university.View.Auth;

sealed class AuthScreens(val route: String) {
    object Login: AuthScreens("login_screen")
    object Registration: AuthScreens("register_screen")
}
