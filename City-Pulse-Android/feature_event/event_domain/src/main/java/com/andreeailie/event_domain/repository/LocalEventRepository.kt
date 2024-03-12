package com.andreeailie.event_domain.repository

import com.andreeailie.event_domain.model.Event
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