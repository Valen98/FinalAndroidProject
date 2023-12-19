package com.example.finalproject

import java.io.File


class UserDataCompanion() {
    companion object {
        var userId: String = ""
        var username: String = ""
        var password: String = ""
        var email: String = ""
        var image: File? = null
        var isSignedIn: Boolean = false
        var userNotExist: Boolean = false
        var followers: Int = 0
        var following: Int = 0
    }
}

