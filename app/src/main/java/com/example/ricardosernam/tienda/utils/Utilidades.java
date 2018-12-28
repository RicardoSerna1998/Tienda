package com.example.ricardosernam.tienda.utils;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

public class Utilidades {
    private static final int COLUMNA_ID_REMOTA_INVENTARIO_DETALLE = 4;
    private static final int COLUMNA_ID_PRODUCTO_INVENTARIO_DETALLE = 1;
    private static final int COLUMNA_EXISTENTE_INICIAL = 2;
    private static final int COLUMNA_EXISTENTE_FINAL = 3;

    private static final int COLUMNA_ID_CARRITO_VENTA = 1;
    private static final int COLUMNA_ID_INVENTARIO_VENTA = 2;
    private static final int COLUMNA_FECHA_VENTA = 3;
    private static final int COLUMNA_UBICACIÓN_VENTA = 4;
    private static final int COLUMNA_VENDEDOR_VENTA = 5;

    private static final int COLUMNA_ID_REMOTA_VENTA_DETALLE = 4;
    private static final int COLUMNA_CANTIDAD = 1;
    private static final int COLUMNA_ID_PRODUCTO_VENTA_DETALLE = 3;
    private static final int COLUMNA_PRECIO = 2;


    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}

    /**
     * Copia los datos de un gasto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason

    public static JSONObject deCursorAJSONObject(Cursor c, String url) {
        JSONObject jObject = new JSONObject();

        if (url.equals(Constantes.UPDATE_URL_INVENTARIO)) {

            try {
                jObject.put(ContractParaProductos.Columnas.DISPONIBLE, 0);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (url.equals(Constantes.UPDATE_URL_INVENTARIO_DETALLE)) {
            int idinventario;
            int idproducto;
            Double existente_inicial;
            Double existente_final;


            idinventario = c.getInt(COLUMNA_ID_REMOTA_INVENTARIO_DETALLE);
            idproducto = c.getInt(COLUMNA_ID_PRODUCTO_INVENTARIO_DETALLE);
            existente_inicial = c.getDouble(COLUMNA_EXISTENTE_INICIAL);
            existente_final = c.getDouble(COLUMNA_EXISTENTE_FINAL);


            try {
                jObject.put("idinventario", idinventario);
                jObject.put(ContractParaProductos.Columnas.ID_PRODUCTO, idproducto);
                jObject.put(ContractParaProductos.Columnas.INVENTARIO_INICIAL, existente_inicial);
                jObject.put(ContractParaProductos.Columnas.INVENTARIO_FINAL, existente_final);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (url.equals(Constantes.INSERT_URL_VENTA)) {
            int idcarrito;
            int idinventario;
            String fecha;
            String ubicacion;
            String vendedor;


            idcarrito = c.getInt(COLUMNA_ID_CARRITO_VENTA);
            idinventario = c.getInt(COLUMNA_ID_INVENTARIO_VENTA);
            fecha = c.getString(COLUMNA_FECHA_VENTA);
            ubicacion = c.getString(COLUMNA_UBICACIÓN_VENTA);
            vendedor = c.getString(COLUMNA_VENDEDOR_VENTA);

            try {
                jObject.put(ContractParaProductos.Columnas.ID_CARRITO, idcarrito);
                jObject.put(ContractParaProductos.Columnas.ID_INVENTARIO, idinventario);
                jObject.put(ContractParaProductos.Columnas.FECHA, fecha);
                jObject.put(ContractParaProductos.Columnas.UBICACION, ubicacion);
                jObject.put(ContractParaProductos.Columnas.VENDEDOR, vendedor);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (url.equals(Constantes.INSERT_URL_VENTA_DETALLE)) {
            int idventa;
            int cantidad;
            int idproducto;
            Double precio;

            idventa = c.getInt(COLUMNA_ID_REMOTA_VENTA_DETALLE);
            cantidad = c.getInt(COLUMNA_CANTIDAD);
            idproducto = c.getInt(COLUMNA_ID_PRODUCTO_VENTA_DETALLE);
            precio = c.getDouble(COLUMNA_PRECIO);



            try {
                jObject.put("idventa", idventa);
                jObject.put(ContractParaProductos.Columnas.CANTIDAD, cantidad);
                jObject.put(ContractParaProductos.Columnas.ID_PRODUCTO, idproducto);
                jObject.put(ContractParaProductos.Columnas.PRECIO, precio);
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("Cursor a JSONObject", String.valueOf(jObject));    ////mostramos que valores se han insertado
        return jObject;
    }
}*/


