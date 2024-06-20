package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIngreso: Button = findViewById(R.id.btnIngresar)
        btnIngreso.setOnClickListener {
            val intent = Intent(this,StartActivity::class.java)
            startActivity(intent)
        }
        val btnSalir: Button = findViewById(R.id.btnSalir)
        btnSalir.setOnClickListener { finishAffinity() }

    }


}