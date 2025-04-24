package com.sdc.findmyperfectdog.dictionaryData

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun loadDogBreedsFromJson(context: Context): List<DictionaryDogBreed> {
    val jsonString = context.assets.open("dog_breeds.json")
        .bufferedReader()
        .use { it.readText() }

    val gson = Gson()
    val listType = object : TypeToken<List<DictionaryDogBreed>>() {}.type
    return gson.fromJson(jsonString, listType)
}
