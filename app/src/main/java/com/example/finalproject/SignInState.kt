package com.example.finalproject

data class SignInState(
    var isSignedInSuccessful: Boolean = false,
    var signInError: String? = null,
    val data: UserData? = null,
)
