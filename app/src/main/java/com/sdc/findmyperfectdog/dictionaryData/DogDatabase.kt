package com.sdc.findmyperfectdog.dictionaryData


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DictionaryDogBreed::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogBreedDao(): DogBreedDao

    companion object {
        @Volatile
        private var INSTANCE: DogDatabase? = null

        fun getDatabase(context: Context): DogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DogDatabase::class.java,
                    "dog_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}