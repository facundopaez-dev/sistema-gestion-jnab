package com.ebcf.jnab.data.repository

import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource
import com.ebcf.jnab.domain.model.UserRole
import com.ebcf.jnab.domain.repository.SignupRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SignupRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuthRemoteDataSource.getInstance().auth,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : SignupRepository {

    override suspend fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit> = try {

        val authResult = auth
            .createUserWithEmailAndPassword(email, password)
            .await()

        val uid = authResult.user?.uid
            ?: return Result.failure(Exception("UID nulo"))

        val userData = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "role" to UserRole.ASSISTANT.value
        )

        firestore
            .collection("users")
            .document(uid)
            .set(userData)
            .await()

        auth.currentUser?.sendEmailVerification()?.await()

        Result.success(Unit)

    } catch (e: Exception) {
        Result.failure(e)
    }
}