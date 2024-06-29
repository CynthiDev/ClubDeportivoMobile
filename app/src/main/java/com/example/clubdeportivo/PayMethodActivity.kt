package com.example.clubdeportivo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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

        // Obtener los extras
        val cuotaImpagaID = intent.getIntExtra("cuotaID", -1)
        val actividadName = intent.getStringExtra("actividadName")


        if (actividadName != null){
            //PAGAR ACTIVIDAD
            setupPaymentButtonsPagarActividad(actividadName)
        }else if (cuotaImpagaID != -1){
            //PAGAR CUOTA
            setupPaymentButtonsPagarCuota(cuotaImpagaID)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPaymentButtonsPagarCuota(cuotaImpagaID: Int) {
        val paymentMethods = mapOf(
            R.id.EFECTIVO to ModalidadDePago.EFECTIVO.descripcion,
            R.id.UNA_CUOTA to ModalidadDePago.UNA_CUOTA.descripcion,
            R.id.TRES_CUOTAS to ModalidadDePago.TRES_CUOTAS.descripcion
        )

        paymentMethods.forEach { (buttonId, paymentDescription) ->
            findViewById<Button>(buttonId).setOnClickListener {
                pagarCuota(cuotaImpagaID, paymentDescription)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun pagarCuota(cuotaImpagaID: Int, metodoPago: String) {
        dbHelper.pagarCuota(cuotaImpagaID, metodoPago)
        Toasty.success(this, "Pago de la cuota: ${cuotaImpagaID} realizado correctamente", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, PaymentActivity::class.java))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPaymentButtonsPagarActividad(actividad: String) {
        val paymentMethods = mapOf(
            R.id.EFECTIVO to ModalidadDePago.EFECTIVO.descripcion,
            R.id.UNA_CUOTA to ModalidadDePago.UNA_CUOTA.descripcion,
            R.id.TRES_CUOTAS to ModalidadDePago.TRES_CUOTAS.descripcion
        )

        paymentMethods.forEach { (buttonId, paymentDescription) ->
            findViewById<Button>(buttonId).setOnClickListener {
                pagarActiviadad(actividad, paymentDescription)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun pagarActiviadad(actividad: String, metodoPago: String) {
        dbHelper.pagarActividad(metodoPago,actividad)
        Toasty.success(this, "Pago de la actividad realizado correctamente", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, PaymentActivity::class.java))
    }
}