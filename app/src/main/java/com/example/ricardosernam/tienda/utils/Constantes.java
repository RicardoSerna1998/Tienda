package com.example.ricardosernam.tienda.utils;

/**
 * Constantes
 */
public class Constantes {

    public static  String GET_URL_EMPLEADOS;

    public static  String GET_URL_INFORMACION;

    public static  String UPDATE_URL_INVENTARIO;

    public static String GET_URL_INVENTARIO;   //////

    public static String INSERT_URL_VENTA;

    public static String INSERT_URL_VENTA_DETALLE;


////TURNO (DUDA)
    public static String GET_URL_TURNO;  ///en duda

    public static String UPDATE_URL_TURNO;   ////Este si


    //private static final String PUERTO_HOST = "";

    private static final String PUERTO_HOST = ":82";

    public Constantes(String ip){
        GET_URL_EMPLEADOS = ip + PUERTO_HOST + "/Eduardo/empleados/obtener_empleados.php";

        GET_URL_INFORMACION= ip + PUERTO_HOST + "/Eduardo/productos/obtener_productos.php";

        GET_URL_INVENTARIO = ip + PUERTO_HOST + "/Eduardo/inventarios/obtener_inventarios.php?idcarrito=";   //////

        UPDATE_URL_INVENTARIO = ip + PUERTO_HOST + "/Eduardo/inventarios/actualizar_inventario.php?idinventario=";   //////

        INSERT_URL_VENTA = ip + PUERTO_HOST + "/Eduardo/ventas/insertar_venta.php";

       INSERT_URL_VENTA_DETALLE = ip + PUERTO_HOST + "/Eduardo/venta_detalles/insertar_venta_detalle.php";

        GET_URL_TURNO = ip + PUERTO_HOST + "/Eduardo/inventario_detalles/obtener_inventario_detalles.php?idinventario=";

        UPDATE_URL_TURNO = ip + PUERTO_HOST + "/Eduardo/inventario_detalles/actualizar_inventario_detalle.php";
    }

    public static final String CARRITO = "carrito";
    public static final String EMPLEADO = "empleado";




    public static final String ID_PRODUCTO = "idproducto";
    public static final String PRODUCTO = "producto";

    public static final String ID_INVENTARIO = "idinventario";
    public static final String INVENTARIO = "inventario";

    public static final String ID_INVENTARIO_DETALLE = "idinventario";//////////////id del localhost
    public static final String INVENTARIO_DETALLE = "inventario_detalle";

    public static final String ID_VENTA = "idventa";
    public static final String ESTADO = "estado";
    public static final String MENSAJE = "mensaje";

    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    /**
     * Tipo de cuenta para la sincronización
     */
    public static final String ACCOUNT_TYPE = "com.example.ricardosernam.tienda.account";
}
