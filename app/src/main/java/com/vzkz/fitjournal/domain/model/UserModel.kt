package com.vzkz.fitjournal.domain.model

data class UserModel(
    val uid: String,
    val nickname: String,
    val email: String?,
    val firstname: String,
    val lastname: String,
    val weight: String? = null,
    val age: String? = null,
    val gender: String? = null,
    val goal: String? = null
)