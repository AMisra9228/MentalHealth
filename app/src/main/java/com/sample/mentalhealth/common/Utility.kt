package com.sample.mentalhealth.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.widget.Toast
import com.sample.mentalhealth.login_registration.SignInActivity
import androidx.core.content.edit

class Utility {
     fun clearUserLoginData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("UserLog", MODE_PRIVATE) // Same name as you use for reading
         sharedPreferences.edit {
             clear()
         }

         Toast.makeText(context, "Logged out and data cleared", Toast.LENGTH_SHORT).show()
         val intent = Intent(context, SignInActivity::class.java)
         intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
         context.startActivity(intent)
    }
}