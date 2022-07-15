package com.bravo.dto
import kotlinx.serialization.Serializable

@Serializable
data class SaveNewsRequest(
    val channelName: String,
    val messageId: String,
    val messageContent: String?,
    val date: String
)
