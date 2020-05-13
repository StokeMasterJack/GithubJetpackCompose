package com.smartsoft.github

import android.os.Build
import android.os.Bundle
import android.security.NetworkSecurityPolicy
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted
        val viewModel by viewModels<AppViewModel> { AppCtx.mkViewModelFactory() }
        setContent {
            App(viewModel)
        }
    }

}


