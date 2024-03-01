package com.andreeailie.citypulse.feature_event.presentation.events

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.citypulse.feature_event.data.network.NetworkStatusTracker
import com.andreeailie.citypulse.feature_event.domain.model.Event
import com.andreeailie.citypulse.feature_event.domain.repository.LocalEventRepository
import com.andreeailie.citypulse.feature_event.domain.repository.RemoteEventRepository
import com.andreeailie.citypulse.feature_event.domain.use_case.EventUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel
@Inject
constructor(
    private val eventUseCases: EventUseCases,
    private val localEventRepository: LocalEventRepository,
    private val remoteEventRepository: RemoteEventRepository,
    private val networkStatusTracker: NetworkStatusTracker,
) : ViewModel() {

    private val _state = mutableStateOf(EventsState())
    val state: State<EventsState> = _state

    private val _currentEvent = MutableStateFlow<Event?>(null)
    val currentEvent: StateFlow<Event?> = _currentEvent.asStateFlow()

    private var recentlyDeletedEvent: Event? = null

    private var getEventsJob: Job? = null

    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: MutableStateFlow<Boolean> = _isNetworkAvailable

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    val snackbarHostState = SnackbarHostState()

    val deletionStatus = MutableStateFlow<Boolean?>(null)

    init {
        performServerOperation {
            getEvents()
            observeNetworkStatus()
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collect { status ->
                _isNetworkAvailable.value = status == NetworkStatusTracker.NetworkStatus.Available
                if (_isNetworkAvailable.value) {
                    Log.d("EntitiesViewModel", "Network is available, syncing pending changes.")
                    syncPendingChanges()
                }
            }
        }
    }

    private fun performServerOperation(operation: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                operation()
            } catch (e: Exception) {
                Log.e("EntitiesViewModel", "Error in server operation: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onEvent(event: EventsEvent) {
        when (event) {
            is EventsEvent.CreateEvent -> performServerOperation {
                viewModelScope.launch {
                    eventUseCases.addEventUseCase(
                        event.event
                    )
                    getEvents()
                }
            }

            is EventsEvent.UpdateEvent -> performServerOperation {
                viewModelScope.launch {
                    eventUseCases.updateEventUseCase(
                        event.event
                    )
                    getEvents()
                }
            }

            is EventsEvent.DeleteEvent -> performServerOperation {
                viewModelScope.launch {
                    try {
                        Log.d("EventsViewModel", "On try")
                        eventUseCases.deleteEventUseCase(
                            event.event
                        )
                        deletionStatus.emit(true)
                        recentlyDeletedEvent = event.event
                        getEvents()
                    } catch (e: Exception) {
                        deletionStatus.emit(false)
                        Log.d("EventsViewModel", "On catch")
                        //showSnackbarMessage("Cannot delete event when offline")
                    }
                }
            }

            is EventsEvent.DeleteEventFromFavourites -> performServerOperation {
                viewModelScope.launch {
                    try {
                        Log.d("EventsViewModel", "On try")
                        eventUseCases.toggleFavouriteStatusUseCase(
                            event.event
                        )
                        deletionStatus.emit(true)
                        recentlyDeletedEvent = event.event
                        getEvents()
                    } catch (e: Exception) {
                        Log.d("EventsViewModel", "On catch")
                        deletionStatus.emit(false)
                    }
                }
            }

            is EventsEvent.RestoreEvent -> performServerOperation {
                viewModelScope.launch {
                    eventUseCases.addEventUseCase(recentlyDeletedEvent ?: return@launch)
                    recentlyDeletedEvent = null
                }
            }
        }
    }

    private fun getEvents() {
        Log.d("EventsViewModel", "getEvents() called")
        getEventsJob?.cancel()
        getEventsJob = eventUseCases.getEventsUseCase()
            .onEach { events ->
                val filteredEvents = events.filterNot { it.action == "delete" }
                _state.value = state.value.copy(
                    events = filteredEvents
                )
                Log.d("EventsViewModel", "ViewModel state updated with new events: $events")
            }
            .launchIn(viewModelScope)
    }

    fun getEventById(id: Int) {
        performServerOperation {
            viewModelScope.launch {
                try {
                    val event = eventUseCases.getEventByIDUseCase(id)
                    _currentEvent.value = event
                } catch (e: Exception) {
                    Log.e("EventsViewModel", "Error fetching event by ID: ${e.message}")
                    _currentEvent.value = null
                }
            }
        }
    }

    suspend fun syncPendingChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            val pendingEvents = localEventRepository.getEventsWithPendingActions()
            pendingEvents.forEach { event ->
                when (event.action) {
                    "add" -> remoteEventRepository.insertEvent(event)
                    "update" -> {
                        remoteEventRepository.updateEvent(event)
                        localEventRepository.insertEvent(event.copy(action = null))
                    }
                    "delete" -> {
                        remoteEventRepository.deleteEvent(event)
                        localEventRepository.deleteEvent(event)
                    }
                    "add_to_favourites" -> {
                        remoteEventRepository.updateEvent(event)
                        localEventRepository.insertEvent(event.copy(action = null))
                    }
                    "delete_from_favourites" -> {
                        remoteEventRepository.updateEvent(event)
                        localEventRepository.insertEvent(event.copy(action = null))
                    }
                }
            }
        }
    }


    fun onToggleFavourite(event: Event) {
        Log.i("EventsViewModel", "onToggleFavourite")
        Log.i("EventsViewModel", "events before toggle: ${_state.value}")
        viewModelScope.launch {
            eventUseCases.toggleFavouriteStatusUseCase(event)
            getEvents()
            Log.i("EventsViewModel", "events after toggle: ${_state.value}")
        }
    }
}