package com.gndy.peoplelog.domain

import javax.inject.Inject

class InsertUserUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(user: User) = repository.insertUser(user)
}
