package com.andreeailie.event_domain.repository

import com.andreeailie.event_domain.model.Event
import kotlinx.coroutines.flow.Flow

interface RemoteEventRepository {
    suspend fun getEvents(): List<Event>

    suspend fun getEventById(id: Int): Event?

    suspend fun insertEvent(event: Event): Event

    suspend fun deleteEvent(event: Event)

    suspend fun updateEvent(event: Event)

    suspend fun addEventToFavorites(event: Event)

    suspend fun deleteEventFromFavorites(event: Event)
}