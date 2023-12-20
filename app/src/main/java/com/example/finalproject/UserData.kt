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
        var followerscount: Int = 0
        var followingcount: Int = 0
        var postcount: Int = 0
    }
}

