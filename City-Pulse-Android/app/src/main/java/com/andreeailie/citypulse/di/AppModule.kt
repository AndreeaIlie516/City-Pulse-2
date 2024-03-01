package com.andreeailie.citypulse.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.andreeailie.citypulse.feature_event.data.data_source.local.EventDatabase
import com.andreeailie.citypulse.feature_event.data.data_source.remote.EventApi
import com.andreeailie.citypulse.feature_event.data.network.NetworkStatusTracker
import com.andreeailie.citypulse.feature_event.data.repository.LocalEventRepositoryImpl
import com.andreeailie.citypulse.feature_event.data.repository.RemoteEventRepositoryImpl
import com.andreeailie.citypulse.feature_event.domain.repository.LocalEventRepository
import com.andreeailie.citypulse.feature_event.domain.repository.RemoteEventRepository
import com.andreeailie.citypulse.feature_event.domain.use_case.AddEventUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.DeleteEventFromFavouritesUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.DeleteEventUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.EventUseCases
import com.andreeailie.citypulse.feature_event.domain.use_case.GetEventByIDUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.GetEventsUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.ToggleFavouriteStatusUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.UpdateEventUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEventDatabase(app: Application): EventDatabase {
        return Room.databaseBuilder(
            app,
            EventDatabase::class.java,
            EventDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideNetworkStatusTracker(@ApplicationContext context: Context): NetworkStatusTracker {
        return NetworkStatusTracker(context)
    }


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:2327")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideEventApi(retrofit: Retrofit): EventApi {
        return retrofit.create(EventApi::class.java)
    }

    @Provides
    fun provideLocalEventRepository(
        db: EventDatabase,
    ): LocalEventRepository {
        return LocalEventRepositoryImpl(db.eventDao)
    }

    @Provides
    fun provideRemoteEventRepository(
        eventApi: EventApi,
    ): RemoteEventRepository {
        return RemoteEventRepositoryImpl(eventApi)
    }

    @Provides
    @Singleton
    fun provideEventUseCases(
        localRepository: LocalEventRepository,
        remoteRepository: RemoteEventRepository,
        networkStatusTracker: NetworkStatusTracker
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