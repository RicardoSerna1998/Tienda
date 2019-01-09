package com.example.ricardosernam.tienda.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.Empleados.usuariosDialogFragment;
import com.example.ricardosernam.tienda.MainActivity;
import com.example.ricardosernam.tienda._____interfazes.actualizado;
import com.example.ricardosernam.tienda.provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.utils.Constantes;
import com.example.ricardosernam.tienda.utils.Utilidades;
import com.example.ricardosernam.tienda.web.Empleados;
import com.example.ricardosernam.tienda.web.Informacion;
import com.example.ricardosernam.tienda.web.Inventario;
import com.example.ricardosernam.tienda.web.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.ricardosernam.tienda.utils.Constantes.UPDATE_URL_EMPLEADOS;
import static com.example.ricardosernam.tienda.utils.Constantes.UPDATE_URL_INVENTARIO;


/**
 * Maneja la transferencia de datos entre el servidor y el cliente
 *
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();
    private static String urlChingon, seleccionado, url2, URL_EMPLEADO;
    private Cursor empladosActualizados;
    private static int conteo, or;
    private ContentValues values, values2;
    private ContentResolver resolver;
    private static actualizado Interfaz;
    private DatabaseHelper admin = new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
    private SQLiteDatabase db = admin.getWritableDatabase();
    //private Gson gson = new Gson();
    private Gson gson = new Gson();


    /**
     * Proyección para las consultas
     */
    private static final String[] PROJECTION_EMPLEADOS = new String[]{
            ContractParaProductos.Columnas._ID,
            ContractParaProductos.Columnas.ID_REMOTA,
            ContractParaProductos.Columnas.NOMBRE_EMPLEADO,
            ContractParaProductos.Columnas.TIPO_EMPLEADO,
            ContractParaProductos.Columnas.CODIGO,
            ContractParaProductos.Columnas.ACTIVO,
    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID_EMPLEADO = 0;
    private static final int COLUMNA_ID_REMOTA_EMPLEADO = 1;
    private static final int COLUMNA_NOMBRE_EMPLEADO = 2;
    private static final int COLUMNA_TIPO_EMPLEADO = 3;
    private static final int COLUMNA_CODIGO = 4;
    public static final int COLUMNA_ACTIVO = 5;

    /////////////////////////////////////////////////////////////////////////////////////
    private static final String[] PROJECTION_INFORMACION = new String[]{
            ContractParaProductos.Columnas._ID,
            ContractParaProductos.Columnas.ID_REMOTA,
            ContractParaProductos.Columnas.NOMBRE_NEGOCIO,
            ContractParaProductos.Columnas.DIRECCION,
            ContractParaProductos.Columnas.TELEFONO,
    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID_PRODUCTO = 0;
    private static final int COLUMNA_ID_REMOTA_INFORMACION = 1;
    private static final int COLUMNA_NOMBRE_NEGOCIO = 2;
    private static final int COLUMNA_DIRECCION = 3;
    private static final int COLUMNA_TELEFONO = 4;

    /////////////////////////////////////////////////////////////////////////////////////
    private static final String[] PROJECTION_INVENTARIO = new String[]{
            ContractParaProductos.Columnas._ID,
            ContractParaProductos.Columnas.ID_REMOTA,
            ContractParaProductos.Columnas.NOMBRE_PRODUCTO,
            ContractParaProductos.Columnas.PRECIO,
            ContractParaProductos.Columnas.EXISTENTES,
    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID_INVENTARIO = 0;
    private static final int COLUMNA_ID_REMOTA_INVENTARIO = 1;
    private static final int COLUMNA_ID_NOMBRE_PRODUCTO = 2;    ////
    private static final int COLUMNA_PRECIO_INVENTARIO = 3;    //////
    private static final int COLUMNA_CODIGO_BARRAS = 4;    //////
    private static final int COLUMNA_EXISTENTES = 5;

    ////////////////////////////////////////////////////////////////////////////////////////
    private static final String[] PROJECTION_TURNO = new String[]{
            ContractParaProductos.Columnas._ID,
            ContractParaProductos.Columnas.ID_REMOTA,
            ContractParaProductos.Columnas.HORA_INICIO,
            ContractParaProductos.Columnas.HORA_FIN,
    };

    // Indices para las columnas indicadas en la proyección
    private static final int COLUMNA_ID_REMOTA_TURNO = 0;
    private static final int COLUMNA_HORA_INICIO = 1;
    private static final int COLUMNA_HORA_FIN = 2;


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String[] PROJECTION_VENTA = new String[]{
            ContractParaProductos.Columnas._ID,
            ContractParaProductos.Columnas.ID_REMOTA,
            ContractParaProductos.Columnas.ID_EMPLEADO,
            ContractParaProductos.Columnas.FECHA,

    };

    // Indices para las columnas indicadas en la proyección
    private static final int COLUMNA_ID_VENTA = 0;
    public static final int COLUMNA_ID_REMOTA_VENTA = 1;
    public static final int COLUMNA_ID_EMPLEADO_VENTA = 2;
    public static final int COLUMNA_FECHA_VENTA = 3;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String[] PROJECTION_VENTA_DETALLE = new String[]{
            ContractParaProductos.Columnas._ID,
            ContractParaProductos.Columnas.ID_REMOTA,
            ContractParaProductos.Columnas.ID_PRODUCTO,
            ContractParaProductos.Columnas.PRECIO,
            ContractParaProductos.Columnas.CANTIDAD,
    };

    // Indices para las columnas indicadas en la proyección
    private static final int COLUMNA_ID_VENTA_DETALLES = 0;      ///////funciona solo en la exportacion
    public static final int COLUMNA_ID_REMOTA_VENTA_DETALLE = 1;
    public static final int COLUMNA_ID_PRODUCTO_VENTA_DETALLE = 2;
    public static final int COLUMNA_PRECIO_VENTA_DETALLES = 3;
    public static final int COLUMNA_CANTIDAD = 4;


    SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    ///             TABLA A OBTENER    CARRITO SELECCIONADO
    public static void inicializarSyncAdapter(Context context, String ur, String select) {    ////PRIMER METODO LLAMADO
        urlChingon = ur;
        seleccionado = select;
        //obtenerCuentaASincronizar(context);
    }


    /////////////////////////////////////////////////////metodos de sincronizacion ///////////////////////////////////////////////////////
    private static Account obtenerCucentaASincronizar(Context context) {
        // Obtener instancia del administrador de cuentas
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Crear cuenta por defecto
        Account newAccount = new Account(context.getString(R.string.app_name), Constantes.ACCOUNT_TYPE);

        // Comprobar existencia de la cuenta
        assert accountManager != null;
        if (null == Objects.requireNonNull(accountManager).getPassword(newAccount)) {

            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;
        }
        Log.i(TAG, "Cuenta de usuario obtenida.");
        return newAccount;
    }

    ///de aqui pasa a onSyncPerform
    public static void sincronizarAhora(Context context, boolean onlyUpload, int orden, String url2) {    ///aquí si imprime
        Log.i(TAG, "Realizando petición de sincronización manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload) {
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
            //bundle.putString("url", url2);
        } else {
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);
            //bundle.putString("url", urlChingon);
        }
        bundle.putString("url", url2);
        bundle.putInt("orden", orden);
        ContentResolver.requestSync(obtenerCucentaASincronizar(context), context.getString(R.string.provider_authority), bundle);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "onPerformSync()...");
        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);
        String url3 = extras.getString("url", url2);
        int orden = extras.getInt("orden", or);


        if (!soloSubida) {
            realizarSincronizacionLocal(syncResult, url3,orden, seleccionado);   ////descargar
        } else {
            realizarSincronizacionRemota(url3);  //subir
        }
    }


    private void realizarSincronizacionLocal(final SyncResult syncResult, final String url, final int orden, final String seleccionado) {
        final String uri;
        ///los que tienen carrito  (se concatena en el url)   o inventario
        /*if(url.equals(Constantes.GET_URL_INVENTARIO) || url.equals(Constantes.GET_URL_INVENTARIO_DETALLE)){
            uri=url+seleccionado;
        }
        ///los que no ocupan saber el carrito ni inventario
        else{*/
        //uri = url;

        //}
        Log.i(TAG, "Actualizando el cliente.");   ////hasta aqui bien
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, url,  ////POSIBLE ERROR
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGet(response, syncResult, orden);
                            }
                        },
                        new Response.ErrorListener() {  //// Si el ip es incorrecto
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Revisa los servicios de XAMPP, tu IP, o tu conexión a Internet e intentalo nuevamente", Toast.LENGTH_LONG).show();

                                /*if(!(uri.equals(Constantes.GET_URL_CARRITO))){  //SI NO ES CARRRITO
                                    new android.os.Handler().postDelayed(
                                                            new Runnable() {
                                                                public void run() {
                                                                    Sincronizar.progressDialog.dismiss();
                                                                    Toast.makeText(getContext(), "Revisa los servicios de XAMPP, tu IP, o tu conexión a Internet e intentalo nuevamente", Toast.LENGTH_LONG).show();
                                                                }
                                                            }, 3000);
                                }
                                else{
                                    Sincronizar.buscar.setEnabled(true);
                                    Sincronizar.buscar.setText(" Buscar carritos ");
                                    Sincronizar.buscar.getBackground().setColorFilter(null);  //habilitado  SE PRESIONO BUSCAR
                                    Toast.makeText(getContext(), "Revisa los servicios de XAMPP, tu IP, o tu conexión a Internet e intentalo nuevamente", Toast.LENGTH_LONG).show();
                                    }*/
                            }
                        }
                )
        );
    }

    /**
     * Procesa la respuesta del servidor al pedir que se retornen todos los gastos.
     *
     * @param response   Respuesta en formato Json
     * @param syncResult Registro de resultados de sincronización
     */
    private void procesarRespuestaGet(JSONObject response, SyncResult syncResult, int url) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {

                case Constantes.SUCCESS: // EXITO En caso de 1
                    Toast.makeText(getContext(), "actualizarDatosLocales", Toast.LENGTH_LONG).show();  ////error con los carritos
                    actualizarDatosLocales(response, syncResult, url);
                    break;
                case Constantes.FAILED: // FALLIDO En caso de 2
                    if (url==1) {   ////no hay carritos
                        /*Sincronizar.carritos.setAdapter(null);
                        database.execSQL("delete from carritos");
                        Sincronizar.buscar.setEnabled(true);
                        Sincronizar.buscar.setText(" Buscar carritos ");
                        Sincronizar.buscar.getBackground().setColorFilter(null);  //habilitado*/
                        Toast.makeText(getContext(), "No hay Empleados", Toast.LENGTH_LONG).show();  ////error con los carritos
                    }
                    /*else if(url.equals(Constantes.GET_URL_INVENTARIO)){   ///el carrro seleccionado ya no existe
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Sincronizar.progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Vuelve a buscar y selecciona otro carrito", Toast.LENGTH_LONG).show();  ////error con los carritos
                                        Sincronizar.carritos.setAdapter(null);
                                    }
                                }, 3000);
                        }*/
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocales(JSONObject response, SyncResult syncResult, int url) throws JSONException {   ///aqui esta el error
/////////////////////////////////////////////// CARRITO /////////////////////////////////////////////////////7
        if (url == 0) {
            JSONArray gastos = null;
            try {
                // Obtener array "gastos"
                gastos = response.getJSONArray(Constantes.INFORMACION);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Parsear con Gson
            // Se pasan de web.Empleados hacias ops2 y luego se insertan
            ArrayList<ContentProviderOperation> ops2 = new ArrayList<ContentProviderOperation>();

            // Tabla hash para recibir las entradas entrantes
            HashMap<String, com.example.ricardosernam.tienda.web.Informacion> expenseMap2 = new HashMap<>();   /////contiene los datos consultados
            //HashMap<String, String> datos2 = new HashMap<>();   /////contiene los datos consultados
            List<com.example.ricardosernam.tienda.web.Informacion> datos2 = new ArrayList<>();
            ;

            //Toast.makeText(getContext(), String.valueOf(gastos.length()), Toast.LENGTH_LONG).show();

            for (int i = 0; i < gastos.length(); i++) {
                JSONObject c = null;
                try {
                    c = gastos.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getContext(), c.getString("id_empleado")+" "+c.getString("nombre_empleado")+" "+c.getString("tipo_empleado")+" "+c.getString("codigo")+" "+c.getInt("activo"), Toast.LENGTH_LONG).show();

                datos2.add(new Informacion(c.getString("id_negocio"), c.getString("nombre_negocio"), c.getString("direccion"), c.getString("telefono")));
            }


            for (com.example.ricardosernam.tienda.web.Informacion e : datos2) {  ///asignamos los datos de data2 a empleados
                expenseMap2.put(e.idinformacion, e);
                //Toast.makeText(getContext(), e.idempleado, Toast.LENGTH_LONG).show();
            }
            //   SyncAdapter.sincronizarAhora(getContext(), false, Constantes.GET_URL_INVENTARIO);
            // }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Consultar registros remotos actuales
            Cursor c2 = db.rawQuery("select * from informacion", null);
            /*Uri uri2 = ContractParaProductos.CONTENT_URI_INFORMACION;
            String select2 = ContractParaProductos.Columnas.ID_REMOTA + " IS NOT NULL";
            Cursor c2 = resolver.query(uri2, PROJECTION_INFORMACION, select2, null, null);*/

            // Encontrar datos obsoletos

            String idinformacion;
            String nombre;
            String direccion;
            String telefono;
            ///CHECAMOS LOS REGISTROS QUE YA ESTAN INSERTADOS
            if ((c2 != null ? c2.getCount() : 0) > 0) {  ///api 19
                while (c2.moveToNext()) {
                    syncResult.stats.numEntries++;

                    idinformacion = c2.getString(COLUMNA_ID_REMOTA_EMPLEADO);    ////
                    nombre = c2.getString(COLUMNA_NOMBRE_EMPLEADO);
                    direccion = c2.getString(COLUMNA_TIPO_EMPLEADO);
                    telefono = c2.getString(COLUMNA_CODIGO);


                    com.example.ricardosernam.tienda.web.Informacion match = expenseMap2.get(idinformacion);

                    if (match != null) {  ////existen los mismos datos
                        // Esta entrada existe, por lo que se remueve del mapeado
                        expenseMap2.remove(idinformacion);

                        Uri existingUri = ContractParaProductos.CONTENT_URI_INFORMACION.buildUpon().appendPath(idinformacion).build();

                        // Comprobar si el gasto necesita ser actualizado
                        boolean b = match.nombre != null && !match.nombre.equals(nombre);
                        boolean b2 = match.direccion != null && !match.direccion.equals(direccion);
                        boolean b3 = match.telefono != null && !match.telefono.equals(telefono);
                        if (b || b2 || b3) {
                            Log.i(TAG, "Programando actualización de: " + existingUri + "INFORMACION");
                            ops2.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(ContractParaProductos.Columnas.NOMBRE_NEGOCIO, match.nombre)
                                    .withValue(ContractParaProductos.Columnas.DIRECCION, match.direccion)
                                    .withValue(ContractParaProductos.Columnas.TELEFONO, match.telefono)
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri + " INFORMACION");
                        }
                    } else {// eliminamos los datos que no estan en local host
                        Uri deleteUri = ContractParaProductos.CONTENT_URI_INFORMACION.buildUpon().appendPath(idinformacion).build();
                        Log.i(TAG, "Programando eliminación de: " + deleteUri + " INFORMACION");
                        ops2.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
            }
            //assert c2 != null;
            if (c2 != null) {
                c2.close();
            }
///////////////////////////////////////////////////////////////newInsert///////////////////////////////////////////////////////////////////////////////
            ////insertamos los valores de la base de datos
            for (com.example.ricardosernam.tienda.web.Informacion e : expenseMap2.values()) {
                Log.i(TAG, "Programando inserción de: " + e.idinformacion + " INFORMACION");
                ops2.add(ContentProviderOperation.newInsert(ContractParaProductos.CONTENT_URI_INFORMACION)   /////error
                        .withValue(ContractParaProductos.Columnas.ID_REMOTA, e.idinformacion)
                        .withValue(ContractParaProductos.Columnas.NOMBRE_NEGOCIO, e.nombre)
                        .withValue(ContractParaProductos.Columnas.DIRECCION, e.direccion)
                        .withValue(ContractParaProductos.Columnas.TELEFONO, e.telefono)
                        .build());
                syncResult.stats.numInserts++;
            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (syncResult.stats.numInserts > 0 || syncResult.stats.numUpdates > 0 || syncResult.stats.numDeletes > 0) {
                Log.i(TAG, "Aplicando operaciones... INFORMACION");
                try {
                    resolver.applyBatch(ContractParaProductos.AUTHORITY, ops2);
                } catch (RemoteException | OperationApplicationException e) {
                    e.printStackTrace();
                }
                resolver.notifyChange(ContractParaProductos.CONTENT_URI_INFORMACION, null, false);
                Log.i(TAG, "Sincronización finalizada INFORMACION.");
                SyncAdapter.sincronizarAhora(getContext(), false, 1, Constantes.GET_URL_EMPLEADOS);


            } else {
                Log.i(TAG, "No se requiere sincronización INFORMACION");
            }
            //Sincronizar.buscar.setEnabled(true);
            //Sincronizar.buscar.setText(" Buscar carritos ");
            //Sincronizar.buscar.getBackground().setColorFilter(null);  //habilitado

        } else if (url == 1) {
            JSONArray gastos = null;
            try {
                // Obtener array "gastos"
                gastos = response.getJSONArray(Constantes.EMPLEADO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Parsear con Gson
            // Se pasan de web.Empleados hacias ops2 y luego se insertan
            ArrayList<ContentProviderOperation> ops2 = new ArrayList<ContentProviderOperation>();

            // Tabla hash para recibir las entradas entrantes
            HashMap<String, com.example.ricardosernam.tienda.web.Empleados> expenseMap2 = new HashMap<>();   /////contiene los datos consultados
            //HashMap<String, String> datos2 = new HashMap<>();   /////contiene los datos consultados
            List<com.example.ricardosernam.tienda.web.Empleados> datos2 = new ArrayList<>();
            ;

            //Toast.makeText(getContext(), String.valueOf(gastos.length()), Toast.LENGTH_LONG).show();

            for (int i = 0; i < gastos.length(); i++) {
                JSONObject c = null;
                try {
                    c = gastos.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getContext(), c.getString("id_empleado")+" "+c.getString("nombre_empleado")+" "+c.getString("tipo_empleado")+" "+c.getString("codigo")+" "+c.getInt("activo"), Toast.LENGTH_LONG).show();

                datos2.add(new Empleados(c.getString("id_empleado"), c.getString("nombre_empleado"), c.getString("tipo_empleado"), c.getString("codigo"), c.getInt("activo")));
            }


            for (com.example.ricardosernam.tienda.web.Empleados e : datos2) {  ///asignamos los datos de data2 a empleados
                expenseMap2.put(e.idempleado, e);
                //Toast.makeText(getContext(), e.idempleado, Toast.LENGTH_LONG).show();
            }
            //   SyncAdapter.sincronizarAhora(getContext(), false, Constantes.GET_URL_INVENTARIO);
            // }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Consultar registros remotos actuales
            Cursor c2 = db.rawQuery("select * from empleados", null);
           /* Uri uri2 = ContractParaProductos.CONTENT_URI_EMPLEADOS;
            String select2 = ContractParaProductos.Columnas.ID_REMOTA + " IS NOT NULL";
            Cursor c2 = resolver.query(uri2, PROJECTION_EMPLEADOS, select2, null, null);*/

            // Encontrar datos obsoletos

            String idempleado;
            String nombre;
            String tipo;
            String codigo;
            int activo;
            ///CHECAMOS LOS REGISTROS QUE YA ESTAN INSERTADOS
            if ((c2 != null ? c2.getCount() : 0) > 0) {  ///api 19
                while (c2.moveToNext()) {
                    syncResult.stats.numEntries++;

                    idempleado = c2.getString(COLUMNA_ID_REMOTA_EMPLEADO);    ////
                    nombre = c2.getString(COLUMNA_NOMBRE_EMPLEADO);
                    tipo = c2.getString(COLUMNA_TIPO_EMPLEADO);
                    codigo = c2.getString(COLUMNA_CODIGO);
                    activo = c2.getInt(COLUMNA_ACTIVO);


                    com.example.ricardosernam.tienda.web.Empleados match = expenseMap2.get(idempleado);

                    if (match != null) {  ////existen los mismos datos
                        // Esta entrada existe, por lo que se remueve del mapeado
                        expenseMap2.remove(idempleado);

                        Uri existingUri = ContractParaProductos.CONTENT_URI_EMPLEADOS.buildUpon().appendPath(idempleado).build();

                        // Comprobar si el gasto necesita ser actualizado
                        boolean b = match.nombre != null && !match.nombre.equals(nombre);
                        boolean b2 = match.tipo != null && !match.tipo.equals(tipo);
                        boolean b3 = match.codigo != null && !match.codigo.equals(codigo);
                        boolean b4 = match.activo != activo;
                        if (b || b2 || b3 || b4) {
                            Log.i(TAG, "Programando actualización de: " + existingUri + " EMPLEADOS");
                            ops2.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(ContractParaProductos.Columnas.NOMBRE_EMPLEADO, match.nombre)
                                    .withValue(ContractParaProductos.Columnas.TIPO_EMPLEADO, match.tipo)
                                    .withValue(ContractParaProductos.Columnas.CODIGO, match.codigo)
                                    .withValue(ContractParaProductos.Columnas.ACTIVO, match.activo)
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri + " EMPLEADOS");
                        }
                    } else {// eliminamos los datos que no estan en local host
                        Uri deleteUri = ContractParaProductos.CONTENT_URI_EMPLEADOS.buildUpon().appendPath(idempleado).build();
                        Log.i(TAG, "Programando eliminación de: " + deleteUri + " EMPLEADOS");
                        ops2.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
            }
            //assert c2 != null;
            if (c2 != null) {
                c2.close();
            }
///////////////////////////////////////////////////////////////newInsert///////////////////////////////////////////////////////////////////////////////
            for (com.example.ricardosernam.tienda.web.Empleados e : expenseMap2.values()) {
                Log.i(TAG, "Programando inserción de: " + e.idempleado + " EMPLEADOS");
                ops2.add(ContentProviderOperation.newInsert(ContractParaProductos.CONTENT_URI_EMPLEADOS)   /////error
                        .withValue(ContractParaProductos.Columnas.ID_REMOTA, e.idempleado)
                        .withValue(ContractParaProductos.Columnas.NOMBRE_EMPLEADO, e.nombre)
                        .withValue(ContractParaProductos.Columnas.TIPO_EMPLEADO, e.tipo)
                        .withValue(ContractParaProductos.Columnas.CODIGO, e.codigo)
                        .withValue(ContractParaProductos.Columnas.ACTIVO, e.activo)
                        .withValue(ContractParaProductos.Columnas.PENDIENTE_INSERCION, 1)
                        .build());
                syncResult.stats.numInserts++;
            }
            if (syncResult.stats.numInserts > 0 || syncResult.stats.numUpdates > 0 || syncResult.stats.numDeletes > 0) {
                Log.i(TAG, "Aplicando operaciones... EMPLEADOS");
                try {
                    resolver.applyBatch(ContractParaProductos.AUTHORITY, ops2);
                } catch (RemoteException | OperationApplicationException e) {
                    e.printStackTrace();
                }
                resolver.notifyChange(ContractParaProductos.CONTENT_URI_EMPLEADOS, null, false);
                Log.i(TAG, "Sincronización finalizada EMPLEADOS.");
                com.example.ricardosernam.tienda.Empleados.Empleados.relleno(getContext());
                SyncAdapter.sincronizarAhora(getContext(), false, 2, Constantes.GET_URL_INVENTARIO);

            } else {
                Log.i(TAG, "No se requiere sincronización EMPLEADOS");
            }
            //Sincronizar.buscar.setEnabled(true);
            //Sincronizar.buscar.setText(" Buscar carritos ");
            //Sincronizar.buscar.getBackground().setColorFilter(null);  //habilitado

        }
///////////////////////////////////////////////INVENTARIO/////////////////////////////////////////////////////7
        else if (url == 2) {
            JSONArray gastosI = null;

            try {
                // Obtener array "gastos"
                gastosI = response.getJSONArray(Constantes.INVENTARIO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Parsear con Gson
            List<com.example.ricardosernam.tienda.web.Inventario> datos2 = new ArrayList<>();
            ;

            for (int i = 0; i < gastosI.length(); i++) {
                JSONObject c = null;
                try {
                    c = gastosI.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                datos2.add(new Inventario(c.getString("id_producto"), c.getString("nombre_producto"), c.getDouble("precio"), c.getString("codigo_barras"), c.getDouble("existente")));
            }

            // Lista para recolección de operaciones pendientes
            ArrayList<ContentProviderOperation> ops3 = new ArrayList<ContentProviderOperation>();

            // Tabla hash para recibir las entradas entrantes
            HashMap<String, com.example.ricardosernam.tienda.web.Inventario> expenseMap3 = new HashMap<String, com.example.ricardosernam.tienda.web.Inventario>();   /////contiene los datos consultados

            for (com.example.ricardosernam.tienda.web.Inventario e : datos2) {
                expenseMap3.put(e.idproducto, e);  ///id, existentes  y nombre nos los trae,
                //Toast.makeText(getContext(), e.idproducto + " " + e.nombre + " " + e.precio + " " + e.codigo_barras + " " + e.existentes, Toast.LENGTH_LONG).show();

            }
            //  }
            // Consultar registros remotos actuales
            Cursor c2 = db.rawQuery("select * from inventario", null);
            /*Uri uri2 = ContractParaProductos.CONTENT_URI_INVENTARIO;
            String select2 = ContractParaProductos.Columnas.ID_REMOTA + " IS NOT NULL";
            Cursor c2 = resolver.query(uri2, PROJECTION_INVENTARIO, select2, null, null);*/

            //assert c2 != null;

            //Log.i(TAG, "Se encontraron " + c2.getCount() + " registros locales INVENTARIO.");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Encontrar datos obsoletos
            String idinventario;
            String nombre_producto;
            double precio;
            String codigo_barras;
            double existentes;


            if ((c2 != null ? c2.getCount() : 0) > 0) {  ///api 19
                while (c2.moveToNext()) {
                    syncResult.stats.numEntries++;

                    idinventario = c2.getString(COLUMNA_ID_REMOTA_INVENTARIO);
                    nombre_producto = c2.getString(COLUMNA_ID_NOMBRE_PRODUCTO);
                    precio = c2.getDouble(COLUMNA_PRECIO_INVENTARIO);
                    codigo_barras = c2.getString(COLUMNA_CODIGO_BARRAS);
                    existentes = c2.getDouble(COLUMNA_EXISTENTES);


                    com.example.ricardosernam.tienda.web.Inventario match = expenseMap3.get(idinventario);

                    if (match != null) {  ////existen los mismos datos
                        // Esta entrada existe, por lo que se remueve del mapeado
                        expenseMap3.remove(idinventario);

                        Uri existingUri = ContractParaProductos.CONTENT_URI_INVENTARIO.buildUpon().appendPath(idinventario).build();

                        // Comprobar si el gasto necesita ser actualizado
                        boolean b = match.idproducto != idinventario;
                        boolean b1 = match.nombre != null && !match.nombre.equals(nombre_producto);
                        boolean b2 = match.precio != precio;
                        boolean b3 = match.codigo_barras != null && !match.codigo_barras.equals(codigo_barras);
                        boolean b4 = match.existentes != existentes;

                        if (b || b1 || b2 || b3 || b4) {
                            Log.i(TAG, "Programando actualización de: " + existingUri + " INVENTARIO");
                            ops3.add(ContentProviderOperation.newUpdate(existingUri)
                                    .withValue(ContractParaProductos.Columnas.ID_PRODUCTO, match.idproducto)
                                    .withValue(ContractParaProductos.Columnas.NOMBRE_PRODUCTO, match.nombre)
                                    .withValue(ContractParaProductos.Columnas.PRECIO, match.precio)
                                    .withValue(ContractParaProductos.Columnas.CODIGO_BARRAS, match.codigo_barras)
                                    .withValue(ContractParaProductos.Columnas.EXISTENTES, match.existentes)
                                    .withValue(ContractParaProductos.Columnas.PENDIENTE_INSERCION, 1)
                                    .build());
                            syncResult.stats.numUpdates++;
                        } else {
                            Log.i(TAG, "No hay acciones para este registro: " + existingUri + " INVENTARIO");
                        }
                    } else {
                        // eliminamos los datos que no estan en local host
                        Uri deleteUri = ContractParaProductos.CONTENT_URI_INVENTARIO.buildUpon().appendPath(idinventario).build();
                        Log.i(TAG, "Programando eliminación de: " + deleteUri + " INVENTARIO");
                        ops3.add(ContentProviderOperation.newDelete(deleteUri).build());
                        syncResult.stats.numDeletes++;
                    }
                }
            }
            if (c2 != null) {
                c2.close();
            }

            /*values2 = new ContentValues();
            ////insertamos los valores a la base de datos
            for (com.example.ricardosernam.tienda.web.Inventario e : expenseMap3.values()) {
                Log.i(TAG, "Programando inserción de: " + e.idproducto + " INVENTARIO");    ////no trae ningun dato
                syncResult.stats.numInserts++;
                values2.put("idRemota", e.idproducto);
                values2.put("nombre_producto", e.nombre);
                values2.put("precio", e.precio);
                values2.put("codigo_barras", e.codigo_barras);
                values2.put("existentes", e.existentes);
                db.insertOrThrow("inventario", null, values2);
            }
            if (syncResult.stats.numInserts > 0 || syncResult.stats.numUpdates > 0 || syncResult.stats.numDeletes > 0) {
                Toast.makeText(getContext(), "Inserta chingon", LENGTH_LONG).show();
                Log.i(TAG, "Sincronización finalizada INVENTARIO.");
                //empladosActualizados = db.rawQuery("select nombre_empleado, tipo_empleado, activo, codigo from empleados ORDER by tipo_empleado, activo desc", null);

                //com.example.ricardosernam.tienda.Empleados.Empleados.relleno(empladosActualizados);
                //Interfaz = (actualizado) getContext();  ///interfaz para notificar el nuevo producto agregado
                ///Interfaz.actualizar();
            }*/
            ////insertamos los valores de la base de datos
            for (com.example.ricardosernam.tienda.web.Inventario e : expenseMap3.values()) {
                Log.i(TAG, "Programando inserción de: " + e.idproducto + " INVENTARIO");
                ops3.add(ContentProviderOperation.newInsert(ContractParaProductos.CONTENT_URI_INVENTARIO)   /////error
                        .withValue(ContractParaProductos.Columnas.ID_REMOTA, e.idproducto)
                        .withValue(ContractParaProductos.Columnas.NOMBRE_PRODUCTO, e.nombre)
                        .withValue(ContractParaProductos.Columnas.PRECIO, e.precio)
                        .withValue(ContractParaProductos.Columnas.CODIGO_BARRAS, e.codigo_barras)
                        .withValue(ContractParaProductos.Columnas.EXISTENTES, e.existentes)
                        .build());
                syncResult.stats.numInserts++;
            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (syncResult.stats.numInserts > 0 || syncResult.stats.numUpdates > 0 || syncResult.stats.numDeletes > 0) {
                Log.i(TAG, "Aplicando operaciones... INVENTARIO");
                try {
                    resolver.applyBatch(ContractParaProductos.AUTHORITY, ops3);
                } catch (RemoteException | OperationApplicationException e) {
                    e.printStackTrace();
                }
                resolver.notifyChange(ContractParaProductos.CONTENT_URI_INVENTARIO, null, false);
                Log.i(TAG, "Sincronización finalizada INVENTARIO.");

            } else {
                Log.i(TAG, "No se requiere sincronización INVENTARIO");
            }
        }
    }
///////////////////////////////////PRODUCTOS//////////////////////////////////////////////////////7

        ////////////////////////////////////////////////////////metodos de incersion //////////////////////////////////////////////////////////
    private void realizarSincronizacionRemota(final String url) {
        Log.i(TAG, "Actualizando el servidor...");
        if (url.equals(UPDATE_URL_EMPLEADOS)) {  ///actualizamos al importar
            iniciarActualizacion(url);
            final Cursor c = db.rawQuery("select activo from empleados where pendiente_insercion=1", null);
            Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios EMPLEADOS");

            if (c.getCount() > 0) {  ///api 19
                while (c.moveToNext()) {
                    @SuppressLint("Recycle") Cursor idempleado = db.rawQuery("select idRemota from empleados", null);

                    if (idempleado.moveToFirst()) {
                        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                                new JsonObjectRequest(Request.Method.POST,
                                        UPDATE_URL_EMPLEADOS + idempleado.getString(0),
                                        Utilidades.deCursorAJSONObject(c, url),  //////////////////////////////////////////////
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                procesarRespuestaInsert(response, 0, url, c.getCount());
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d(TAG, "Error Volley: " + error.getMessage()); ///error aqui
                                                //realizarSincronizacionRemota(url);

                                            }
                                        }

                                ) {
                                    @Override
                                    public Map<String, String> getHeaders() {
                                        Map<String, String> headers = new HashMap<String, String>();
                                        headers.put("Content-Type", "application/json; charset=utf-8");
                                        headers.put("Accept", "application/json");
                                        return headers;
                                    }

                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=utf-8" + getParamsEncoding();
                                    }
                                }
                        );
                    }
                }

            } else {
                Log.i(TAG, "No se requiere sincronización UPDATE INVENTARIO");
                //}
            }
            c.close();
        } else if (url.equals(UPDATE_URL_INVENTARIO)) {  ///actualizamos al importar
            iniciarActualizacion(url);
            final Cursor c = db.rawQuery("select existente from inventario where pendiente_insercion=1", null);
            Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios INVENTARIO");

            if (c.getCount() > 0) {  ///api 19
                while (c.moveToNext()) {
                    @SuppressLint("Recycle") Cursor idinventario = db.rawQuery("select idRemota from inventarios", null);

                    if (idinventario.moveToFirst()) {
                        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                                new JsonObjectRequest(Request.Method.POST,
                                        UPDATE_URL_INVENTARIO + idinventario.getString(0),
                                        Utilidades.deCursorAJSONObject(c, url),  //////////////////////////////////////////////
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                procesarRespuestaInsert(response, 0, url, c.getCount());
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d(TAG, "Error Volley: " + error.getMessage()); ///error aqui
                                                //realizarSincronizacionRemota(url);
                                            }
                                        }

                                ) {
                                    @Override
                                    public Map<String, String> getHeaders() {
                                        Map<String, String> headers = new HashMap<String, String>();
                                        headers.put("Content-Type", "application/json; charset=utf-8");
                                        headers.put("Accept", "application/json");
                                        return headers;
                                    }

                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=utf-8" + getParamsEncoding();
                                    }
                                }
                        );
                    }
                }

            } else {
                Log.i(TAG, "No se requiere sincronización UPDATE INVENTARIO");
                //}
            }
            c.close();
        }
        /*else if (url.equals(Constantes.UPDATE_URL_INVENTARIO_DETALLE)) {
            iniciarActualizacion(url);

            final Cursor c=database.rawQuery("select * from inventario_detalles where pendiente_insercion=1", null);

            Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios INVENTARIO_DETALLES");   ////muestra la cantidad a sincronizar

                if (c.getCount()> 0) {  ///api 19
                while (c.moveToNext()) {
                    Log.i(TAG, "Va en " + c.getPosition());   ////muestra la cantidad a sincronizar
                    final int idLocal = c.getInt(COLUMNA_ID_INVENTARIO_DETALLES);

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(
                                new JsonObjectRequest(Request.Method.POST,
                                        Constantes.UPDATE_URL_INVENTARIO_DETALLE,
                                        Utilidades.deCursorAJSONObject(c, url),  //////////////////////////////////////////////
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                procesarRespuestaInsert(response, idLocal, url, c.getCount());
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley INVENTARIO_DETALLES: " + error.getMessage());

                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {
                                                        Sincronizar.progressDialog.dismiss();
                                                        Toast.makeText(getContext(), "Revisa los servicios de XAMPP, tu IP, o tu conexión a Internet e intentalo nuevamente", Toast.LENGTH_LONG).show();
                                                    }
                                                }, 3000);
                                        }
                                }

                                ) {
                                    @Override
                                    public Map<String, String> getHeaders() {
                                        Map<String, String> headers = new HashMap<String, String>();
                                        headers.put("Content-Type", "application/json; charset=utf-8");
                                        headers.put("Accept", "application/json");
                                        return headers;
                                    }

                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=utf-8" + getParamsEncoding();
                                    }
                                }
                        );
                }

            } else {
                Log.i(TAG, "No se requiere sincronización");
            }
                c.close();
        }
        else if (url.equals(Constantes.INSERT_URL_VENTA)) {
            iniciarActualizacion(url);

            final Cursor c=database.rawQuery("select * from ventas where pendiente_insercion=1", null);

            Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios VENTAS.");

            if (c.getCount() > 0) {  ///api 19
                while (c.moveToNext()) {
                    final int idLocal = c.getInt(COLUMNA_ID_VENTA);

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.POST,
                                    Constantes.INSERT_URL_VENTA,
                                    Utilidades.deCursorAJSONObject(c, url),  /////////////// vuelve al principio
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            procesarRespuestaInsert(response, idLocal, url, c.getCount());
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "Error Volley: " + error.getMessage());
                                            realizarSincronizacionRemota(url);
                                        }
                                    }

                            ) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json; charset=utf-8");
                                    headers.put("Accept", "application/json");
                                    return headers;
                                }

                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8" + getParamsEncoding();
                                }
                            }
                    );
                }

            } else {
                Log.i(TAG, "No se requiere sincronización VENTAS");   ////si no hay venta
                Log.i(TAG, "RECREA BD");
                Sincronizar.carritos.setEnabled(true);
                Sincronizar.carritos.setAdapter(null);


                Sincronizar.importar.setEnabled(true);
                Sincronizar.exportar.setEnabled(false);
                Sincronizar.buscar.setEnabled(true);

                Sincronizar.importar.getBackground().setColorFilter(null);  //habilitado
                Sincronizar.exportar.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP); ///deshabilitado
                Sincronizar.buscar.getBackground().setColorFilter(null);  //habilitado

                values.put(ContractParaProductos.Columnas.IMPORTADO, 1);
                database.update("estados", values, null, null);

                Sincronizar.progressDialog.dismiss();
                Toast.makeText(getContext(), "Tus datos han sido subidos", Toast.LENGTH_LONG).show();
                DatabaseHelper admin=new DatabaseHelper(getContext(), ProviderDeProductos.DATABASE_NAME, null, ProviderDeProductos.DATABASE_VERSION);
                DatabaseHelper.limpiar(admin.getWritableDatabase());
            }
                c.close();
        }
        else if (url.equals(Constantes.INSERT_URL_VENTA_DETALLE)) {
            iniciarActualizacion(url);   //NO OBTIENE BIEN LA CUENTA

            final Cursor c=database.rawQuery("select * from venta_detalles where pendiente_insercion=1", null);


            Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios VENTA DETALLE.");

                if (c.getCount()  > 0) {  ///api 19
                while (c.moveToNext()) {
                    final int idLocal = c.getInt(COLUMNA_ID_VENTA_DETALLES);

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(
                            new JsonObjectRequest(Request.Method.POST,
                                    Constantes.INSERT_URL_VENTA_DETALLE,
                                    Utilidades.deCursorAJSONObject(c, url),  //////////////////////////////////////////////
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            procesarRespuestaInsert(response, idLocal, url, c.getCount());
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "Error Volley: " + error.getMessage());
                                    realizarSincronizacionRemota(url);
                                }
                            }

                            ) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json; charset=utf-8");
                                    headers.put("Accept", "application/json");
                                    return headers;
                                }

                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8" + getParamsEncoding();
                                }
                            }
                    );
                }

            } else {
                Log.i(TAG, "No se requiere sincronización VENTA_DETALLE");
            }
            //c.close();
                c.close();
        }
    }

    /**
     * Obtiene el registro que se acaba de marcar como "pendiente por sincronizar" y
     * con "estado de sincronización"
     *
     * @return Cursor con el registro.*/
        /**
         * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente*/
    }

         private void iniciarActualizacion(String url) {
         if (url.equals(UPDATE_URL_EMPLEADOS)) {
         Uri uri = ContractParaProductos.CONTENT_URI_EMPLEADOS;
         String selection = ContractParaProductos.Columnas.PENDIENTE_INSERCION + "=? AND " + ContractParaProductos.Columnas.ESTADO + "=?";
         String[] selectionArgs = new String[]{"1", ContractParaProductos.ESTADO_OK + ""};

         ContentValues v = new ContentValues();
         v.put(ContractParaProductos.Columnas.ESTADO, ContractParaProductos.ESTADO_SYNC);

         int results = resolver.update(uri, v, selection, selectionArgs);
         Log.i(TAG, "Registros puestos en cola de inserción:" + results);
         }
         else if (url.equals(UPDATE_URL_INVENTARIO)) {
                 Uri uri = ContractParaProductos.CONTENT_URI_INVENTARIO;
                 String selection = ContractParaProductos.Columnas.PENDIENTE_INSERCION + "=? AND " + ContractParaProductos.Columnas.ESTADO + "=?";
                 String[] selectionArgs = new String[]{"1", ContractParaProductos.ESTADO_OK + ""};

                 ContentValues v = new ContentValues();
                 v.put(ContractParaProductos.Columnas.ESTADO, ContractParaProductos.ESTADO_SYNC);

                 int results = resolver.update(uri, v, selection, selectionArgs);
                 Log.i(TAG, "Registros puestos en cola de inserción:" + results);
         }
/*
         else if (url.equals(Constantes.UPDATE_URL_INVENTARIO_DETALLE)) {
         Uri uri = ContractParaProductos.CONTENT_URI_INVENTARIO_DETALLE;
         String selection = ContractParaProductos.Columnas.PENDIENTE_INSERCION + "=? AND " + ContractParaProductos.Columnas.ESTADO + "=?";
         String[] selectionArgs = new String[]{"1", ContractParaProductos.ESTADO_OK + ""};

         ContentValues v = new ContentValues();
         v.put(ContractParaProductos.Columnas.ESTADO, ContractParaProductos.ESTADO_SYNC);

         int results = resolver.update(uri, v, selection, selectionArgs);
         Log.i(TAG, "Registros puestos en cola de inserción:" + results);
         }

         else if (url.equals(Constantes.INSERT_URL_VENTA)) {
         Uri uri = ContractParaProductos.CONTENT_URI_VENTA;
         String selection = ContractParaProductos.Columnas.PENDIENTE_INSERCION + "=? AND " + ContractParaProductos.Columnas.ESTADO + "=?";
         String[] selectionArgs = new String[]{"1", ContractParaProductos.ESTADO_OK + ""};

         ContentValues v = new ContentValues();
         v.put(ContractParaProductos.Columnas.ESTADO, ContractParaProductos.ESTADO_SYNC);

         int results = resolver.update(uri, v, selection, selectionArgs);
         Log.i(TAG, "Registros puestos en cola de inserción:" + results);
         }
         else if (url.equals(Constantes.INSERT_URL_VENTA_DETALLE)) {   //Aquí esta el problema
         Uri uri = ContractParaProductos.CONTENT_URI_VENTA_DETALLE;
         String selection = ContractParaProductos.Columnas.PENDIENTE_INSERCION + "=? AND " + ContractParaProductos.Columnas.ESTADO + "=?";
         String[] selectionArgs = new String[]{"1", ContractParaProductos.ESTADO_OK + ""};

         ContentValues v = new ContentValues();
         v.put(ContractParaProductos.Columnas.ESTADO, ContractParaProductos.ESTADO_SYNC);

         int results = resolver.update(uri, v, selection, selectionArgs);
         Log.i(TAG, "Registros puestos en cola de inserción VENTA_DETALLES:" + results);
         }*/
         }


         /**
         * Limpia el registro que se sincronizó y le asigna la nueva id remota proveida
         * por el servidor
         *
         * @param idRemota id remota

         @TargetApi(Build.VERSION_CODES.KITKAT)
         @RequiresApi(api = Build.VERSION_CODES.KITKAT)
         private void finalizarActualizacion(String idRemota, int idLocal, String url, int cuenta) {     /////actualizamos lo insertado en la app

         if (url.equals(Constantes.UPDATE_URL_INVENTARIO_DETALLE)) {
         Log.i(TAG, "TERMINA INVENTARIO DETALLES "+idLocal);

         Uri uri = ContractParaProductos.CONTENT_URI_INVENTARIO_DETALLE;
         String selection = ContractParaProductos.Columnas._ID + "=?";
         String[] selectionArgs = new String[]{String.valueOf(idLocal)};

         ContentValues v = new ContentValues();
         v.put(ContractParaProductos.Columnas.PENDIENTE_INSERCION, "0");
         v.put(ContractParaProductos.Columnas.ESTADO, ContractParaProductos.ESTADO_OK);
         resolver.update(uri, v, selection, selectionArgs);

         if(idLocal==cuenta){
         conteo=1;
         realizarSincronizacionRemota(Constantes.INSERT_URL_VENTA);
         }
         }
         else if (url.equals(Constantes.INSERT_URL_VENTA)) {
         Log.i(TAG, "TERMINA VENTAS "+idLocal);

         Uri uri = ContractParaProductos.CONTENT_URI_VENTA;
         String selection = ContractParaProductos.Columnas._ID + "=?";
         String[] selectionArgs = new String[]{String.valueOf(idLocal)};

         ContentValues v = new ContentValues();
         v.put(ContractParaProductos.Columnas.PENDIENTE_INSERCION, "0");
         v.put(ContractParaProductos.Columnas.ESTADO, ContractParaProductos.ESTADO_OK);
         v.put(ContractParaProductos.Columnas.ID_REMOTA, idRemota);

         resolver.update(uri, v, selection, selectionArgs);
         if(idLocal==cuenta){
         realizarSincronizacionRemota(Constantes.INSERT_URL_VENTA_DETALLE);
         }
         }
         else if (url.equals(Constantes.INSERT_URL_VENTA_DETALLE)) {
         Log.i(TAG, "TERMINA VENTA_DETALLE "+idLocal);

         Uri uri = ContractParaProductos.CONTENT_URI_VENTA;
         String selection = ContractParaProductos.Columnas._ID + "=?";
         String[] selectionArgs = new String[]{String.valueOf(idLocal)};

         ContentValues v = new ContentValues();
         v.put(ContractParaProductos.Columnas.PENDIENTE_INSERCION, "0");
         v.put(ContractParaProductos.Columnas.ESTADO, ContractParaProductos.ESTADO_OK);
         v.put(ContractParaProductos.Columnas.ID_REMOTA, idRemota);

         resolver.update(uri, v, selection, selectionArgs);
         if(idLocal==(cuenta*2)){    //idlocal son 10      cuenta es siempre la mitad
         Log.i(TAG, "RECREA BD");
         Sincronizar.carritos.setEnabled(true);
         Sincronizar.carritos.setAdapter(null);


         Sincronizar.importar.setEnabled(true);
         Sincronizar.exportar.setEnabled(false);
         Sincronizar.buscar.setEnabled(true);

         Sincronizar.importar.getBackground().setColorFilter(null);  //habilitado
         Sincronizar.exportar.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP); ///deshabilitado
         Sincronizar.buscar.getBackground().setColorFilter(null);  //habilitado

         values.put(ContractParaProductos.Columnas.IMPORTADO, 1);
         database.update("estados", values, null, null);

         Sincronizar.progressDialog.dismiss();
         Toast.makeText(getContext(), "Tus datos han sido subidos", Toast.LENGTH_LONG).show();
         DatabaseHelper admin=new DatabaseHelper(getContext(), ProviderDeProductos.DATABASE_NAME, null, ProviderDeProductos.DATABASE_VERSION);
         DatabaseHelper.limpiar(admin.getWritableDatabase());
         }
         }
         }

         /**
          * Procesa los diferentes tipos de respuesta obtenidos del servidor
          *
          * @param response Respuesta en formato Json

         @TargetApi(Build.VERSION_CODES.KITKAT)
         @RequiresApi(api = Build.VERSION_CODES.KITKAT)*/
         @SuppressLint("Recycle") private void procesarRespuestaInsert(JSONObject response, int idLocal, String url, int cuenta) {
         ///////////////////////////////obtenemos los datos por php//////////////////////////////////////////////////////////////////////////////////////////////////////////
         Cursor consulta;
         if (url.equals(UPDATE_URL_EMPLEADOS)) {
         try {
         values=new ContentValues();
         // Obtener estado
         String estado = response.getString(Constantes.ESTADO);
         // Obtener mensaje
         String mensaje = response.getString(Constantes.MENSAJE);
         // Obtener identificador del nuevo registro creado en el servidor
         String idRemota = response.getString(Constantes.ID_EMPLEADOS);

         switch (estado) {
         case Constantes.SUCCESS:
         Log.i(TAG, mensaje +" "+ idRemota); ////muestra el " creacion exitosa"

         break;

         case Constantes.FAILED:
         Log.i(TAG, mensaje +" "+ idRemota);
         break;
         }
         } catch (JSONException e) {
         e.printStackTrace();
         }

         }
         if (url.equals(UPDATE_URL_INVENTARIO)) {
                 try {
                     values=new ContentValues();
                     // Obtener estado
                     String estado = response.getString(Constantes.ESTADO);
                     // Obtener mensaje
                     String mensaje = response.getString(Constantes.MENSAJE);
                     // Obtener identificador del nuevo registro creado en el servidor
                     String idRemota = response.getString(Constantes.ID_INVENTARIO);

                     switch (estado) {
                         case Constantes.SUCCESS:
                             Log.i(TAG, mensaje +" "+ idRemota); ////muestra el " creacion exitosa"

                             break;

                         case Constantes.FAILED:
                             Log.i(TAG, mensaje +" "+ idRemota);
                             break;
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

             }
         /*else if (url.equals(UPDATE_URL_INVENTARIO_DETALLE)) {
         try {
         values=new ContentValues();
         // Obtener estado
         String estado = response.getString(Constantes.ESTADO);
         // Obtener mensaje
         String mensaje = response.getString(Constantes.MENSAJE);

         switch (estado) {
         case Constantes.SUCCESS:
         Log.i(TAG, mensaje); ////muestra el " creacion exitosa"
         break;

         case Constantes.FAILED:
         finalizarActualizacion(null, idLocal, url, cuenta);
         break;
         }
         } catch (JSONException e) {
         e.printStackTrace();
         }

         }
         else if (url.equals(Constantes.INSERT_URL_VENTA)) {
         try {
         values = new ContentValues();
         // Obtener estado
         String estado = response.getString(Constantes.ESTADO);
         // Obtener mensaje
         String mensaje = response.getString(Constantes.MENSAJE);

         Log.i(TAG, mensaje);

         // Obtener identificador del nuevo registro creado en el servidor
         String idRemota = response.getString(Constantes.ID_VENTA);


         ///creacion de venta_detalles
         Log.i(TAG, "Conteo "+conteo);
         ///idRemota es idventa
         consulta = database.rawQuery("select * from venta_detalles where idRemota='"+conteo+"' and pendiente_insercion=0", null);
         if (consulta.getCount()  > 0) {
         while (consulta.moveToNext()) {
         values.put("idRemota", Integer.parseInt(idRemota));
         values.put("cantidad", consulta.getString(1));
         values.put("idproducto", consulta.getString(3));
         values.put("precio", consulta.getDouble(2));
         values.put(ContractParaProductos.Columnas.PENDIENTE_INSERCION, 1);
         database.insertOrThrow("venta_detalles", null, values);
         Log.i("Datos", String.valueOf(values));    ////mostramos que valores se han insertado
         }
         }
         conteo++;

         switch (estado) {
         case Constantes.SUCCESS:
         Log.i(TAG, mensaje);
         finalizarActualizacion(idRemota, idLocal, url, cuenta);
         break;

         case Constantes.FAILED:
         Log.i(TAG, mensaje);
         break;
         }
         } catch (JSONException e) {
         e.printStackTrace();
         }

         }*/
         /*else if (url.equals(INSERT_URL_VENTA_DETALLE)) {
         try {
         values=new ContentValues();
         // Obtener estado
         String estado = response.getString(Constantes.ESTADO);
         // Obtener mensaje
         String mensaje = response.getString(Constantes.MENSAJE);

         switch (estado) {
         case Constantes.SUCCESS:
         break;

         case Constantes.FAILED:
         finalizarActualizacion(null, idLocal, url, cuenta);
         break;
         }
         } catch (JSONException e) {
         e.printStackTrace();
         }

         }

         }*/
    }
}
//}



