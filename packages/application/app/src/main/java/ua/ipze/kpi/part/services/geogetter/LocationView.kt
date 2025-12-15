package ua.ipze.kpi.part.services.geogetter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val locationManager = LocationManager(application)

    private val _city = MutableStateFlow<String?>(null)
    val city: StateFlow<String?> = _city.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchLocation() {
        viewModelScope.launch {
            _isLoading.value = true

            locationManager.getLocation { result ->
                _city.value = result
                _isLoading.value = false
            }
        }
    }
}