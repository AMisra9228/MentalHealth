package com.sample.mentalhealth.login_registration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.MainActivity
import com.sample.mentalhealth.R

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val btnStart = findViewById<Button>(R.id.btnStartGame)

        btnStart.setOnClickListener {
            // Launch the Tic-Tac-Toe SDK Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}