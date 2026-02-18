package com.example.polyhome

data class Device(
    val id: String,
    val type: String,
    val availableCommands: List<String>,
    val opening: Int? = null, // Pour les volets et garages
    val power: Int? = null    // Pour les lumières
)


data class DevicesResponse(
    val devices: List<Device>
)