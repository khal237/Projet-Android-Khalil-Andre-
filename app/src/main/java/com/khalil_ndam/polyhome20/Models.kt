package com.khalil_ndam.polyhome20.Models

data class AuthRequest(val login: String, val password: String)
data class AuthResponse(val token: String)