package com.example.finalproject

import java.io.File

data class UserData(
    val userId: String?,
    val username: String?,
    ) {}

class UserDataCompanion() {
    companion object {
        var userId: String = ""
        var username: String = ""
        var password: String = ""
        var email: String = ""
        var image: File? = null
        var isSignedIn: Boolean = false
    }
}

