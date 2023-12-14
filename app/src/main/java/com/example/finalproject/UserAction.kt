package com.example.finalproject

sealed class UserAction {

    object LoginUser: UserAction()

    object CreateAccount: UserAction()

    object Logout: UserAction()

}