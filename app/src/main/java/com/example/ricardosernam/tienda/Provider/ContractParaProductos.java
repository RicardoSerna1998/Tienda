package com.example.ricardosernam.tienda.Provider;

import android.content.UriMatcher;
import android.graphics.Color;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.Ventas.Historial.Historial_class;
import com.example.ricardosernam.tienda.Ventas.ProductosVenta_class;

import java.util.ArrayList;

/**
 * Contract Class entre el provider y las aplicaciones
 */
public class ContractParaProductos {
    /**
     * Autoridad del Content Provider
     */
    public static final String DATABASE_NAME = "db_tacos.db";

    public static final int DATABASE_VERSION = 1;

    public final static String AUTHORITY = "com.example.ricardosernam.puntodeventa";


    public static final String EMPLEADOS = "empleados";

    public static final String VENTAS = "ventas";

    public static final String VENTA_DETALLES = "venta_detalles";

    public static final String INVENTARIO = "inventario";

    public static final String INFORMACION = "informacion";

    public static final String TURNOS = "turnos";

    public static final String ESTADOS= "estados";


public final static String SINGLE_MIME_CARRITO = "vnd.android.cursor.item/vnd." + AUTHORITY + EMPLEADOS;

public final static String MULTIPLE_MIME_CARRITO = "vnd.android.cursor.dir/vnd." + AUTHORITY + EMPLEADOS;

public final static String SINGLE_MIME_INVENTARIO = "vnd.android.cursor.item/vnd." + AUTHORITY + INVENTARIO;

public final static String MULTIPLE_MIME_INVENTARIO = "vnd.android.cursor.dir/vnd." + AUTHORITY + INVENTARIO;

public final static String SINGLE_MIME_INVENTARIO_DETALLE = "vnd.android.cursor.item/vnd." + AUTHORITY + VENTAS;

public final static String MULTIPLE_MIME_INVENTARIO_DETALLE = "vnd.android.cursor.dir/vnd." + AUTHORITY + VENTAS;

public final static String SINGLE_MIME_PRODUCTO = "vnd.android.cursor.item/vnd." + AUTHORITY + VENTA_DETALLES;

public final static String MULTIPLE_MIME_PRODUCTO = "vnd.android.cursor.dir/vnd." + AUTHORITY + VENTA_DETALLES;

    public final static String SINGLE_MIME_VENTA = "vnd.android.cursor.item/vnd." + AUTHORITY + INFORMACION;

    public final static String MULTIPLE_MIME_VENTA = "vnd.android.cursor.dir/vnd." + AUTHORITY + INFORMACION;

public final static String SINGLE_MIME_VENTA_DETALLE = "vnd.android.cursor.item/vnd." + AUTHORITY + TURNOS;

public final static String MULTIPLE_MIME_VENTA_DETALLE = "vnd.android.cursor.dir/vnd." + AUTHORITY + TURNOS;





public final static Uri CONTENT_URI_EMPLEADOS = Uri.parse("content://" + AUTHORITY + "/" + EMPLEADOS);

public final static Uri CONTENT_URI_INVENTARIO = Uri.parse("content://" + AUTHORITY + "/" + INVENTARIO);

public final static Uri CONTENT_URI_VENTA = Uri.parse("content://" + AUTHORITY + "/" + VENTAS);

public final static Uri CONTENT_URI_VENTA_DETALLE = Uri.parse("content://" + AUTHORITY + "/" + VENTA_DETALLES);

public final static Uri CONTENT_URI_INFORMACION= Uri.parse("content://" + AUTHORITY + "/" + INFORMACION);

public final static Uri CONTENT_URI_TURNOS = Uri.parse("content://" + AUTHORITY + "/" + TURNOS);







public static final UriMatcher uriMatcherEmpleado;
public static final UriMatcher uriMatcherInventario;
public static final UriMatcher uriMatcherInformacion;
public static final UriMatcher uriMatcherTurnos;
public static final UriMatcher uriMatcherVenta;
public static final UriMatcher uriMatcherVentaDetalles;



/**
 * Código para URIs de multiples registros
 */
public static final int ALLROWS = 1;
/**
 * Código para URIS de un solo registro
 */
public static final int SINGLE_ROW = 2;


// Asignación de URIs
static {

        uriMatcherEmpleado = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherEmpleado.addURI(AUTHORITY, EMPLEADOS, ALLROWS);
        uriMatcherEmpleado.addURI(AUTHORITY, EMPLEADOS + "/#", SINGLE_ROW);

        uriMatcherInformacion = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherInformacion.addURI(AUTHORITY, INFORMACION, ALLROWS);
        uriMatcherInformacion.addURI(AUTHORITY, INFORMACION + "/#", SINGLE_ROW);

        uriMatcherInventario = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherInventario.addURI(AUTHORITY, INVENTARIO, ALLROWS);
        uriMatcherInventario.addURI(AUTHORITY, INVENTARIO + "/#", SINGLE_ROW);

        uriMatcherTurnos = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherTurnos.addURI(AUTHORITY, TURNOS, ALLROWS);
        uriMatcherTurnos.addURI(AUTHORITY, TURNOS + "/#", SINGLE_ROW);

        uriMatcherVenta = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherVenta.addURI(AUTHORITY, VENTAS, ALLROWS);
        uriMatcherVenta.addURI(AUTHORITY, VENTAS + "/#", SINGLE_ROW);

    uriMatcherVentaDetalles = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcherVentaDetalles.addURI(AUTHORITY, VENTA_DETALLES, ALLROWS);
    uriMatcherVentaDetalles.addURI(AUTHORITY, VENTA_DETALLES + "/#", SINGLE_ROW);
        ;
        }

// Valores para la columna ESTADO
public static final int ESTADO_OK = 0;
public static final int ESTADO_SYNC = 1;
public static ArrayList<ProductosVenta_class> itemsProductosVenta= new ArrayList <>(); ///Arraylist que contiene los productos///
    public static final int rojo = Color.parseColor("#FFF62D2D");
    public static final int verde = Color.parseColor("#FF0AEA45");


/**
 * Estructura de la tabla
 *
 *
 */
public static class Columnas implements BaseColumns {

    private Columnas() {
        // Sin instancias
    }
//////////////empleados//////////////////
    public final static String NOMBRE_EMPLEADO = "nombre_empleado";
    public final static String TIPO_EMPLEADO = "tipo_empleado";
    public final static String CODIGO = "codigo";
    public final static String ACTIVO = "activo";


    /////////////////////ventas///////////////////7
    public final static String ID_EMPLEADO= "id_empleado";
    public final static String FECHA = "fecha";

    /////////////////////venta detalles///////////////////7
    public final static String CANTIDAD = "cantidad";


    /////////////////inventario
    public final static String ID_PRODUCTO = "id_producto";
    public final static String NOMBRE_PRODUCTO = "nombre_producto";
    public final static String PRECIO = "precio";
    public final static String CODIGO_BARRAS = "codigo_barras";
    public final static String EXISTENTES = "existentes";


    ////////////////////informacion////////////
    public final static String NOMBRE_NEGOCIO = "nombre_negocio";
    public final static String DIRECCION = "direccion";
    public final static String TELEFONO = "telefono";

    ////////////turnos///////////////////77
    public final static String HORA_INICIO = "nombre_producto";
    public final static String HORA_FIN = "precio";


    public static final String ESTADO = "estado";
    public static final String ID_REMOTA = "idRemota";
    public final static String PENDIENTE_INSERCION = "pendiente_insercion";

}
}
