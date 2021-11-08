package com.santi.rickymortyapi.model.local

import androidx.room.Embedded
import androidx.room.Relation

data class LocationAndCharacter(
    @Embedded val characterDb: CharacterDb,
    @Relation(
        parentColumn = "id",
        entityColumn = "locationOwnerId"
    )
    val locationDb: LocationDb
)
