package com.andreeailie.event_domain.use_case

import com.andreeailie.core.network.NetworkStatusTracker
import com.andreeailie.event_domain.model.Event
import com.andreeailie.event_domain.repository.LocalEventRepository
import com.andreeailie.event_domain.repository.RemoteEventRepository

class GetEventByIDUseCase(
    private val localRepository: LocalEventRepository,
    private val remoteRepository: RemoteEventRepository,
    private val networkStatusTracker: com.andreeailie.core.network.NetworkStatusTracker
) {

    suspend operator fun invoke(id: Int, local: Boolean = false): Event? {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        return try {
            localRepository.getEventById(id)
//            if (local) {
//                localRepository.getEventById(id)
//            }
//            if (isNetworkAvailable) {
//                remoteRepository.getEventById(id)
//            } else {
//                localRepository.getEventById(id)
//            }
        } catch (e: Exception) {
            throw Exception("Failed to get the event. Please try again later.")
        }
    }
}