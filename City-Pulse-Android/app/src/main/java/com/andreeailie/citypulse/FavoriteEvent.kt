package com.andreeailie.citypulse

data class FavoriteEvent (
    val favorites: MutableMap<Event, Boolean> = mutableMapOf()
)