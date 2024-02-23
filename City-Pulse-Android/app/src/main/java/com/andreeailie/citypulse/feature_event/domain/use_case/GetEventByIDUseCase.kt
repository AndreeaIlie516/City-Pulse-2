package com.andreeailie.citypulse.feature_event.domain.use_case

import com.andreeailie.citypulse.feature_event.domain.model.Event
import com.andreeailie.citypulse.feature_event.domain.repository.EventRepository

class GetEventByIDUseCase(
    private val repository: EventRepository
) {

    suspend operator fun invoke(id: Int): Event? {
        return repository.getEventById(id)
    }
}