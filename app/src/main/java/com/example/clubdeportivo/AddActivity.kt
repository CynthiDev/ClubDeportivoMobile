package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity


class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val btnCliente: Button = findViewById(R.id.btnCliente)
        btnCliente.setOnClickListener {
            val intent = Intent(this,AddClientActivity::class.java)
            startActivity(intent)
        }

        val btnProfesor: Button = findViewById(R.id.btnProfesor)
        btnProfesor.setOnClickListener {
            val intent = Intent(this,AddTeacherActivity::class.java)
        }

        val btnActividad: Button = findViewById(R.id.btnActividad)
        btnActividad.setOnClickListener {
            val intent = Intent(this,AddActivityActivity::class.java)
            startActivity(intent)
        }
    }
}