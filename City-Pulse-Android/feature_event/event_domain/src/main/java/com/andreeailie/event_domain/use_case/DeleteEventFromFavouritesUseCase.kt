package com.andreeailie.event_domain.use_case

import com.andreeailie.event_domain.model.Event
import com.andreeailie.event_domain.repository.LocalEventRepository
import com.andreeailie.event_domain.repository.RemoteEventRepository

class DeleteEventFromFavouritesUseCase(
    private val localRepository: LocalEventRepository,
    private val remoteRepository: RemoteEventRepository,
    private val networkStatusTracker: com.andreeailie.core.network.NetworkStatusTracker
) {
    suspend operator fun invoke(event: Event) {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        val updatedEvent = event.copy(isFavourite = false)
        localRepository.insertEvent(updatedEvent)
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