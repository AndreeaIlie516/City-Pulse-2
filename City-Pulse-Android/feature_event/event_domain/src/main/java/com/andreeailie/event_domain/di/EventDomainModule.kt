package com.andreeailie.event_domain.di

import com.andreeailie.event_domain.repository.LocalEventRepository
import com.andreeailie.event_domain.repository.RemoteEventRepository
import com.andreeailie.event_domain.use_case.AddEventUseCase
import com.andreeailie.event_domain.use_case.DeleteEventFromFavouritesUseCase
import com.andreeailie.event_domain.use_case.DeleteEventUseCase
import com.andreeailie.event_domain.use_case.EventUseCases
import com.andreeailie.event_domain.use_case.GetEventByIDUseCase
import com.andreeailie.event_domain.use_case.GetEventsUseCase
import com.andreeailie.event_domain.use_case.ToggleFavouriteStatusUseCase
import com.andreeailie.event_domain.use_case.UpdateEventUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventDomainModule {

    @Provides
    @Singleton
    fun provideEventUseCases(
        localRepository: LocalEventRepository,
        remoteRepository: RemoteEventRepository,
        networkStatusTracker: com.andreeailie.core.network.NetworkStatusTracker
    ): EventUseCases {
        return EventUseCases(
            getEventsUseCase = GetEventsUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            getEventByIDUseCase = GetEventByIDUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            addEventUseCase = AddEventUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            deleteEventUseCase = DeleteEventUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker),
            toggleFavouriteStatusUseCase = ToggleFavouriteStatusUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            deleteEventFromFavouritesUseCase = DeleteEventFromFavouritesUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            updateEventUseCase = UpdateEventUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            )
        )
    }

}