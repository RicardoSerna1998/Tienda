package com.example.ricardosernam.tienda;

import android.Manifest;
import android.app.ActionBar;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest.permission.*;
import com.example.ricardosernam.tienda.Empleados.Empleados;
import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.Ventas.Ventas;
import com.example.ricardosernam.tienda.sync.SyncAdapter;
import com.example.ricardosernam.tienda.utils.Constantes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
                SyncAdapter.inicializarSyncAdapter(getApplicationContext(), Constantes.GET_URL_EMPLEADOS, null);
                SyncAdapter.sincronizarAhora(getApplicationContext(), false, null);
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
