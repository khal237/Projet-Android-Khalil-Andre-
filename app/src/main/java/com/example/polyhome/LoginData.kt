package com.example.polyhome

// Pour l'inscription et la connexion (Envoi)
data class LoginData(
    val login: String,
    val password: String
)

data class LoginResponse(
    val token: String
)
