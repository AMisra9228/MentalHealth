// AppComponent.kt
package com.sample.mentalhealth.di

import com.sample.mentalhealth.home.HomeFragment
import com.sample.mentalhealth.login_registration.SignInActivity
import com.sample.mentalhealth.login_registration.SignUpActivity
import com.sample.mentalhealth.login_registration.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: SignInActivity)
    fun inject(activity: SignUpActivity)
    fun inject(activity: SplashActivity)
    fun inject(homeFragment: HomeFragment)
}
