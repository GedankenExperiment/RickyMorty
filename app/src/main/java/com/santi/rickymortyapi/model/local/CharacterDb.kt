package com.santi.rickymortyapi.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterDb(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val url: String?,
    val species: String?,
    val status: String?)
