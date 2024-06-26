package com.example.clubdeportivo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_NAME = "ClubDeportivo.db"
        private const val DATABASE_VERSION = 3

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

            // CREACION Y CARGA DE DATOS TABLA ADMIN
            val createAdminTable = "CREATE TABLE $TABLE_ADMIN ($COLUMN_ADMIN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_ADMIN_USERNAME TEXT, $COLUMN_ADMIN_PASSWORD TEXT)"
            db.execSQL(createAdminTable)
            Log.d("Database", "Creacion tabla $TABLE_ADMIN")

            // CREACION Y CARGA DE DATOS TABLA CLIENTES
            val createClienteTable = "CREATE TABLE $TABLE_CLIENTES ($COLUMN_CLIENTE_DNI INTEGER PRIMARY KEY, " +
                    "$COLUMN_CLIENTE_NOMBRE TEXT, $COLUMN_CLIENTE_APELLIDO TEXT, $COLUMN_CLIENTE_APTO_FISICO INTEGER, $COLUMN_CLIENTE_ES_SOCIO INTEGER)"
            db.execSQL(createClienteTable)
            Log.d("Database", "Creacion tabla $TABLE_CLIENTES")

            // CREACION Y CARGA DE DATOS TABLA ACTIVIDADES
            val createActividadesTable = "CREATE TABLE $TABLE_ACTIVIDADES ($COLUMN_ACTIVIDAD_NOMBRE TEXT PRIMARY KEY, " +
                    "$COLUMN_ACTIVIDAD_PRECIO INTEGER)"
            db.execSQL(createActividadesTable)
            Log.d("Database", "Creacion tabla $TABLE_ACTIVIDADES")

            // CREACION Y CARGA DE DATOS TABLA CUOTAS
            val createCuotasTable = "CREATE TABLE $TABLE_CUOTAS ($COLUMN_CUOTA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_CUOTA_PRECIO INTEGER, $COLUMN_CUOTA_FECHA_VENC TEXT, $COLUMN_CUOTA_ESTADO INTEGER, $COLUMN_CUOTA_CLIENTE_DNI INTEGER)"
            db.execSQL(createCuotasTable)
            Log.d("Database", "Creacion tabla $TABLE_CUOTAS")

            // CREACION Y CARGA DE DATOS TABLA PAGOS
            val createPagosTable = "CREATE TABLE $TABLE_PAGOS ($COLUMN_PAGOS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_PAGOS_FECHA_PAGO TEXT, $COLUMN_PAGOS_MODALIDAD TEXT, $COLUMN_PAGOS_CUOTA_ID INTEGER, $COLUMN_PAGOS_ACTIVIDAD_NOMBRE TEXT)"
            db.execSQL(createPagosTable)
            Log.d("Database", "Creacion tabla $TABLE_PAGOS")

            // CARGA DATOS ADMIN
            val adminValues = ContentValues().apply {
                put(COLUMN_ADMIN_USERNAME, "a")
                put(COLUMN_ADMIN_PASSWORD, "z")
            }
            db.insert(TABLE_ADMIN, null, adminValues)
            Log.d("Database", "Admin data ok")

            // - CLIENTE SOCIO
            val cliente1Values = ContentValues().apply {
                put(COLUMN_CLIENTE_DNI, 36000000) // es socio
                put(COLUMN_CLIENTE_NOMBRE, "Pedro")
                put(COLUMN_CLIENTE_APELLIDO, "Naranja")
                put(COLUMN_CLIENTE_APTO_FISICO, 1)
                put(COLUMN_CLIENTE_ES_SOCIO, 1)
            }
            db.insert(TABLE_CLIENTES, null, cliente1Values)
            Log.d("Database", "Cliente data (Pedro)")

            // - NO SOCIO
            val cliente2Values = ContentValues().apply {
                put(COLUMN_CLIENTE_DNI, 37000000) // no es socio
                put(COLUMN_CLIENTE_NOMBRE, "Juana")
                put(COLUMN_CLIENTE_APELLIDO, "Azul")
                put(COLUMN_CLIENTE_APTO_FISICO, 1)
                put(COLUMN_CLIENTE_ES_SOCIO, 0)
            }
            db.insert(TABLE_CLIENTES, null, cliente2Values)
            Log.d("Database", "Cliente data (Juana)")

            // FUTBOL
            val act1Values = ContentValues().apply {
                put(COLUMN_ACTIVIDAD_NOMBRE, "Futbol")
                put(COLUMN_ACTIVIDAD_PRECIO, 1000)
            }
            db.insert(TABLE_ACTIVIDADES, null, act1Values)
            Log.d("Database", "Actividad Futbol")

            // VOLEY
            val act2Values = ContentValues().apply {
                put(COLUMN_ACTIVIDAD_NOMBRE, "Voley")
                put(COLUMN_ACTIVIDAD_PRECIO, 1000)
            }
            db.insert(TABLE_ACTIVIDADES, null, act2Values)
            Log.d("Database", "Actividad Voley")

            // NATACION
            val act3Values = ContentValues().apply {
                put(COLUMN_ACTIVIDAD_NOMBRE, "Natacion")
                put(COLUMN_ACTIVIDAD_PRECIO, 2000)
            }
            db.insert(TABLE_ACTIVIDADES, null, act3Values)
            Log.d("Database", "Actividad Natacion")

            // CUOTA 1 PAGA
            val cuota1Values = ContentValues().apply {
                put(COLUMN_CUOTA_PRECIO, 10000)
                put(COLUMN_CUOTA_FECHA_VENC, "20/05/2024")
                put(COLUMN_CUOTA_ESTADO, 1) // PAGO
                put(COLUMN_CUOTA_CLIENTE_DNI, 36000000) // SOCIO
            }
            db.insert(TABLE_CUOTAS, null, cuota1Values)
            Log.d("Database", "Cuota 1 (Paga)")

            // CUOTA 2 IMPAGA
            val cuota2Values = ContentValues().apply {
                put(COLUMN_CUOTA_PRECIO, 10000)
                put(COLUMN_CUOTA_FECHA_VENC, "26/06/2024")
                put(COLUMN_CUOTA_ESTADO, 0) // IMPAGO
                put(COLUMN_CUOTA_CLIENTE_DNI, 36000000) // SOCIO
            }
            db.insert(TABLE_CUOTAS, null, cuota2Values)
            Log.d("Database", "Cuota 2 (Impaga)")

            // PAGO 1
            val pago1Values = ContentValues().apply {
                put(COLUMN_PAGOS_FECHA_PAGO, "25/05/2024")
                put(COLUMN_PAGOS_MODALIDAD, "EFECTIVO")
                put(COLUMN_PAGOS_CUOTA_ID, 1) // SOCIO
                put(COLUMN_PAGOS_ACTIVIDAD_NOMBRE, "")
            }
            db.insert(TABLE_PAGOS, null, pago1Values)
            Log.d("Database", "Pago 1")

            // PAGO 2
            val pago2Values = ContentValues().apply {
                put(COLUMN_PAGOS_FECHA_PAGO, "25/05/2024")
                put(COLUMN_PAGOS_MODALIDAD, "3 CUOTAS")
                put(COLUMN_PAGOS_CUOTA_ID, 0) // NO SOCIO
                put(COLUMN_PAGOS_ACTIVIDAD_NOMBRE, "FUTBOL")
            }
            db.insert(TABLE_PAGOS, null, pago2Values)
            Log.d("Database", "Pago 2")

            // PAGO 3
            val pago3Values = ContentValues().apply {
                put(COLUMN_PAGOS_FECHA_PAGO, "25/05/2024")
                put(COLUMN_PAGOS_MODALIDAD, "1 CUOTA")
                put(COLUMN_PAGOS_CUOTA_ID, 0) // NO SOCIO
                put(COLUMN_PAGOS_ACTIVIDAD_NOMBRE, "VOLEY")
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
}
