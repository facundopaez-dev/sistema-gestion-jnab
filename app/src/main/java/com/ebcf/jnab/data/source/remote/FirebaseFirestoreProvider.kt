package com.ebcf.jnab.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseFirestoreProvider {
    fun provide(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}