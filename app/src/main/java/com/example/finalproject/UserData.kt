package com.example.finalproject

import java.io.File

class UserData() {
    companion object {
        var username: String = ""
        var password: String = ""
        var email: String = ""
        var image: File? = null
        var isSignedIn: Boolean = false
    }
}

