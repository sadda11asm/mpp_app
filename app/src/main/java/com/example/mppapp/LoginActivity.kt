package com.example.mppapp

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mppapp.databinding.ActivityLoginBinding
import org.kotlin.mpp.mobile.ServiceLocator
import org.kotlin.mpp.mobile.data.entity.AuthorizationResponse
import org.kotlin.mpp.mobile.presentation.login.LoginView

class LoginActivity: AppCompatActivity(), LoginView {

    private val logTag = LoginActivity::class.java.simpleName

    private val presenter by lazy { ServiceLocator.loginPresenter}


    override fun showLoadingVisible(visible: Boolean) {
        Log.v("LALALA", visible.toString())
    }

    override fun showFailedLogin() {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_LONG).show()
    }

    override fun showSuccessfulLogin(response: AuthorizationResponse) {
        MainActivity.open(this, response)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initListeners(binding)



    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()

    }

    private fun initListeners(binding: ActivityLoginBinding) {
        binding.authorizationButtonEnter.setOnClickListener {
            presenter.onLogin(binding.authorizationTextLogin.text.toString(), binding.authorizationTextPass.text.toString())
        }
    }
}