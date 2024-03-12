package com.andreeailie.event_domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val idLocal: Int,
    @SerializedName("ID")
    val id: Int,
    val time: String,
    val band: String,
    val location: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("is_private")
    val isPrivate: Boolean,
    @SerializedName("is_favourite")
    var isFavourite: Boolean,
    var action: String?
)

data class EventServer(
    val time: String,
    val band: String,
    val location: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("is_private")
    val isPrivate: Boolean?,
    @SerializedName("is_favourite")
    val isFavourite: Boolean?
)

class InvalidEventException(message: String) : Exception(message)