package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import android.widget.EditText
import android.widget.Toast
import com.example.clubdeportivo.helpers.DatabaseHelper

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val btnIngreso: Button = findViewById<Button>(R.id.btnIngresar)

        val btnSalir: Button = findViewById(R.id.btnSalir)
        btnSalir.setOnClickListener { finishAffinity() }

        // Eliminar la base de datos
        deleteDatabase("ClubDeportivo.db")

        databaseHelper = DatabaseHelper(this)

        btnIngreso.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (databaseHelper.validateAdmin(username, password)) {
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

    }


}