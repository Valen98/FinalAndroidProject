package com.example.finalproject

sealed class UserAction {
    object UploadNewUser: UserAction()
    object LoginUser: UserAction()

}