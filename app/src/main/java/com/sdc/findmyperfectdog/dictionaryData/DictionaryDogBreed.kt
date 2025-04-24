package com.sdc.findmyperfectdog.dictionaryData

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "dog_breeds")
data class DictionaryDogBreed(
    @PrimaryKey val name: String,
    val origin: String?,
    val size: String?,
    val imageUrl: String?,
    val description: String?,
    val officialName: String?,
    val traits: Map<String, Int>?,
    val notice: String?,
    val description2: String?,
    val info1: String?,
    val info2: String?,
    val history: String?,
    val interestingThings: InterestingThings?
) : Parcelable

@Parcelize
data class InterestingThings(
    val things1: String?,
    val things2: String?,
    val things3: String?
) : Parcelable


