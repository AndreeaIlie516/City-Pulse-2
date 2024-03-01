package com.andreeailie.citypulse.feature_event.data.repository

import android.util.Log
import com.andreeailie.citypulse.feature_event.data.data_source.remote.EventApi
import com.andreeailie.citypulse.feature_event.domain.model.Event
import com.andreeailie.citypulse.feature_event.domain.model.EventServer
import com.andreeailie.citypulse.feature_event.domain.repository.RemoteEventRepository

class RemoteEventRepositoryImpl(
    private val eventApi: EventApi,
) : RemoteEventRepository {
    override suspend fun getEvents(): List<Event> {
        return try {
            Log.d("RemoteEventRepositoryImpl", "getEvents called")
            val response = eventApi.getEvents()
            Log.d("RemoteEventRepositoryImpl", "response: $response")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("RemoteEventRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RemoteEventRepositoryImpl", "Exception in getEvents: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getEventById(id: Int): Event? {
        return try {
            Log.d("RemoteEventRepositoryImpl", "getEventById called")
            val response =eventApi.getEventById(id)
            Log.d("RemoteEventRepositoryImpl", "response: $response")
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RemoteEventRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RemoteEventRepositoryImpl", "Exception in getEventById: ${e.message}")
            null
        }
    }

    override suspend fun insertEvent(event: Event): Event {
        try {
            val eventForServer = EventServer(
                time = event.time,
                band = event.band,
                location = event.location,
                image_url = event.image_url,
                is_private = event.is_private,
                is_favourite = event.is_favourite
            )
            Log.d("RemoteEventRepositoryImpl", "insertEvent called")
            val response = eventApi.createEvent(eventForServer)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Failed to retrieve created event")
            } else {
                throw Exception("Failed to create event: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("RemoteEventRepository", "Error inserting event: ${e.message}")
            throw Exception("Server error occurred while creating the event.")
        }
    }

    override suspend fun deleteEvent(event: Event) {
        Log.d("RemoteEventRepositoryImpl", "deleteEvent called")
        try {
            Log.d("RemoteEventRepositoryImpl", "Id: ${event.ID}")
            eventApi.deleteEvent(event.ID)
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Error deleting entity: ${e.message}")
            throw Exception("Server error occurred while deleting the entity.")
        }
    }

    override suspend fun updateEvent(event: Event) {
        try {
            val eventForServer = EventServer(
                time = event.time,
                band = event.band,
                location = event.location,
                image_url = event.image_url,
                is_private = event.is_private,
                is_favourite = event.is_favourite
            )
            Log.d("RemoteEventRepositoryImpl", "updateEvent called")
            eventApi.updateEvent(event.ID, eventForServer)
        } catch (e: Exception) {
            Log.e("RemoteEventRepository", "Error updating event: ${e.message}")
            throw Exception("Server error occurred while updating the event.")
        }
    }

    override suspend fun addEventToFavorites(event: Event) {
        eventApi.addToFavourites(event.ID)
    }

    override suspend fun deleteEventFromFavorites(event: Event) {
        eventApi.deleteFromFavourites(event.ID)
    }
}