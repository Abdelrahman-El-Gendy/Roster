package com.gndy.peoplelog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val age: Int,
    val jobTitle: String,
    val gender: String,
    val createdAt: Long
)
