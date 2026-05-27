// MyApp.kt
package com.sample.mentalhealth

import android.app.Application
import com.sample.mentalhealth.di.AppComponent
import com.sample.mentalhealth.di.AppModule
import com.sample.mentalhealth.di.DaggerAppComponent

class MyApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
