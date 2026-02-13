package com.example.polyhome

// Pour l'inscription et la connexion (Envoi)
data class LoginData(
    val login: String,
    val password: String
)

// Pour récupérer le token après la connexion (Réception)
data class LoginResponse(
    val token: String
)
