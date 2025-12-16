package ua.ipze.kpi.part.services.geogetter

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getLocation(onResult: (String) -> Unit) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        getLocationName(location.latitude, location.longitude, onResult)
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
                        getLocationName(location.latitude, location.longitude, onResult)
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

    private fun getLocationName(lat: Double, lon: Double, onResult: (String) -> Unit) {
        try {
            val geocoder = Geocoder(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    lat,
                    lon,
                    1,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            val address = addresses.firstOrNull()
                            val city = address?.locality ?: "Unknown city"
                            val country = address?.countryName ?: "Unknown country"
                            onResult("$city, $country")
                        }

                        override fun onError(errorMessage: String?) {
                            onResult("Error: $errorMessage")
                        }
                    }
                )
            } else {
                @Suppress("DEPRECATION")
                try {
                    val address = geocoder
                        .getFromLocation(lat, lon, 1)?.firstOrNull()

                    val city = address?.locality ?: "Unknown city"
                    val country = address?.countryName ?: "Unknown country"
                    onResult("$city, $country")
                } catch (e: Exception) {
                    onResult("Error")
                }
            }
        } catch (e: Exception) {
            onResult("Error: ${e.message}")
        }
    }
}