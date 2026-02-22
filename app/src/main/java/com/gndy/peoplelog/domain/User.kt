package com.gndy.peoplelog.domain

data class User(
    val id: Long = 0,
    val fullName: String,
    val age: Int,
    val jobTitle: String,
    val gender: Gender,
    val createdAt: Long = System.currentTimeMillis()
)
