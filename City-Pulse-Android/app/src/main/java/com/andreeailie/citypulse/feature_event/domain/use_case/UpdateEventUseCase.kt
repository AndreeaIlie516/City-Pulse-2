package com.andreeailie.citypulse.feature_event.domain.use_case

import android.util.Log
import com.andreeailie.citypulse.feature_event.data.network.NetworkStatusTracker
import com.andreeailie.citypulse.feature_event.domain.model.Event
import com.andreeailie.citypulse.feature_event.domain.model.InvalidEventException
import com.andreeailie.citypulse.feature_event.domain.repository.LocalEventRepository
import com.andreeailie.citypulse.feature_event.domain.repository.RemoteEventRepository

class UpdateEventUseCase(
    private val localRepository: LocalEventRepository,
    private val remoteRepository: RemoteEventRepository,
    private val networkStatusTracker: NetworkStatusTracker
) {

    @Throws(InvalidEventException::class)
    suspend operator fun invoke(event: Event) {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        if (event.time.isBlank()) {
            throw InvalidEventException("The time of the event can't be empty.")
        }
        if (event.band.isBlank()) {
            throw InvalidEventException("The band of the event can't be empty.")
        }
        if (event.location.isBlank()) {
            throw InvalidEventException("The location of the event can't be empty.")
        }
        try {
            if (isNetworkAvailable) {
                val newEvent = remoteRepository.updateEvent(event)
                Log.d("UpdateEventUseCase", "newEvent: $newEvent")
                localRepository.insertEvent(event.copy(ID = event.ID, idLocal = event.idLocal, action = null))
            } else {
                Log.d("UpdateEventUseCase", "Update on the local database, no internet")
                localRepository.insertEvent(event.copy(ID = event.ID, idLocal = event.idLocal, action = "update"))
            }
        } catch (e: Exception) {
            throw Exception("Failed to update the event. Please try again later.")
        }
    }
}