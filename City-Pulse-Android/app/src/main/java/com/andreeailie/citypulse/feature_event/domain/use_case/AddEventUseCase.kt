package com.andreeailie.citypulse.feature_event.domain.use_case

import android.util.Log
import com.andreeailie.citypulse.feature_event.data.network.NetworkStatusTracker
import com.andreeailie.citypulse.feature_event.domain.model.Event
import com.andreeailie.citypulse.feature_event.domain.model.InvalidEventException
import com.andreeailie.citypulse.feature_event.domain.repository.LocalEventRepository
import com.andreeailie.citypulse.feature_event.domain.repository.RemoteEventRepository

class AddEventUseCase(
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
                val newEvent = remoteRepository.insertEvent(event)
                Log.d("AddEventUseCase", "newEvent: $newEvent")
                localRepository.insertEvent(event.copy(ID = newEvent.ID, action = null))
            } else {
                Log.d("AddEventUseCase", "Add on the local database, no internet")
                localRepository.insertEvent(event.copy(action = "add"))
            }
        } catch (e: Exception) {
            throw Exception("Failed to add the event. Please try again later.")
        }
    }
}