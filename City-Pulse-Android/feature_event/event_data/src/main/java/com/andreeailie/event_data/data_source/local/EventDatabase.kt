package com.andreeailie.event_data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreeailie.event_domain.model.Event

@Database(
    entities = [Event::class],
    version = 2
)
abstract class EventDatabase : RoomDatabase() {

    abstract val eventDao: EventDao

    companion object {
        const val DATABASE_NAME = "event_db"
    }
}