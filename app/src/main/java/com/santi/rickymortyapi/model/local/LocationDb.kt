package com.santi.rickymortyapi.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationDb(
    var locationOwnerId: Int,
    var created: String?,
    var dimension: String?,
    var type: String?,
    var name: String?,
    val url: String?
){
    @PrimaryKey(autoGenerate = true)
    var locationId: Int = 0
}
