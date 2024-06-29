package com.example.clubdeportivo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
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
import es.dmoral.toasty.Toasty

class PayMethodActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var cuotaImpaga : CuotaImpaga
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_method)
        dbHelper = DatabaseHelper(this)

        val cuotaImpagaID = intent.getIntExtra("cuotaID", 0)

        //PAGAR CUOTA
        setupPaymentButtons(cuotaImpagaID)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPaymentButtons(cuotaImpagaID: Int) {
        val paymentMethods = mapOf(
            R.id.EFECTIVO to ModalidadDePago.EFECTIVO.descripcion,
            R.id.UNA_CUOTA to ModalidadDePago.UNA_CUOTA.descripcion,
            R.id.TRES_CUOTAS to ModalidadDePago.TRES_CUOTAS.descripcion
        )

        paymentMethods.forEach { (buttonId, paymentDescription) ->
            findViewById<Button>(buttonId).setOnClickListener {
                handlePayment(cuotaImpagaID, paymentDescription)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlePayment(cuotaImpagaID: Int, metodoPago: String) {
        dbHelper.pagarCuota(cuotaImpagaID, metodoPago)
        Toasty.success(this, "Pago realizado correctamente", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, StartActivity::class.java))
    }
}