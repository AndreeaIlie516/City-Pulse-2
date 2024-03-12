package com.andreeailie.event_presentation.events

import com.andreeailie.event_domain.model.Event

sealed class EventsEvent {
    data class CreateEvent(val event: Event) : EventsEvent()
    data class UpdateEvent(val event: Event) : EventsEvent()
    data class DeleteEvent(val event: Event) : EventsEvent()
    data class DeleteEventFromFavourites(val event: Event) : EventsEvent()
    data object RestoreEvent : EventsEvent()
}