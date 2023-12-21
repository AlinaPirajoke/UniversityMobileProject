package com.example.university.view.auth;

sealed class AuthScreens(val route: String) {
    object Login : AuthScreens("login_screen")
    object Registration : AuthScreens("register_screen")
}
