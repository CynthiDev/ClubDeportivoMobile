package com.example.clubdeportivo

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.Database.Companion.COLUMN_CLIENTE_NOMBRE
import com.example.clubdeportivo.Database.Companion.COLUMN_CUOTA_CLIENTE_DNI
import com.example.clubdeportivo.Database.Companion.COLUMN_CUOTA_FECHA_VENC
import com.example.clubdeportivo.Database.Companion.COLUMN_CUOTA_ID
import com.example.clubdeportivo.Database.Companion.COLUMN_CUOTA_PRECIO
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class PaymentActivity : AppCompatActivity() {
    private lateinit var dbHelper: Database
    private lateinit var listaCuotasAVencer : TextView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = Database(this)

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
            startActivity(intent)
        }

        val btnPagarNoSocio: Button = findViewById(R.id.btnPagarNoSocio)
        btnPagarNoSocio.setOnClickListener {
            val intent = Intent(this, PayMethodActivity::class.java)
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
                    Toast.makeText(this, "Por favor, ingrese un número válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingrese un DNI", Toast.LENGTH_SHORT).show()
            }
        }

        val cursorCuotas: Cursor? = dbHelper.consultarCuotasAVencer()

        listaCuotasAVencer = findViewById(R.id.tv_info_cuotas)

        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fecha = LocalDate.now()
        val diaHoy = fecha.format(formato)
        val tvDiaHoy = findViewById<TextView>(R.id.tv_dia_hoy)
        tvDiaHoy.text = diaHoy;

        if(cursorCuotas != null && cursorCuotas.moveToFirst()) {
            do{
                val idCuota = cursorCuotas.getString(cursorCuotas.getColumnIndexOrThrow(COLUMN_CUOTA_ID))
                val precioCuota = cursorCuotas.getString(cursorCuotas.getColumnIndexOrThrow(COLUMN_CUOTA_PRECIO))
                val dniSocio = cursorCuotas.getString(cursorCuotas.getColumnIndexOrThrow(COLUMN_CUOTA_CLIENTE_DNI))

                val button = Button(this)

                button.setBackgroundResource(R.drawable.rounded_get) // android:background="@drawable/rounded_get"
                button.text = "" // android:text=""
                button.setTextColor(Color.BLACK)


                button.text = "id:$idCuota    $$precioCuota    DNI:$dniSocio "
                var layout_cuotas = findViewById<LinearLayout>(R.id.ll_lista_cuotas)
                layout_cuotas.addView(button)

            } while (cursorCuotas.moveToNext())
        }
        else {
            listaCuotasAVencer.visibility = View.VISIBLE
            listaCuotasAVencer.text = "Cuotas no encontradas"
        }
        cursorCuotas?.close()
        }

    private fun handleDniInput(dni: Int) {
        val socio = dbHelper.isSocio(dni)
        if (socio) {
            val cuotaImpaga = dbHelper.getCuotaImpaga(dni)
            val tvCuota: TextView = findViewById(R.id.tv_cuota)
            tvCuota.text = cuotaImpaga
            findViewById<LinearLayout>(R.id.ll_socio_info).visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.ll_no_socio_info).visibility = View.GONE
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
    }

}