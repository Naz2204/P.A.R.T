package ua.ipze.kpi.part.services.geogetter

import android.content.Context
import android.location.Geocoder
import android.os.Looper
import com.google.android.gms.location.*
import java.util.Locale

class LocationManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getLocation(onResult: (String) -> Unit) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        getCityName(location.latitude, location.longitude, onResult)
                    } else {
                        requestCurrentLocation(onResult)
                    }
                }
                .addOnFailureListener {
                    onResult("Error getting location: ${it.message}")
                }
        } catch (e: SecurityException) {
            onResult("Permission error")
        }
    }

    private fun requestCurrentLocation(onResult: (String) -> Unit) {
        try {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                0L
            ).setMaxUpdates(1).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        getCityName(location.latitude, location.longitude, onResult)
                    } ?: run {
                        onResult("Could not get location")
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            onResult("Permission error")
        }
    }

    private fun getCityName(lat: Double, lon: Double, onResult: (String) -> Unit) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)

            if (!addresses.isNullOrEmpty()) {
                val city = addresses[0].locality ?: addresses[0].subAdminArea ?: "Unknown"
                onResult("You are in: $city")
            } else {
                onResult("City not found")
            }
        } catch (e: Exception) {
            onResult("Error: ${e.message}")
        }
    }
}