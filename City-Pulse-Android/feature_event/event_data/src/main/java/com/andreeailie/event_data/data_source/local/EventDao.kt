package com.andreeailie.event_data.data_source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andreeailie.event_domain.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    fun getEvents(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEventById(id: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("Delete FROM event")
    suspend fun deleteAll()

    @Query("SELECT * FROM event WHERE action IS NOT NULL")
    fun getEventsWithPendingActions(): List<Event>


}