package com.example.clubdeportivo

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.clubdeportivo.enums.ModalidadDePago
import com.example.clubdeportivo.helpers.DatabaseHelper
import java.io.IOException
import kotlin.math.log


class AddClientActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)


        //APTO FISICO
        var hasAptoFisico = false
        val botonCertificado = findViewById<Button>(R.id.save_apto_fisico_button)
        botonCertificado.setOnClickListener {
            hasAptoFisico = true
            Toast.makeText(this, "Apto físico cargado correctamente", Toast.LENGTH_SHORT).show()
            botonCertificado.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightViolet))
        }


        //ACTIVIDADES LIST
        val radioGroup = findViewById<RadioGroup>(R.id.esSocio)
        val spinerActivity = findViewById<Spinner>(R.id.spinner_actividad)
        var esSocio: Boolean = false
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.nosocio -> {
                    esSocio = false
                    spinerActivity.setVisibility(View.VISIBLE) // Mostrar el spinner
                    // Carga las opciones del spinner
                    databaseHelper = DatabaseHelper(this)
                    var opcionesActivityList = databaseHelper.getAllActivitys()
                    opcionesActivityList = arrayOf("Seleccionar actividad") + opcionesActivityList
                    val adapterActivity: ArrayAdapter<String?> = ArrayAdapter<String?>(this, android.R.layout.simple_spinner_item, opcionesActivityList)
                    adapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinerActivity.setPadding(16, 16, 16, 16) // ajusta el padding a 16dp en todos los lados
                    spinerActivity.setAdapter(adapterActivity)
                    // Establecer el elemento "placeholder" como seleccionado por defecto
                    spinerActivity.setSelection(0)
                }
                R.id.socio -> {
                    esSocio = true
                    spinerActivity.setVisibility(View.GONE) // Ocultar el spinner
                }
            }
        }


        //METODO DE PAGO LIST
        val spinerMetodoPago = findViewById<Spinner>(R.id.spinner_metodo_pago)
        var opcionesPagoList: Array<String> = arrayOf(ModalidadDePago.EFECTIVO.descripcion, ModalidadDePago.UNA_CUOTA.descripcion, ModalidadDePago.TRES_CUOTAS.descripcion);
        opcionesPagoList = arrayOf("Seleccionar método de pago") + opcionesPagoList
        val adapterPago: ArrayAdapter<String?> = ArrayAdapter<String?>(this, android.R.layout.simple_spinner_item, opcionesPagoList)
        adapterPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinerMetodoPago.setPadding(16, 16, 16, 16) // ajusta el padding a 16dp en todos los lados
        spinerMetodoPago.setAdapter(adapterPago)
        // Establecer el elemento "placeholder" como seleccionado por defecto
        spinerMetodoPago.setSelection(0)


        //GUARDAR
        findViewById<Button>(R.id.save_client_button).setOnClickListener {
            guardarDatos(hasAptoFisico, esSocio)
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun guardarDatos(hasAptoFisico: Boolean, esSocio: Boolean) {
        val nombre = findViewById<TextView>(R.id.nameEditText).text.toString().trim()
        val apellido = findViewById<TextView>(R.id.lastNameEditText).text.toString().trim()
        val dni = findViewById<TextView>(R.id.dniEditText).text.toString().trim()
        val radioGroup = findViewById<RadioGroup>(R.id.esSocio)
        val spinerActivity = findViewById<Spinner>(R.id.spinner_actividad)
        val spinerMetodoPago = findViewById<Spinner>(R.id.spinner_metodo_pago)

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if(dni.length != 8){
            Toast.makeText(this, "El DNI debe tener 8 dígitos", Toast.LENGTH_SHORT).show()
            return
        }

        if (radioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Debe seleccionar el tipo de cliente", Toast.LENGTH_SHORT).show()
            return
        }

        if (spinerActivity.visibility == View.VISIBLE && spinerActivity.selectedItemPosition == 0) {
            Toast.makeText(this, "Debe seleccionar una actividad", Toast.LENGTH_SHORT).show()
            return
        }

        if (spinerMetodoPago.selectedItemPosition == 0) {
            Toast.makeText(this, "Debe seleccionar un método de pago", Toast.LENGTH_SHORT).show()
            return
        }

        if (!hasAptoFisico) {
            Toast.makeText(this, "Debe cargar el certificado de apto físico", Toast.LENGTH_SHORT).show()
            return
        }

        // CREAR CLIENTE
        val dniInt = dni.toIntOrNull();
        val metodoPago = spinerMetodoPago.getSelectedItem().toString()
        val actividad = if (!esSocio)spinerActivity.getSelectedItem().toString() else null;
        if (dniInt != null) {
            val registrado = databaseHelper.insertarCliente(dniInt, nombre,apellido,esSocio, metodoPago,actividad)
            if (registrado){
                Toast.makeText(this, "El cliente ha sido registrado" , Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Existe un cliente con el mismo DNI" , Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Debe completar el campo DNI", Toast.LENGTH_SHORT).show()
        }


    }
}