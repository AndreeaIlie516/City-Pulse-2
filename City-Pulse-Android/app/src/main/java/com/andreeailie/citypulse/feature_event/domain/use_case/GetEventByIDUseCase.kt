package com.andreeailie.citypulse.feature_event.domain.use_case

import com.andreeailie.citypulse.feature_event.data.network.NetworkStatusTracker
import com.andreeailie.citypulse.feature_event.domain.model.Event
import com.andreeailie.citypulse.feature_event.domain.repository.LocalEventRepository
import com.andreeailie.citypulse.feature_event.domain.repository.RemoteEventRepository

class GetEventByIDUseCase(
    private val localRepository: LocalEventRepository,
    private val remoteRepository: RemoteEventRepository,
    private val networkStatusTracker: NetworkStatusTracker
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