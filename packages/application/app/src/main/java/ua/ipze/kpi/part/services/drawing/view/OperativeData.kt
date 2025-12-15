package ua.ipze.kpi.part.services.drawing.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OperativeData(viewModelScope: CoroutineScope, oldTime: Int = 0) {
    private val viewModelScope = viewModelScope
    private val _time = MutableStateFlow(oldTime)
    val time: StateFlow<Int> = _time

    private var timerJob: Job? = null

    fun start() {
        if (timerJob != null) return

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _time.update { it + 1 }
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
        timerJob = null
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
        _time.value = 0
    }

}