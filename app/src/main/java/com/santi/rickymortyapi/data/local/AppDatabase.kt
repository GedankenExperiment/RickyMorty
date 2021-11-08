package com.santi.rickymortyapi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.santi.rickymortyapi.model.local.CharacterDb
import com.santi.rickymortyapi.model.local.LocationDb

@Database(entities = [CharacterDb::class, LocationDb::class],
    version = 7, exportSchema = true)
abstract class AppDatabase : RoomDatabase(){
    abstract fun characterDao(): CharacterDao
    companion object {
        @Volatile private var instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }
        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "characters.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}