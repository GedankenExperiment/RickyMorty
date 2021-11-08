package com.santi.rickymortyapi.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.santi.rickymortyapi.data.local.CharacterDao
import com.santi.rickymortyapi.model.local.CharacterDb
import com.santi.rickymortyapi.network.RetrofitService
import com.santi.rickymortyapi.model.FullCharacter
import com.santi.rickymortyapi.model.FullLocation
import com.santi.rickymortyapi.model.local.LocationAndCharacter
import com.santi.rickymortyapi.model.local.LocationDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val retrofitService: RetrofitService,
private val characterDao: CharacterDao
) {
    fun getCharacter() : Flow<PagingData<FullCharacter>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 15),
            pagingSourceFactory = {
                CharacterPagingSource(retrofitService)
            }
        ).flow
    }
    suspend fun getLocation(id: String) : FullLocation {
        return CharacterPagingSource(retrofitService).getLocation(id)
    }

    suspend fun insertFullCharacter(fullCharacter: FullCharacter, fullLocation: FullLocation){
        characterDao.insert(convertCharacterFullToDb(fullCharacter))
        if(fullCharacter.id != null){
            characterDao.insert(convertLocationFullToDb(fullCharacter.id!!, fullLocation))
        }

    }
    suspend fun getFavorites(): List<LocationAndCharacter>{
        return characterDao.getAllOriginAndCosa()
    }

    fun convertCharacterFullToDb(fullCharacter: FullCharacter): CharacterDb{
        return CharacterDb(id = fullCharacter.id?:0,
            name = fullCharacter.name,
            url = fullCharacter.image,
            species = fullCharacter.species,
            status = fullCharacter.status
        )
    }
    private fun convertLocationFullToDb(foreignKey: Int, fullLocation: FullLocation):LocationDb{
        return LocationDb(
            locationOwnerId = foreignKey,
            created = fullLocation.created,
            dimension = fullLocation.dimension,
            type = fullLocation.type,
            name = fullLocation.name,
            url = fullLocation.url)
    }
}