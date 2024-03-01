package com.andreeailie.citypulse.feature_event.domain.use_case

import android.util.Log
import com.andreeailie.citypulse.feature_event.data.network.NetworkStatusTracker
import com.andreeailie.citypulse.feature_event.domain.model.Event
import com.andreeailie.citypulse.feature_event.domain.repository.LocalEventRepository
import com.andreeailie.citypulse.feature_event.domain.repository.RemoteEventRepository

class DeleteEventUseCase(
    private val localRepository: LocalEventRepository,
    private val remoteRepository: RemoteEventRepository,
    private val networkStatusTracker: NetworkStatusTracker
) {

    suspend operator fun invoke(event: Event) {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        try {
            if (isNetworkAvailable) {
                remoteRepository.deleteEvent(event)
                localRepository.deleteEvent(event)
                Log.d("DeleteEventUseCase", "Deleted event: $event")
            } else {
                Log.d("DeleteEventUseCase", "Delete on the local database, no internet")
                localRepository.insertEvent(event.copy(ID = event.ID, idLocal = event.idLocal, action = "delete"))
            }
        }  catch (e: Exception) {
            throw Exception("Failed to delete the event. Please try again later.")
        }
    }
}