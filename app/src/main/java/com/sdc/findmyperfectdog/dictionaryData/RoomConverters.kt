package com.sdc.findmyperfectdog.dictionaryData

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // Map<String, Int> 변환기
    @TypeConverter
    fun fromTraitsMap(value: Map<String, Int>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTraitsMap(value: String): Map<String, Int> {
        val type = object : TypeToken<Map<String, Int>>() {}.type
        return gson.fromJson(value, type)
    }

    // InterestingThings 변환기
    @TypeConverter
    fun fromInterestingThings(value: InterestingThings?): String {
        return gson.toJson(value ?: InterestingThings("", "", ""))
    }


    @TypeConverter
    fun toInterestingThings(value: String): InterestingThings? {
        return gson.fromJson(value, InterestingThings::class.java)
    }
}
