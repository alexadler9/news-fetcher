package ru.alexadler9.newsfetcher.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<VIEW_STATE, VIEW_EVENT> : ViewModel() {

    protected abstract val initialViewState: VIEW_STATE

    /**
     * Describes what screen state should be displayed right now (flows from ViewModel to View).
     */
    private val _viewState by lazy { MutableStateFlow(initialViewState) }
    val viewState: StateFlow<VIEW_STATE>
        get() = _viewState

    /**
     * One-time events like showing Snackbar or Dialog, etc (flows from ViewModel to View).
     */
    private val _viewEvents = Channel<VIEW_EVENT?>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    /**
     * Actions is the only legal way to tell something to ViewModel from a View (flows from View to ViewModel).
     */
    private val actions = Channel<Action>(Channel.BUFFERED)

    abstract fun reduce(action: Action, previousState: VIEW_STATE): VIEW_STATE?

    init {
        viewModelScope.launch {
            actions.consumeEach { action ->
                updateViewState(action)
            }
        }
    }

    fun processUiAction(action: Action) = viewModelScope.launch {
        actions.send(action)
    }

    protected fun processDataAction(action: Action) = viewModelScope.launch {
        actions.send(action)
    }

    protected fun sendViewEvent(event: VIEW_EVENT) = viewModelScope.launch {
        _viewEvents.send(event)
        _viewEvents.send(null)
    }

    private fun updateViewState(action: Action) {
        val newViewState = reduce(action, _viewState.value)
        if (newViewState != null) {
            _viewState.value = newViewState
        }
    }
}