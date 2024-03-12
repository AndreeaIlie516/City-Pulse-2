package com.andreeailie.event_presentation.add_edit_event

data class EventTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)