package com.example.ricardosernam.tienda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ricardosernam.tienda.Provider.ContractParaProductos;


/**
 * Clase envoltura para el gestor de Bases de datos
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase database) {
        productos(database);
    }

    public static void productos(SQLiteDatabase database) {
        String cmd0 = "CREATE TABLE " + ContractParaProductos.EMPLEADOS + " (" +
                ContractParaProductos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaProductos.Columnas.NOMBRE_EMPLEADO + " TEXT, " +
                ContractParaProductos.Columnas.TIPO_EMPLEADO + " TEXT, " +
                ContractParaProductos.Columnas.CODIGO+ " VARCHAR(45), " +
                ContractParaProductos.Columnas.ACTIVO + " INTEGER, " +
                ContractParaProductos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaProductos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaProductos.ESTADO_OK+"," +
                ContractParaProductos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd0);

        database.execSQL("INSERT INTO empleados (nombre_empleado, tipo_empleado, activo) values ('Juan', 'Cajero', 1)");
        database.execSQL("INSERT INTO empleados (nombre_empleado, tipo_empleado, activo) values ('María', 'Cerrillo',0)");

        String cmd4 = "CREATE TABLE " + ContractParaProductos.VENTAS + " (" +
                ContractParaProductos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaProductos.Columnas.ID_EMPLEADO + " INT, " +
                ContractParaProductos.Columnas.FECHA + " TEXT, " +
                ContractParaProductos.Columnas.ID_REMOTA + " TEXT," +
                ContractParaProductos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaProductos.ESTADO_OK+"," +
                ContractParaProductos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd4);

        String cmd5 = "CREATE TABLE " + ContractParaProductos.VENTA_DETALLES + " (" +
                ContractParaProductos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaProductos.Columnas.ID_VENTA + " INT, " +
                ContractParaProductos.Columnas.ID_PRODUCTO+ " INT, " +
                ContractParaProductos.Columnas.PRECIO + " DOUBLE, " +
                ContractParaProductos.Columnas.CANTIDAD + " DOUBLE, " +
                ContractParaProductos.Columnas.ID_REMOTA + " TEXT," +
                ContractParaProductos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaProductos.ESTADO_OK+"," +
                ContractParaProductos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd5);

        String cmd = "CREATE TABLE " + ContractParaProductos.INVENTARIO + " (" +
                ContractParaProductos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaProductos.Columnas.NOMBRE_PRODUCTO + " VARCHAR(45), " +
                ContractParaProductos.Columnas.PRECIO + " DOUBLE, " +
                ContractParaProductos.Columnas.CODIGO_BARRAS + " VARCHAR(45), " +
                ContractParaProductos.Columnas.EXISTENTES + "DOUBLE," +
                ContractParaProductos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaProductos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaProductos.ESTADO_OK+"," +
                ContractParaProductos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);

        database.execSQL("INSERT INTO inventario (nombre_producto, precio, codigo_barras) values ('Jitomate', 15.0, null)");
        database.execSQL("INSERT INTO inventario (nombre_producto, precio, codigo_barras) values ('Atun', 10.0 , '097339000054')");

        String cmd2 = "CREATE TABLE " + ContractParaProductos.INFORMACION + " (" +
                ContractParaProductos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaProductos.Columnas.NOMBRE_NEGOCIO + " TEXT, " +
                ContractParaProductos.Columnas.DIRECCION + " VARCHAR(45), " +
                ContractParaProductos.Columnas.TELEFONO + " VARCHAR(45)," +

                ContractParaProductos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaProductos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaProductos.ESTADO_OK+"," +
                ContractParaProductos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd2);
    }

    //@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            limpiar(db);
            onCreate(db);
    }

    public static void limpiar(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + ContractParaProductos.VENTA_DETALLES);
        db.execSQL("drop table if exists " + ContractParaProductos.VENTAS);
        db.execSQL("drop table if exists " + ContractParaProductos.INVENTARIO);
        db.execSQL("drop table if exists " + ContractParaProductos.EMPLEADOS);
        db.execSQL("drop table if exists " + ContractParaProductos.INFORMACION);
        productos(db);
    }

}
