package com.andreeailie.citypulse.events

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class EventViewModel : ViewModel() {
    val favoriteList = mutableStateMapOf<Event, Boolean>().also { favoriteList ->
        Event.entries.forEach { event ->
            favoriteList[event] = false
        }
    }
}