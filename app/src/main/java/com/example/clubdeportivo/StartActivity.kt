package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val btnIngreso: ImageButton = findViewById(R.id.btnInicio)
        btnIngreso.setOnClickListener {
            val intent = Intent(this,StartActivity::class.java)
            startActivity(intent)
        }
        val btnListas: ImageButton = findViewById(R.id.btnListas)
        btnListas.setOnClickListener {
            val intent = Intent(this,ListsActivity::class.java)
            startActivity(intent)
        }
        val btnAbonar: ImageButton = findViewById(R.id.btnAbonar)
        btnAbonar.setOnClickListener {
            val intent = Intent(this,PaymentActivity::class.java)
            startActivity(intent)
        }
        val btnAgregar: ImageButton = findViewById(R.id.btnAgregar)
        btnAgregar.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }
        val btnSalir: ImageButton = findViewById(R.id.btnSalir)
        btnSalir.setOnClickListener { finishAffinity() }
    }
}