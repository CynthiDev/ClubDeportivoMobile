package com.example.clubdeportivo.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.clubdeportivo.enums.Actividad
import com.example.clubdeportivo.enums.EstadoDePago
import com.example.clubdeportivo.enums.ModalidadDePago
import java.time.LocalDate

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_NAME = "ClubDeportivo.db"
        private const val DATABASE_VERSION = 1

        // TABLA ADMIN  :
        private const val TABLE_ADMIN = "Admin"
        private const val COLUMN_ADMIN_ID = "idAdmin"
        private const val COLUMN_ADMIN_USERNAME = "username"
        private const val COLUMN_ADMIN_PASSWORD = "password"

        // TABLA CLIENTES:
        private const val TABLE_CLIENTES = "Cliente"
        private const val COLUMN_CLIENTE_DNI = "dni"
        private const val COLUMN_CLIENTE_NOMBRE = "nombre"
        private const val COLUMN_CLIENTE_APELLIDO = "apellido"
        private const val COLUMN_CLIENTE_APTO_FISICO = "aptoFisico" // BOOLEAN
        private const val COLUMN_CLIENTE_ES_SOCIO = "esSocio" // BOOLEAN

        // TABLA ACTIVIDADES CLUB:
        private const val TABLE_ACTIVIDADES = "Actividad"
        private const val COLUMN_ACTIVIDAD_NOMBRE = "nombre"
        private const val COLUMN_ACTIVIDAD_PRECIO = "precio"

        // TABLA CUOTAS:
        private const val TABLE_CUOTAS = "Cuota"
        private const val COLUMN_CUOTA_ID = "id"
        private const val COLUMN_CUOTA_PRECIO = "precio" // precio fijo mensual $10000
        private const val COLUMN_CUOTA_FECHA_VENC = "fechaVenc" // 30 dias a la fecha de pago o 10 de cada mes
        private const val COLUMN_CUOTA_ESTADO = "estado" // PAGO O IMPAGO
        private const val COLUMN_CUOTA_CLIENTE_DNI = "dniSocio"

        // TABLA PAGOS:
        private const val TABLE_PAGOS = "Pago"
        private const val COLUMN_PAGOS_ID = "id" // sea cuota o actividad
        private const val COLUMN_PAGOS_FECHA_PAGO = "fechaPago"
        private const val COLUMN_PAGOS_MODALIDAD = "modalidad" // EFECTIVO, 1 CUOTA O 3 CUOTAS
        private const val COLUMN_PAGOS_CUOTA_ID = "idCuota"
        private const val COLUMN_PAGOS_ACTIVIDAD_NOMBRE = "nombreActividad"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("Database", "onCreate")

        try {
            db.beginTransaction()

            // CREACION
            //TABLA ADMIN
            val createAdminTable = "CREATE TABLE $TABLE_ADMIN ($COLUMN_ADMIN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_ADMIN_USERNAME TEXT UNIQUE, $COLUMN_ADMIN_PASSWORD TEXT)"
            db.execSQL(createAdminTable)
            Log.d("Database", "Creacion tabla $TABLE_ADMIN")

            //TABLA CLIENTES
            val createClienteTable = "CREATE TABLE $TABLE_CLIENTES ($COLUMN_CLIENTE_DNI INTEGER PRIMARY KEY, " +
                    "$COLUMN_CLIENTE_NOMBRE TEXT, $COLUMN_CLIENTE_APELLIDO TEXT, $COLUMN_CLIENTE_APTO_FISICO INTEGER, $COLUMN_CLIENTE_ES_SOCIO INTEGER)"
            db.execSQL(createClienteTable)
            Log.d("Database", "Creacion tabla $TABLE_CLIENTES")

            //TABLA ACTIVIDADES
            val createActividadesTable = "CREATE TABLE $TABLE_ACTIVIDADES ($COLUMN_ACTIVIDAD_NOMBRE TEXT PRIMARY KEY, " +
                    "$COLUMN_ACTIVIDAD_PRECIO INTEGER)"
            db.execSQL(createActividadesTable)
            Log.d("Database", "Creacion tabla $TABLE_ACTIVIDADES")

            //TABLA CUOTAS
            val createCuotasTable = "CREATE TABLE $TABLE_CUOTAS ($COLUMN_CUOTA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_CUOTA_PRECIO INTEGER, $COLUMN_CUOTA_FECHA_VENC TEXT, $COLUMN_CUOTA_ESTADO INTEGER, $COLUMN_CUOTA_CLIENTE_DNI INTEGER)"
            db.execSQL(createCuotasTable)
            Log.d("Database", "Creacion tabla $TABLE_CUOTAS")

            //TABLA PAGOS
            val createPagosTable = "CREATE TABLE $TABLE_PAGOS ($COLUMN_PAGOS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_PAGOS_FECHA_PAGO TEXT, $COLUMN_PAGOS_MODALIDAD TEXT, $COLUMN_PAGOS_CUOTA_ID INTEGER, $COLUMN_PAGOS_ACTIVIDAD_NOMBRE TEXT)"
            db.execSQL(createPagosTable)
            Log.d("Database", "Creacion tabla $TABLE_PAGOS")



            // CARGA DATOS
            // ADMIN
            val adminValues = ContentValues().apply {
                put(COLUMN_ADMIN_USERNAME, "a")
                put(COLUMN_ADMIN_PASSWORD, "z")
            }
            db.insert(TABLE_ADMIN, null, adminValues)
            Log.d("Database", "Admin data ok")

            //SOCIO
            val cliente1Values = ContentValues().apply {
                put(COLUMN_CLIENTE_DNI, 36000000) // es socio
                put(COLUMN_CLIENTE_NOMBRE, "Pedro")
                put(COLUMN_CLIENTE_APELLIDO, "Naranja")
                put(COLUMN_CLIENTE_APTO_FISICO, 1)
                put(COLUMN_CLIENTE_ES_SOCIO, 1)
            }
            db.insert(TABLE_CLIENTES, null, cliente1Values)
            Log.d("Database", "Cliente data (Pedro)")

            //NO SOCIO
            val cliente2Values = ContentValues().apply {
                put(COLUMN_CLIENTE_DNI, 37000000) // no es socio
                put(COLUMN_CLIENTE_NOMBRE, "Juana")
                put(COLUMN_CLIENTE_APELLIDO, "Azul")
                put(COLUMN_CLIENTE_APTO_FISICO, 1)
                put(COLUMN_CLIENTE_ES_SOCIO, 0)
            }
            db.insert(TABLE_CLIENTES, null, cliente2Values)
            Log.d("Database", "Cliente data (Juana)")

            // ACTIVIDAD FUTBOL
            val act1Values = ContentValues().apply {
                put(COLUMN_ACTIVIDAD_NOMBRE, Actividad.FUTBOL.toString())
                put(COLUMN_ACTIVIDAD_PRECIO, 1000)
            }
            db.insert(TABLE_ACTIVIDADES, null, act1Values)
            Log.d("Database", "Actividad Futbol")

            // ACTIVIDAD VOLEY
            val act2Values = ContentValues().apply {
                put(COLUMN_ACTIVIDAD_NOMBRE, Actividad.VOLEY.toString())
                put(COLUMN_ACTIVIDAD_PRECIO, 1000)
            }
            db.insert(TABLE_ACTIVIDADES, null, act2Values)
            Log.d("Database", "Actividad Voley")

            //ACTIVIDAD  NATACION
            val act3Values = ContentValues().apply {
                put(COLUMN_ACTIVIDAD_NOMBRE, Actividad.NATACION.toString())
                put(COLUMN_ACTIVIDAD_PRECIO, 2000)
            }
            db.insert(TABLE_ACTIVIDADES, null, act3Values)
            Log.d("Database", "Actividad Natacion")

            // CUOTA 1 PAGA
            val cuota1Values = ContentValues().apply {
                put(COLUMN_CUOTA_PRECIO, 10000)
                put(COLUMN_CUOTA_FECHA_VENC, "20/05/2024")
                put(COLUMN_CUOTA_ESTADO, EstadoDePago.PAGO.toString())
                put(COLUMN_CUOTA_CLIENTE_DNI, 36000000) // SOCIO
            }
            db.insert(TABLE_CUOTAS, null, cuota1Values)
            Log.d("Database", "Cuota 1 (Paga)")

            // CUOTA 2 IMPAGA
            val cuota2Values = ContentValues().apply {
                put(COLUMN_CUOTA_PRECIO, 10000)
                put(COLUMN_CUOTA_FECHA_VENC, "26/06/2024")
                put(COLUMN_CUOTA_ESTADO, EstadoDePago.IMPAGO.toString())
                put(COLUMN_CUOTA_CLIENTE_DNI, 36000000) // SOCIO
            }
            db.insert(TABLE_CUOTAS, null, cuota2Values)
            Log.d("Database", "Cuota 2 (Impaga)")

            // PAGO 1
            val pago1Values = ContentValues().apply {
                put(COLUMN_PAGOS_FECHA_PAGO, "25/05/2024")
                put(COLUMN_PAGOS_MODALIDAD, ModalidadDePago.EFECTIVO.descripcion)
                put(COLUMN_PAGOS_CUOTA_ID, 1) // SOCIO
                put(COLUMN_PAGOS_ACTIVIDAD_NOMBRE, "")
            }
            db.insert(TABLE_PAGOS, null, pago1Values)
            Log.d("Database", "Pago 1")

            // PAGO 2
            val pago2Values = ContentValues().apply {
                put(COLUMN_PAGOS_FECHA_PAGO, "25/05/2024")
                put(COLUMN_PAGOS_MODALIDAD, ModalidadDePago.TRES_CUOTAS.descripcion)
                put(COLUMN_PAGOS_CUOTA_ID, 0) // NO SOCIO
                put(COLUMN_PAGOS_ACTIVIDAD_NOMBRE, Actividad.FUTBOL.toString())
            }
            db.insert(TABLE_PAGOS, null, pago2Values)
            Log.d("Database", "Pago 2")

            // PAGO 3
            val pago3Values = ContentValues().apply {
                put(COLUMN_PAGOS_FECHA_PAGO, "25/05/2024")
                put(COLUMN_PAGOS_MODALIDAD, ModalidadDePago.UNA_CUOTA.descripcion)
                put(COLUMN_PAGOS_CUOTA_ID, 0) // NO SOCIO
                put(COLUMN_PAGOS_ACTIVIDAD_NOMBRE, Actividad.VOLEY.toString())
            }
            db.insert(TABLE_PAGOS, null, pago3Values)
            Log.d("Database", "Pago 3")

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // ACTUALIZACION EN CASO DE CAMBIOS
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVIDADES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUOTAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAGOS")
        onCreate(db)
    }

    // LOGIN
    fun validateAdmin(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ADMIN WHERE $COLUMN_ADMIN_USERNAME = ? AND $COLUMN_ADMIN_PASSWORD = ?", arrayOf(username, password))
        val isValid = cursor.count > 0
        cursor.close()
        db.close()
        return isValid
    }


    // ACTIVIDADES LIST
    fun getAllActivitys(): Array<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_ACTIVIDAD_NOMBRE FROM $TABLE_ACTIVIDADES", null)
        val opciones = mutableListOf<String>()

        // Recorrer el cursor y agregar los nombres de las actividades al array
        if (cursor.moveToFirst()) {
            do {
                opciones.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()

        return opciones.toTypedArray()
    }


    //ACTUALIZACIONES EN LA DDBB
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertarCliente(dni: Int, nombre: String, apellido: String, esSocio: Boolean, modalidadPago: String, actividad: String?) : Boolean{

        val db = this.writableDatabase
        val cursor = db.query(TABLE_CLIENTES, arrayOf(COLUMN_CLIENTE_DNI), "$COLUMN_CLIENTE_DNI = ?", arrayOf(dni.toString()), null, null, null)
        if (cursor.count > 0) {
            cursor.close()
            return false
        }
        val values = ContentValues()
        values.put(COLUMN_CLIENTE_DNI, dni)
        values.put(COLUMN_CLIENTE_NOMBRE, nombre)
        values.put(COLUMN_CLIENTE_APELLIDO, apellido)
        values.put(COLUMN_CLIENTE_APTO_FISICO, 1) //se ha cargado el certificado
        values.put(COLUMN_CLIENTE_ES_SOCIO, if (esSocio) 1 else 0)
        db.insert(TABLE_CLIENTES, null, values)

        //agrego  cuota o pago
        if (esSocio){
            val cuotaId = insertarCuota(dni)
        }else{
            insertarPago(esSocio,null, modalidadPago, actividad)
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertarPago(esSocio: Boolean, cuotaID: Int?, modalidadPago: String, actividad: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PAGOS_FECHA_PAGO, Utils.formatDateString(LocalDate.now().toString())) // fecha actual
        values.put(COLUMN_PAGOS_MODALIDAD, modalidadPago)
        values.put(COLUMN_PAGOS_CUOTA_ID, if (esSocio)cuotaID else null)
        values.put(COLUMN_PAGOS_ACTIVIDAD_NOMBRE, if (!esSocio)actividad else null)  //todo: acividad no relacionada al NoSocio
        db.insert(TABLE_PAGOS, null, values)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertarCuota(dni: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CUOTA_PRECIO, 15000)
        values.put(COLUMN_CUOTA_FECHA_VENC, Utils.formatDateString(LocalDate.now().plusDays(30).toString())) // 30 días posteriores a la fecha actual
        values.put(COLUMN_CUOTA_ESTADO, EstadoDePago.IMPAGO.toString())
        values.put(COLUMN_CUOTA_CLIENTE_DNI, dni)
        val cuotaId = db.insert(TABLE_CUOTAS, null, values)
        return cuotaId.toInt()
    }

}