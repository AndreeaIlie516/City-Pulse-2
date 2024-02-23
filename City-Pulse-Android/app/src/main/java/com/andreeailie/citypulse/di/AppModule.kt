package com.andreeailie.citypulse.di

import android.app.Application
import androidx.room.Room
import com.andreeailie.citypulse.feature_event.data.data_source.EventDatabase
import com.andreeailie.citypulse.feature_event.data.repository.EventRepositoryImpl
import com.andreeailie.citypulse.feature_event.domain.repository.EventRepository
import com.andreeailie.citypulse.feature_event.domain.use_case.AddEventUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.DeleteEventFromFavouritesUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.DeleteEventUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.EventUseCases
import com.andreeailie.citypulse.feature_event.domain.use_case.GetEventByIDUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.GetEventsUseCase
import com.andreeailie.citypulse.feature_event.domain.use_case.ToggleFavouriteStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    @Provides
    fun provideEventRepository(db: EventDatabase): EventRepository {
        return EventRepositoryImpl(db.eventDao)
    }

    @Provides
    @Singleton
    fun provideEventUseCases(repository: EventRepository): EventUseCases {
        return EventUseCases(
            getEventsUseCase = GetEventsUseCase(repository),
            getEventByIDUseCase = GetEventByIDUseCase(repository),
            addEventUseCase = AddEventUseCase(repository),
            deleteEventUseCase = DeleteEventUseCase(repository),
            toggleFavouriteStatusUseCase = ToggleFavouriteStatusUseCase(repository),
            deleteEventFromFavouritesUseCase = DeleteEventFromFavouritesUseCase(repository)
        )
    }
}