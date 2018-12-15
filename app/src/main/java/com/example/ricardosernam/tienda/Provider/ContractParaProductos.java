package com.example.ricardosernam.tienda.Provider;

import android.graphics.Color;
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


public static final String ESTADOS= "estados";



/**
 * Tipo MIME que retorna la consulta de una sola fila

public final static String SINGLE_MIME_CARRITO = "vnd.android.cursor.item/vnd." + AUTHORITY + CARRITO;

public final static String MULTIPLE_MIME_CARRITO = "vnd.android.cursor.dir/vnd." + AUTHORITY + CARRITO;

public final static String SINGLE_MIME_INVENTARIO = "vnd.android.cursor.item/vnd." + AUTHORITY + INVENTARIO;

public final static String MULTIPLE_MIME_INVENTARIO = "vnd.android.cursor.dir/vnd." + AUTHORITY + INVENTARIO;

public final static String SINGLE_MIME_INVENTARIO_DETALLE = "vnd.android.cursor.item/vnd." + AUTHORITY + INVENTARIO_DETALLE;

public final static String MULTIPLE_MIME_INVENTARIO_DETALLE = "vnd.android.cursor.dir/vnd." + AUTHORITY + INVENTARIO_DETALLE;

public final static String SINGLE_MIME_PRODUCTO = "vnd.android.cursor.item/vnd." + AUTHORITY + PRODUCTO;

public final static String MULTIPLE_MIME_PRODUCTO = "vnd.android.cursor.dir/vnd." + AUTHORITY + PRODUCTO;

    public final static String SINGLE_MIME_VENTA = "vnd.android.cursor.item/vnd." + AUTHORITY + VENTA;

    public final static String MULTIPLE_MIME_VENTA = "vnd.android.cursor.dir/vnd." + AUTHORITY + VENTA;

public final static String SINGLE_MIME_VENTA_DETALLE = "vnd.android.cursor.item/vnd." + AUTHORITY + VENTA_DETALLE;

public final static String MULTIPLE_MIME_VENTA_DETALLE = "vnd.android.cursor.dir/vnd." + AUTHORITY + VENTA_DETALLE;


/**
 * URI de contenido principal

public final static Uri CONTENT_URI_CARRITO = Uri.parse("content://" + AUTHORITY + "/" + CARRITO);

public final static Uri CONTENT_URI_INVENTARIO = Uri.parse("content://" + AUTHORITY + "/" + INVENTARIO);

public final static Uri CONTENT_URI_PRODUCTO = Uri.parse("content://" + AUTHORITY + "/" + PRODUCTO);

public final static Uri CONTENT_URI_INVENTARIO_DETALLE = Uri.parse("content://" + AUTHORITY + "/" + INVENTARIO_DETALLE);

public final static Uri CONTENT_URI_VENTA= Uri.parse("content://" + AUTHORITY + "/" + VENTA);

public final static Uri CONTENT_URI_VENTA_DETALLE = Uri.parse("content://" + AUTHORITY + "/" + VENTA_DETALLE);





 * Comparador de URIs de contenido
 */

/*public static final UriMatcher uriMatcherCarrito;
public static final UriMatcher uriMatcherProducto;
public static final UriMatcher uriMatcherInventario;
public static final UriMatcher uriMatcherInventarioDetalles;
public static final UriMatcher uriMatcherVenta;
public static final UriMatcher uriMatcherVentaDetalles;*/



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

        /*uriMatcherCarrito = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherCarrito.addURI(AUTHORITY, CARRITO, ALLROWS);
        uriMatcherCarrito.addURI(AUTHORITY, CARRITO + "/#", SINGLE_ROW);

        uriMatcherProducto = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherProducto.addURI(AUTHORITY, PRODUCTO, ALLROWS);
        uriMatcherProducto.addURI(AUTHORITY, PRODUCTO + "/#", SINGLE_ROW);

        uriMatcherInventario = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherInventario.addURI(AUTHORITY, INVENTARIO, ALLROWS);
        uriMatcherInventario.addURI(AUTHORITY, INVENTARIO + "/#", SINGLE_ROW);

        uriMatcherInventarioDetalles = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherInventarioDetalles.addURI(AUTHORITY, INVENTARIO_DETALLE, ALLROWS);
        uriMatcherInventarioDetalles.addURI(AUTHORITY, INVENTARIO_DETALLE + "/#", SINGLE_ROW);

        uriMatcherVenta = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherVenta.addURI(AUTHORITY, VENTA, ALLROWS);
        uriMatcherVenta.addURI(AUTHORITY, VENTA + "/#", SINGLE_ROW);*/
        ;
        }

// Valores para la columna ESTADO
public static final int ESTADO_OK = 0;
public static final int ESTADO_SYNC = 1;
public static final ArrayList<ProductosVenta_class> itemsProductosVenta= new ArrayList <>(); ///Arraylist que contiene los productos///
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



    public static final String ESTADO = "estado";
    public static final String ID_REMOTA = "idRemota";
    public final static String PENDIENTE_INSERCION = "pendiente_insercion";

}
}
