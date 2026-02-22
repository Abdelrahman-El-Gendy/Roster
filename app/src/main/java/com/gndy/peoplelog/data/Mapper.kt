package com.gndy.peoplelog.data

import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.domain.User

fun UserEntity.toDomain(): User = User(
    id = id,
    fullName = fullName,
    age = age,
    jobTitle = jobTitle,
    gender = Gender.valueOf(gender),
    createdAt = createdAt
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    fullName = fullName,
    age = age,
    jobTitle = jobTitle,
    gender = gender.name,
    createdAt = createdAt
)
