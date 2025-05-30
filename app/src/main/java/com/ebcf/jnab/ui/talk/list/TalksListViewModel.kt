package com.ebcf.jnab.ui.talk.list

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ebcf.jnab.data.model.FavouriteEntity
import com.ebcf.jnab.data.repository.FavouriteRepository
import com.ebcf.jnab.data.repository.TalkRepository
import com.ebcf.jnab.data.source.local.AppDatabase
import com.ebcf.jnab.domain.model.TalkModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class TalksListViewModel(private val context: Context) : ViewModel() {


    private val repository = TalkRepository()
    private var allTalks: List<TalkModel> = listOf()

    private val _talks = MutableLiveData<List<TalkModel>>()
    val talks: LiveData<List<TalkModel>> get() = _talks

    private val favouriteRepository = FavouriteRepository(AppDatabase.getInstance(context).favouriteDao())
    private val _favoriteIds = MutableLiveData<Set<Int>>()

    val favoriteIds: LiveData<Set<Int>> get() = _favoriteIds

    private val _filteredTalks = MutableLiveData<List<TalkModel>>()
    val filteredTalks: LiveData<List<TalkModel>> = _filteredTalks


    val displayTalks = MediatorLiveData<Pair<List<TalkModel>, Set<Int>>>()


    init {
        loadTalks()
        loadFavourites()

        displayTalks.addSource(_filteredTalks) { talks ->
            displayTalks.value = Pair(talks, _favoriteIds.value ?: emptySet())
        }

        displayTalks.addSource(_favoriteIds) { favorites ->
            displayTalks.value = Pair(_filteredTalks.value ?: emptyList(), favorites)
        }

    }

    private fun loadFavourites() {
        viewModelScope.launch {
            val favorites = favouriteRepository.getAllFavourites()
            _favoriteIds.value = favorites.map { it.talkId }.toSet()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTalks() {
        allTalks = repository.getAll(2)
        _talks.value = allTalks
        _filteredTalks.value = allTalks // Para mostrar todos al inicio
    }

    fun toggleFavorite(talkId: Int) {
        viewModelScope.launch {
            try {
                val favorites = favouriteRepository.getAllFavourites()
                val isFavorite = favorites.any { it.talkId == talkId }

                if (isFavorite) {
                    favouriteRepository.removeFromFavourites(FavouriteEntity(talkId))
                } else {
                    favouriteRepository.addToFavourites(FavouriteEntity(talkId))
                }

                val updatedFavorites = favouriteRepository.getAllFavourites()
                _favoriteIds.postValue(updatedFavorites.map { it.talkId }.toSet())
            } catch (e: Exception) {
                Log.e("TalksListViewModel", "Error toggleFavorite: ${e.message}", e)
            }
        }
    }


    fun applyFilters(symposiumId: Int?, speakerId: Int?, date: LocalDate?) {
        Log.d("TalksListViewModel", "Applying filters: SymposiumId=$symposiumId, SpeakerId=$speakerId, Date=$date")
        val allTalks = _talks.value ?: emptyList()

        val filtered = allTalks.filter { talk ->
            (symposiumId == null || talk.symposiumId == symposiumId) &&
                    (speakerId == null || talk.speakerId == speakerId) &&
                    (date == null || talk.date.isEqual(date))
        }

        _filteredTalks.value = filtered
        Log.d("adsf","$filtered")

    }

    fun clearFilters() {
        _filteredTalks.value = _talks.value
    }


    class TalksListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TalksListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TalksListViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }




}