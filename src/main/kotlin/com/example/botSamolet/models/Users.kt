package com.example.botSamolet.models

data class Users(
    val id: Long? = 0,
    val firstName: String? = "",
    val isBot: Boolean? = false,
    val lastName: String? = "",
    val userName: String? = "",
    val languageCode: String? = "",
    val canJoinGroups: Boolean? = false,
    val canReadAllGroupMessages: Boolean? = false,
    val supportInlineQueries: Boolean? = false,
    val isPremium: Boolean? = false,
    val addedToAttachmentMenu: Boolean? = false
)