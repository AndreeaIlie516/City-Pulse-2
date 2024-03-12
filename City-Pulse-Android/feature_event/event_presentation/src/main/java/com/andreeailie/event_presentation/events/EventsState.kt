package com.andreeailie.event_presentation.events

import com.andreeailie.event_domain.model.Event

data class EventsState(
    val events: List<Event> = emptyList()
)