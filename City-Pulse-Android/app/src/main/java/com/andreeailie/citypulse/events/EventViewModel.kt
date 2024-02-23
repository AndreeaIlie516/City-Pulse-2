package com.andreeailie.citypulse.events

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventViewModel : ViewModel() {
    val favoriteList = mutableStateMapOf<PredefinedEvent, Boolean>().also { favoriteList ->
        PredefinedEvent.entries.forEach { event ->
            favoriteList[event] = false
        }
    }

    val events = MutableLiveData<List<PrivateEvent>>(emptyList())

    fun addEvent(event: PrivateEvent) {
        val currentEvents = events.value ?: emptyList()
        events.value = currentEvents + event
    }
}