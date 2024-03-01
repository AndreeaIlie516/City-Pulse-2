package com.andreeailie.citypulse.feature_event.domain.repository

import com.andreeailie.citypulse.feature_event.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface LocalEventRepository {

    fun getEvents(): Flow<List<Event>>

    suspend fun getEventById(id: Int): Event?

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)

    suspend fun deleteAll()

    suspend fun clearAndCacheEvents(eventsFlow: Flow<List<Event>>)

    suspend fun getEventsWithPendingActions(): List<Event>
}