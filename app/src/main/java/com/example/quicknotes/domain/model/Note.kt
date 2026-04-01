package com.example.quicknotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val isChecklist: Boolean = false,
    val checkItems: List<ChecklistItem> = emptyList(),
    val colorIdx: Int = 0,
    val pinned: Boolean = false,
    val recordings: List<VoiceRecording> = emptyList(),
    val images: List<ImageAttachment> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long
)

data class ChecklistItem(
    val id: String,
    val text: String,
    val checked: Boolean
)

data class VoiceRecording(
    val id: String,
    val url: String,
    val duration: Int,
    val createdAt: Long
)

data class ImageAttachment(
    val id: String,
    val data: String, // Base64 or URI
    val name: String
)
