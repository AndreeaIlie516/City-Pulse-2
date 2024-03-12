package com.andreeailie.event_domain.use_case

import android.util.Log
import com.andreeailie.core.network.NetworkStatusTracker
import com.andreeailie.event_domain.model.Event
import com.andreeailie.event_domain.repository.LocalEventRepository
import com.andreeailie.event_domain.repository.RemoteEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class GetEventsUseCase(
    private val localRepository: LocalEventRepository,
    private val remoteRepository: RemoteEventRepository,
    private val networkStatusTracker: com.andreeailie.core.network.NetworkStatusTracker
) {

    operator fun invoke(): Flow<List<Event>> = flow {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        val localEvents = localRepository.getEvents()
        Log.d("GetEventsUseCase", "localEvents: $localEvents")
        if (isNetworkAvailable) {
            try {
                val remoteEvents = remoteRepository.getEvents()
                Log.d("GetEventsUseCase", "remoteEvents: $remoteEvents")

                val mergedEvents = localEvents.combine(flowOf(remoteEvents)) { local, remote ->
                    Log.d("GetEventsUseCase", "Merging: Local - $local, Remote - $remote")
                    val allEvents = (local + remote)
                    allEvents.distinctBy { Triple(it.time, it.band, it.location) }
                }

                localRepository.clearAndCacheEvents(mergedEvents)

                emitAll(mergedEvents)
            } catch (e: Exception) {
                emitAll(localRepository.getEvents())
            }
        } else {
            emitAll(localRepository.getEvents())
        }
    }.flowOn(Dispatchers.IO)
}