package com.example.ricardosernam.tienda;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricardosernam.tienda.Empleados.Empleados;
import com.example.ricardosernam.tienda.provider.ContractParaProductos;
import com.example.ricardosernam.tienda.ventas.Ventas;
import com.example.ricardosernam.tienda.sync.SyncAdapter;
import com.example.ricardosernam.tienda.utils.Constantes;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    public static TextView empleadoActivo;
    private SQLiteDatabase db;
    private Cursor activos,  empleadoEnCaja;
    public static Toolbar toolbar;
    public static android.support.v7.app.ActionBar bar;



//////////////////////////////////////////////////////////////////////////////77
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper admin = new DatabaseHelper(getApplicationContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db = admin.getWritableDatabase();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Empleados(), "Empleados").addToBackStack("Empleados").commit(); ///cambio de fragment
        }
        empleadoEnCaja= db.rawQuery("select nombre_empleado from empleados where tipo_empleado='Admin.' and activo=1 or tipo_empleado='Cajero' and activo=1", null);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        empleadoActivo = findViewById(R.id.TVempleadoActivo);
        if(empleadoEnCaja.moveToFirst()){
            MainActivity.empleadoActivo.setText("Cajer@: "+empleadoEnCaja.getString(0));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        activos= db.rawQuery("select * from empleados where tipo_empleado='Admin.' and activo=1 or tipo_empleado='Cajero' and activo=1", null);

        if (id == R.id.action_user) {
            if(activos.moveToFirst()){   ////hay alguien activo en caja
                if(getSupportFragmentManager().findFragmentByTag("Empleados").isVisible()){  //estoy en empleados
                    getSupportFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Ventas(), "Ventas").addToBackStack("Ventas").commit(); ///cambio de fragment
                    //item.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_supervisor_account_black_24dp));
                }
                else{  ///no estoy en empleados
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Empleados(), "Empleados").addToBackStack("Empleados").commit(); ///cambio de fragment
                }
            }
            return true;
        }
        else if (id == R.id.sync_data) {   ////SINCRONIZACIÓN DE DATOS
            if(Empleados.ip.isEnabled()){
                Toast.makeText(getApplicationContext(), "Establece la IP", LENGTH_LONG).show();
            } else{
                //SyncAdapter.inicializarSyncAdapter(getApplicationContext(), Constantes.GET_URL_EMPLEADOS, null);
                //SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.GET_URL_EMPLEADOS);
                SyncAdapter.sincronizarAhora(getApplicationContext(), false, Constantes.GET_URL_INVENTARIO);

            }
            return true;
        }
        else if (id == android.R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.LLprincipal, getSupportFragmentManager().findFragmentByTag("Ventas")).addToBackStack("Ventas").commit(); ///cambio de fragment
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            return true;
        }
            return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {  ///anulamos el onBackPressed
    }


}
