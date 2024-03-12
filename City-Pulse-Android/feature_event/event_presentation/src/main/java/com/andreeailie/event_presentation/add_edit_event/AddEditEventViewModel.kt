package com.andreeailie.event_presentation.add_edit_event

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.event_domain.model.Event
import com.andreeailie.event_domain.model.InvalidEventException
import com.andreeailie.event_domain.use_case.EventUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditEventViewModel
@Inject
constructor(
    private val eventUseCases: EventUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventTime = mutableStateOf(
        EventTextFieldState(
            hint = "Enter time"
        )
    )

    val eventTime: State<EventTextFieldState> = _eventTime

    private val _eventBand = mutableStateOf(
        EventTextFieldState(
            hint = "Enter band"
        )
    )

    val eventBand: State<EventTextFieldState> = _eventBand

    private val _eventLocation = mutableStateOf(
        EventTextFieldState(
            hint = "Enter location"
        )
    )

    val eventLocation: State<EventTextFieldState> = _eventLocation

    private val _eventImagePath = mutableStateOf(
        EventTextFieldState(
            hint = "Enter image path"
        )
    )

    val eventImagePath: State<EventTextFieldState> = _eventImagePath

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentEventId: Int? = null
    private var currentEventIdLocal: Int? = null

    init {
        savedStateHandle.get<Int>("eventId")?.let { eventId ->

            if (eventId != -1) {
                Log.i("AddEditEventViewModel", "eventId: $eventId")
                viewModelScope.launch {

                    eventUseCases.getEventByIDUseCase(eventId, local = true)?.also { event ->
                        currentEventId = event.id
                        currentEventIdLocal = event.idLocal
                        _eventTime.value = eventTime.value.copy(
                            text = event.time,
                            isHintVisible = false
                        )
                        _eventBand.value = eventBand.value.copy(
                            text = event.band,
                            isHintVisible = false
                        )
                        _eventLocation.value = eventLocation.value.copy(
                            text = event.location,
                            isHintVisible = false
                        )
                        _eventImagePath.value = eventImagePath.value.copy(
                            text = event.imageUrl,
                            isHintVisible = false
                        )
                    }
                }
            }

        }
    }

    fun onEvent(event: AddEditEventEvent) {
        Log.i("AddEditEventViwModel", "on event: $event")
        when (event) {
            is AddEditEventEvent.EnteredTime -> {
                _eventTime.value = eventTime.value.copy(
                    text = event.value
                )
            }

            is AddEditEventEvent.ChangeTimeFocus -> {
                _eventTime.value = eventTime.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            eventTime.value.text.isBlank()
                )
            }

            is AddEditEventEvent.EnteredBand -> {
                _eventBand.value = eventBand.value.copy(
                    text = event.value
                )
            }

            is AddEditEventEvent.ChangeBandFocus -> {
                _eventBand.value = eventBand.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            eventBand.value.text.isBlank()
                )
            }

            is AddEditEventEvent.EnteredLocation -> {
                _eventLocation.value = eventLocation.value.copy(
                    text = event.value
                )
            }

            is AddEditEventEvent.ChangeLocationFocus -> {
                _eventLocation.value = eventLocation.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            eventLocation.value.text.isBlank()
                )

            }

            is AddEditEventEvent.EnteredImagePath -> {
                _eventImagePath.value = eventImagePath.value.copy(
                    text = event.value
                )
            }

            is AddEditEventEvent.ChangeImagePathFocus -> {
                _eventImagePath.value = eventImagePath.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            eventImagePath.value.text.isBlank()
                )
            }

            is AddEditEventEvent.SaveNewEvent -> {
                Log.i("AddEditEventViewModel", "Save new event current event id: $currentEventId")
                viewModelScope.launch {
                    try {
                        val event = if (currentEventId != null) {
                            Event(
                                idLocal = currentEventIdLocal!!,
                                id = currentEventId!!,
                                time = eventTime.value.text,
                                band = eventBand.value.text,
                                location = eventLocation.value.text,
                                imageUrl = eventImagePath.value.text,
                                isFavourite = false,
                                isPrivate = true,
                                action = null
                            )
                        } else {
                            Event(
                                idLocal = 0,
                                id = 0,
                                time = eventTime.value.text,
                                band = eventBand.value.text,
                                location = eventLocation.value.text,
                                imageUrl = eventImagePath.value.text,
                                isFavourite = false,
                                isPrivate = true,
                                action = null
                            )
                        }
                        eventUseCases.addEventUseCase(event)
                        _eventFlow.emit(UiEvent.SaveNewEvent)
                    } catch (e: InvalidEventException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save event"
                            )
                        )
                    }
                }
            }

            is AddEditEventEvent.SaveUpdatedEvent -> {
                Log.i("AddEditEventViewModel", "Save updated event current event id: $currentEventId")
                Log.i("AddEditEventViewModel", "Save updated event current event id local: $currentEventIdLocal")

                viewModelScope.launch {
                    try {
                        val event = if (currentEventId != null) {
                            Event(
                                idLocal = currentEventIdLocal!!,
                                id = currentEventId!!,
                                time = eventTime.value.text,
                                band = eventBand.value.text,
                                location = eventLocation.value.text,
                                imageUrl = eventImagePath.value.text,
                                isFavourite = false,
                                isPrivate = true,
                                action = null
                            )
                        } else {
                            Event(
                                idLocal = 0,
                                id = 0,
                                time = eventTime.value.text,
                                band = eventBand.value.text,
                                location = eventLocation.value.text,
                                imageUrl = eventImagePath.value.text,
                                isFavourite = false,
                                isPrivate = true,
                                action = null
                            )
                        }
                        eventUseCases.updateEventUseCase(event)
                        _eventFlow.emit(UiEvent.SaveUpdatedEvent)
                    } catch (e: InvalidEventException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save event"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object SaveNewEvent : UiEvent()
        data object SaveUpdatedEvent : UiEvent()
    }
}