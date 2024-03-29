package com.andreeailie.event_domain.use_case

data class EventUseCases(
    val getEventsUseCase: GetEventsUseCase,
    val getEventByIDUseCase: GetEventByIDUseCase,
    val addEventUseCase: AddEventUseCase,
    val deleteEventUseCase: DeleteEventUseCase,
    val deleteEventFromFavouritesUseCase: DeleteEventFromFavouritesUseCase,
    val toggleFavouriteStatusUseCase: ToggleFavouriteStatusUseCase,
    val updateEventUseCase: UpdateEventUseCase
)