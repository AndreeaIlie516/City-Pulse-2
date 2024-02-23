package com.andreeailie.citypulse

import com.andreeailie.citypulse.events.Event

data class FavoriteEvent (
    val favorites: MutableMap<Event, Boolean> = mutableMapOf()
)