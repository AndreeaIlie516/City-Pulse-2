package com.andreeailie.event_domain.use_case

import android.util.Log
import com.andreeailie.event_domain.model.Event
import com.andreeailie.event_domain.repository.LocalEventRepository
import com.andreeailie.event_domain.repository.RemoteEventRepository

class ToggleFavouriteStatusUseCase(
    private val localRepository: LocalEventRepository,
    private val remoteRepository: RemoteEventRepository,
    private val networkStatusTracker: com.andreeailie.core.network.NetworkStatusTracker
) {
    suspend operator fun invoke(event: Event) {
        Log.d("ToggleFavouriteStatusUseCase", "event: $event")
        // val updatedEvent = event.copy(is_favourite = !event.is_favourite)
        // localRepository.insertEvent(updatedEvent)
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        if (isNetworkAvailable) {
            try {
                localRepository.insertEvent(
                    event.copy(
                        isFavourite = !event.isFavourite,
                        action = null
                    )
                )
                if (event.isFavourite) {
                    remoteRepository.deleteEventFromFavorites(event)
                } else {
                    remoteRepository.addEventToFavorites(event)
                }
            } catch (e: Exception) {
                if (event.isFavourite) {
                    localRepository.insertEvent(
                        event.copy(
                            isFavourite = false,
                            action = "add_to_favourites"
                        )
                    )
                } else {
                    localRepository.insertEvent(
                        event.copy(
                            isFavourite = true,
                            action = "delete_from_favourites"
                        )
                    )
                }
            }
        } else {
            if (event.isFavourite) {
                localRepository.insertEvent(
                    event.copy(
                        isFavourite = false,
                        action = "add_to_favourites"
                    )
                )
            } else {
                localRepository.insertEvent(
                    event.copy(
                        isFavourite = true,
                        action = "delete_from_favourites"
                    )
                )
            }
        }
    }
}