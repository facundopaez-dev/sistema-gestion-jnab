package com.ebcf.jnab.domain.repository

interface SignupRepository {

    suspend fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit>

}