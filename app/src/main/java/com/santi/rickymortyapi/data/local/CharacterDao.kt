package com.santi.rickymortyapi.data.local

import androidx.room.*
import com.santi.rickymortyapi.model.local.CharacterDb
import com.santi.rickymortyapi.model.local.LocationAndCharacter
import com.santi.rickymortyapi.model.local.LocationDb

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characterDb: CharacterDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationDb: LocationDb)

    @Transaction
    @Query("SELECT * FROM characters")
    suspend fun getAllOriginAndCosa(): List<LocationAndCharacter>
}