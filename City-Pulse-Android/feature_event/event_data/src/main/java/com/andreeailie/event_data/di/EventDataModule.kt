package com.andreeailie.event_data.di

import android.app.Application
import androidx.room.Room
import com.andreeailie.event_data.data_source.local.EventDatabase
import com.andreeailie.event_data.data_source.remote.EventApi
import com.andreeailie.event_data.repository.LocalEventRepositoryImpl
import com.andreeailie.event_data.repository.RemoteEventRepositoryImpl
import com.andreeailie.event_domain.repository.LocalEventRepository
import com.andreeailie.event_domain.repository.RemoteEventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventDataModule {

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
}