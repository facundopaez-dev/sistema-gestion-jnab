package com.ebcf.jnab.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.model.SymposiumModel
import java.time.LocalDateTime


class SymposiumsListViewModel : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _symposiums = MutableLiveData<List<SymposiumModel>>().apply {
        value = listOf(
            SymposiumModel(
                title = "Simposio de Antropología Biológica 2025",
                description = "Un simposio sobre avances en la investigación de la antropología biológica.",
                dateTime = LocalDateTime.of(2025, 5, 1, 9, 0)
            ),
            SymposiumModel(
                title = "Simposio sobre Evolución Humana",
                description = "Simposio que cubre temas relacionados con la evolución humana.",
                dateTime = LocalDateTime.of(2024, 11, 15, 10, 0)
            ),
            SymposiumModel(
                title = "Simposio de Biodiversidad Humana",
                description = "Un análisis sobre la biodiversidad en los seres humanos.",
                dateTime = LocalDateTime.of(2025, 7, 20, 14, 0)
            )
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    val symposiums: LiveData<List<SymposiumModel>> = _symposiums
}

