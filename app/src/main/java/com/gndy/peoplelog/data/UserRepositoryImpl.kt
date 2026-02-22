package com.gndy.peoplelog.data

import com.gndy.peoplelog.domain.User
import com.gndy.peoplelog.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> =
        dao.getAllUsers().map { entities -> entities.map { it.toDomain() } }

    override suspend fun insertUser(user: User) {
        dao.insertUser(user.toEntity())
    }
}
