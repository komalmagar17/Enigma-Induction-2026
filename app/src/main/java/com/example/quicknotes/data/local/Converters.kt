package com.example.quicknotes.data.local

import androidx.room.TypeConverter
import com.example.quicknotes.domain.model.ChecklistItem
import com.example.quicknotes.domain.model.ImageAttachment
import com.example.quicknotes.domain.model.VoiceRecording
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromChecklistItems(value: List<ChecklistItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toChecklistItems(value: String): List<ChecklistItem> {
        val listType = object : TypeToken<List<ChecklistItem>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromVoiceRecordings(value: List<VoiceRecording>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toVoiceRecordings(value: String): List<VoiceRecording> {
        val listType = object : TypeToken<List<VoiceRecording>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromImageAttachments(value: List<ImageAttachment>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toImageAttachments(value: String): List<ImageAttachment> {
        val listType = object : TypeToken<List<ImageAttachment>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}
