package com.example.prueba.Login

data class LoginResponse(
    val token: String,
    val username: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)