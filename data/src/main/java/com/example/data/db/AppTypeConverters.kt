package com.example.data.db

import androidx.room.TypeConverter
import com.example.data.entities.SourceEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppTypeConverters {

    @TypeConverter
    fun fromSourceEntity(sourceEntity: SourceEntity?): String? {
        return Gson().toJson(sourceEntity)
    }

    @TypeConverter
    fun toSourceEntity(sourceString: String?): SourceEntity? {
        return Gson().fromJson(
            sourceString,
            object : TypeToken<SourceEntity?>() {}.type
        )
    }
}