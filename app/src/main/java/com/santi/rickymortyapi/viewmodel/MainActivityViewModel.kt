package com.santi.rickymortyapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.santi.rickymortyapi.data.MainRepository
import com.santi.rickymortyapi.model.FullCharacter
import com.santi.rickymortyapi.model.FullLocation
import com.santi.rickymortyapi.model.local.LocationAndCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    // MASTER
    fun getListData(): Flow<PagingData<FullCharacter>> {
        return mainRepository.getCharacter().cachedIn(viewModelScope)
    }

    // DETAIL
    val selectedFullCharacter: MutableLiveData<FullCharacter> = MutableLiveData()
    val location: MutableLiveData<FullLocation> = MutableLiveData()
    fun getLocation(locationUrl: String) {
        viewModelScope.launch {
            location.value = mainRepository.getLocation(locationUrl)

        }
    }

    fun insertFullCharacter() {
        if (selectedFullCharacter.value != null && location.value != null && canLike) {
            viewModelScope.launch {
                mainRepository.insertFullCharacter(
                    selectedFullCharacter.value!!,
                    location.value!!
                )
            }
        } else {
            // TODO no se pudo insertar
        }
    }

    // FAVORITE
    val favorites = MutableLiveData<List<LocationAndCharacter>>()
    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            favorites.postValue(mainRepository.getFavorites())

        }
    }
    var canLike : Boolean = true
    fun fromDBToSelected(locationAndCharacter: LocationAndCharacter) {
        canLike = false
        selectedFullCharacter.value?.id = locationAndCharacter.characterDb.id
        selectedFullCharacter.value?.name = locationAndCharacter.characterDb.name
        selectedFullCharacter.value?.image = locationAndCharacter.characterDb.url
        selectedFullCharacter.value?.species = locationAndCharacter.characterDb.species
        selectedFullCharacter.value?.status = locationAndCharacter.characterDb.status

        location.value?.created = locationAndCharacter.locationDb.created
        location.value?.dimension = locationAndCharacter.locationDb.dimension
        location.value?.type = locationAndCharacter.locationDb.type
        location.value?.name = locationAndCharacter.locationDb.name
        location.value?.url = locationAndCharacter.locationDb.url
    }
}
