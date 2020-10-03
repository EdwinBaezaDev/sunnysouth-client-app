package com.sunnysouth.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunnysouth.repository.models.Login
import com.sunnysouth.repository.models.LoginSuccess
import com.sunnysouth.repository.rest.login.LoginRepository

class LoginViewModel (): ViewModel(){

    private lateinit var context: Context
    var service: LoginRepository = LoginRepository(this)
    var loginSuccess: MutableLiveData<LoginSuccess> = MutableLiveData()
    var loginError:  MutableLiveData<String> = MutableLiveData()
    var authenticationState: MutableLiveData<AuthenticationState> = MutableLiveData()

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED  ,        // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    fun login(login: Login){
        service.login(login, this.context)
    }

    fun setContextApp(context: Context){
        this.context = context
    }

    fun setLoginSuccess(loginSuccess: LoginSuccess?){
        this.loginSuccess.value = loginSuccess
        this.setAuthenticationState(AuthenticationState.AUTHENTICATED)
        this.saveDataLogin(loginSuccess)
    }

    fun setLoginError(errorMessage: String){
        this.loginError.value = errorMessage
        this.setAuthenticationState(AuthenticationState.INVALID_AUTHENTICATION)
    }

    private fun setAuthenticationState(authenticationState: AuthenticationState){
        this.authenticationState.value = authenticationState
    }

    private fun saveDataLogin(login: LoginSuccess?) {
        val sharedPref = this.context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("token", login?.access_token)
        editor.putString("email", login?.user?.email)
        editor.apply()
    }
}