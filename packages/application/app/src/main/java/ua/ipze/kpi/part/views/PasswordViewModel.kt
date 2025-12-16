package ua.ipze.kpi.part.views

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import ua.ipze.kpi.part.utils.PasswordStore


class PasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val passwordStore = PasswordStore(application.applicationContext)

    var passwordInput by mutableStateOf("")

    var isAuthenticated by mutableStateOf(false)
        private set

    var passwordExists by mutableStateOf(false)
        private set

    init {
        checkPasswordExists()
    }

    fun clearPassword() {
        passwordStore.clearPassword()
        passwordExists = false
    }

    private fun checkPasswordExists() {
        val storedPassword = passwordStore.getPassword()
        passwordExists = storedPassword != null
        Log.d("PasswordCheck", "Password exist $passwordExists $storedPassword")
    }

    fun createPass() {
        passwordStore.savePassword(passwordInput)
        isAuthenticated = true
        passwordExists = true
    }

    fun handleLogin(){
        val storedPassword = passwordStore.getPassword()
        Log.d("PasswordCheck", "Password is correct $passwordInput")
        isAuthenticated = passwordInput == storedPassword
    }
}