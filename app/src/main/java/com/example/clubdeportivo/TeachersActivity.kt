package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TeachersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teachers)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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

        val btnProfesor1: Button = findViewById(R.id.btnProfesor1)
        btnProfesor1.setOnClickListener {
            val intent = Intent(this,TeacherDataActivity::class.java)
            startActivity(intent)
        }
    }
}