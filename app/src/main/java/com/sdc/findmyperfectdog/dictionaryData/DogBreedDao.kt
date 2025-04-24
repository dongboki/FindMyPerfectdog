package com.sdc.findmyperfectdog.dictionaryData

import androidx.room.*

@Dao
interface DogBreedDao {

    @Query("SELECT * FROM dog_breeds")
    suspend fun getAll(): List<DictionaryDogBreed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<DictionaryDogBreed>)

    @Query("SELECT * FROM dog_breeds WHERE name = :name")
    suspend fun getByName(name: String): DictionaryDogBreed?
}