package com.andreeailie.citypulse.feature_event.presentation.add_edit_event

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.citypulse.feature_event.domain.model.Event
import com.andreeailie.citypulse.feature_event.domain.model.InvalidEventException
import com.andreeailie.citypulse.feature_event.domain.use_case.EventUseCases
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

    init {
        Log.i("AddEditEventViewModel", "intra")
        Log.i("AddEditEventViewModel", "id: ${savedStateHandle.get<Int>("eventId")}")
        savedStateHandle.get<Int>("eventId")?.let { eventId ->

            if (eventId != -1) {
                Log.i("AddEditEventViewModel", "eventId: $eventId")
                viewModelScope.launch {
                    eventUseCases.getEventByIDUseCase(eventId)?.also { event ->
                        currentEventId = event.id
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
                            text = event.imagePath,
                            isHintVisible = false
                        )
                    }
                }
            }

        }
    }

    fun onEvent(event: AddEditEventEvent) {
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

            is AddEditEventEvent.SaveEvent -> {
                viewModelScope.launch {
                    try {
                        val event = if (currentEventId != null) {
                            Event(
                                id = currentEventId!!,
                                time = eventTime.value.text,
                                band = eventBand.value.text,
                                location = eventLocation.value.text,
                                imagePath = eventImagePath.value.text,
                                isFavourite = false,
                                isPrivate = true
                            )
                        } else {
                            Event(
                                id = 0,
                                time = eventTime.value.text,
                                band = eventBand.value.text,
                                location = eventLocation.value.text,
                                imagePath = eventImagePath.value.text,
                                isFavourite = false,
                                isPrivate = true
                            )
                        }
                        eventUseCases.addEventUseCase(event)
                        _eventFlow.emit(UiEvent.SaveEvent)
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
        object SaveEvent : UiEvent()
    }
}