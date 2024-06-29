package com.example.clubdeportivo

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.dto.CuotaImpaga
import com.example.clubdeportivo.enums.ModalidadDePago
import com.example.clubdeportivo.helpers.DatabaseHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import es.dmoral.toasty.Toasty



class PaymentActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listaCuotasAVencer : TextView
    private var cuotaImpaga : CuotaImpaga? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        dbHelper = DatabaseHelper(this)

        val btnIngreso: ImageButton = findViewById(R.id.btnInicio)
        btnIngreso.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
        val btnListas: ImageButton = findViewById(R.id.btnListas)
        btnListas.setOnClickListener {
            val intent = Intent(this, ListsActivity::class.java)
            startActivity(intent)
        }
        val btnAbonar: ImageButton = findViewById(R.id.btnAbonar)
        btnAbonar.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
        val btnAgregar: ImageButton = findViewById(R.id.btnAgregar)
        btnAgregar.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        val btnSalir: ImageButton = findViewById(R.id.btnSalir)
        btnSalir.setOnClickListener { finishAffinity() }

        val btnPagarSocio: Button = findViewById(R.id.btnPagarSocio)
        btnPagarSocio.setOnClickListener {
            val intent = Intent(this, PayMethodActivity::class.java)
            intent.putExtra("cuotaID", cuotaImpaga?.idCuota)
            startActivity(intent)
        }

        val btnPagarNoSocio: Button = findViewById(R.id.btnPagarNoSocio)
        btnPagarNoSocio.setOnClickListener {
            val spinnerActivity: Spinner = findViewById(R.id.sp_actividades)
            val actividad = spinnerActivity.getSelectedItem().toString()
            val intent = Intent(this, PayMethodActivity::class.java)
            intent.putExtra("actividadName", actividad)
            startActivity(intent)
        }


        val etDNI: EditText = findViewById(R.id.et_dni)
        val btnBuscar: Button = findViewById(R.id.btnBuscar)

        //buscar al presionar enter
        etDNI.setOnEditorActionListener { _, _, _ ->
            val dni = etDNI.text.toString().toInt()
            handleDniInput(dni)
            true
        }
        //implementación del botón buscar
        btnBuscar.setOnClickListener {
            val dni = etDNI.text.toString()

            // Verificar si el texto no está vacío
            if (dni.isNotEmpty()) {
                try {
                    // Convertir el texto a un número entero
                    val dni = dni.toInt()
                    handleDniInput(dni)
                } catch (e: NumberFormatException) {
                    Toasty.warning(this, "Por favor, ingrese un número válido", Toast.LENGTH_SHORT)
                        .show()
                    findViewById<LinearLayout>(R.id.ll_socio_info).visibility = View.GONE
                    findViewById<LinearLayout>(R.id.ll_no_socio_info).visibility = View.GONE
                }
            } else {
                Toasty.warning(this, "Por favor, ingrese un DNI", Toast.LENGTH_SHORT).show()
                findViewById<LinearLayout>(R.id.ll_socio_info).visibility = View.GONE
                findViewById<LinearLayout>(R.id.ll_no_socio_info).visibility = View.GONE
            }
        }


        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fecha = LocalDate.now()
        val diaHoy = fecha.format(formato)
        val tvDiaHoy = findViewById<TextView>(R.id.tv_dia_hoy)
        tvDiaHoy.text = diaHoy;

        //Consultar Cuotas a Vencer
        listaCuotasAVencer = findViewById(R.id.tv_info_cuotas)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        dbHelper = DatabaseHelper(this)
        val cursorCuotas: Cursor? = dbHelper.consultarCuotasAVencer()
        updateListaCuotasAVencer(cursorCuotas)
    }

    private fun updateListaCuotasAVencer(cursor: Cursor?) {
        if (cursor != null && cursor.moveToFirst()) {
            // Update the listaCuotasAVencer with the new data
            do {
                val idCuota = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CUOTA_ID))
                val precioCuota = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CUOTA_PRECIO))
                val dniSocio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CUOTA_CLIENTE_DNI))

                val button = Button(this)

                button.setBackgroundResource(R.drawable.rounded_get) // android:background="@drawable/rounded_get"
                button.text = "" // android:text=""
                button.setTextColor(Color.BLACK)


                button.text = "id:$idCuota    $$precioCuota    DNI:$dniSocio "
                var layout_cuotas = findViewById<LinearLayout>(R.id.ll_lista_cuotas)
                layout_cuotas.addView(button)

            } while (cursor.moveToNext())
        } else {
            listaCuotasAVencer.visibility = View.VISIBLE
            listaCuotasAVencer.text = "Cuotas no encontradas"
        }
        cursor?.close()
    }



    private fun handleDniInput(dni: Int) {
        val cliente = dbHelper.getCliente(dni)
        if (cliente){

        val socio = dbHelper.getSocio(dni)
        if (socio) {
            cuotaImpaga = dbHelper.getCuotaImpaga(dni)
            if (cuotaImpaga != null){
                val tvCuota: TextView = findViewById(R.id.tv_cuota)
                tvCuota.text = "Cuota: ${cuotaImpaga?.idCuota} |  $${cuotaImpaga?.precio} | ${cuotaImpaga?.fechaVencimiento} "
                findViewById<LinearLayout>(R.id.ll_socio_info).visibility = View.VISIBLE
                findViewById<LinearLayout>(R.id.ll_no_socio_info).visibility = View.GONE
            }else{
                Toasty.success(this, "El socio no tiene cuotas sin pagar", Toast.LENGTH_SHORT).show()
            }


        } else {
            val actividades = dbHelper.getActividades()
            val spinner: Spinner = findViewById(R.id.sp_actividades)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, actividades)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            findViewById<LinearLayout>(R.id.ll_socio_info).visibility = View.GONE
            findViewById<LinearLayout>(R.id.ll_no_socio_info).visibility = View.VISIBLE

            // Ajustar la posición de la lista de cuotas
            val params = findViewById<LinearLayout>(R.id.ll_lista_cuotas).layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.BELOW, R.id.ll_no_socio_info)
            findViewById<LinearLayout>(R.id.ll_lista_cuotas).layoutParams = params
        }
        } else {
            Toasty.warning(this, "No existe cliente con este DNI", Toast.LENGTH_SHORT).show()
            findViewById<LinearLayout>(R.id.ll_socio_info).visibility = View.GONE
            findViewById<LinearLayout>(R.id.ll_no_socio_info).visibility = View.GONE
        }
    }

}