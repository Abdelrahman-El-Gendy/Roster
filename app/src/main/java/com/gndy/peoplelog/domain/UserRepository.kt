package com.gndy.peoplelog.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun insertUser(user: User)
}
