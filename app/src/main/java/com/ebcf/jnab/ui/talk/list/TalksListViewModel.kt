package com.ebcf.jnab.ui.talk.list

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.ebcf.jnab.data.model.FavouriteEntity
import com.ebcf.jnab.data.repository.FavouriteRepository
import com.ebcf.jnab.data.repository.TalkRepository
import com.ebcf.jnab.data.source.local.AppDatabase
import com.ebcf.jnab.domain.model.TalkModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

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

    private val _groupedFavorites = MutableLiveData<List<GroupedTalks>>()
    val groupedFavorites = MediatorLiveData<List<GroupedTalks>>()

    init {
        loadTalks()
        loadFavourites()

        displayTalks.addSource(_filteredTalks) {
            displayTalks.value = Pair(it, _favoriteIds.value ?: emptySet())
            updateGroupedFavoritesIfReady()
        }

        displayTalks.addSource(_favoriteIds) {
            displayTalks.value = Pair(_filteredTalks.value ?: emptyList(), it)
            updateGroupedFavoritesIfReady()
        }

    }

    private fun loadFavourites() {
        viewModelScope.launch {
            val favorites = favouriteRepository.getAllFavourites()
            _favoriteIds.value = favorites.map { it.talkId }.toSet()
            updateGroupedFavoritesIfReady() // <- Ahora sí: se ejecuta cuando ya tenés los datos
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTalks() {
        allTalks = repository.getAll(2)
        _talks.value = allTalks
        _filteredTalks.value = allTalks
        updateGroupedFavoritesIfReady()
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
        val allTalks = _talks.value ?: emptyList()
        val filtered = allTalks.filter { talk ->
            (symposiumId == null || talk.symposiumId == symposiumId) &&
                    (speakerId == null || talk.speakerId == speakerId) &&
                    (date == null || talk.date.isEqual(date))
        }
        _filteredTalks.value = filtered
    }

    fun clearFilters() {
        _filteredTalks.value = _talks.value
    }

    private fun updateGroupedFavoritesIfReady() {
        val talks = _talks.value
        val favorites = _favoriteIds.value
        if (talks != null && favorites != null) {
            val favoriteTalks = talks.filter { favorites.contains(it.id) }

            val grouped = favoriteTalks
                .groupBy { it.date }
                .map { (date, talks) ->
                    GroupedTalks(date, talks.sortedBy { it.startTime })
                }.sortedBy { it.date }

            groupedFavorites.postValue(grouped)
        }
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


data class GroupedTalks(
    val date: LocalDate,
    val talks: List<TalkModel>
)

