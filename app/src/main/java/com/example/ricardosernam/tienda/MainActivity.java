package com.example.ricardosernam.tienda;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricardosernam.tienda.Empleados.Empleados;

public class MainActivity extends AppCompatActivity {
    public static TextView empleadoActivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Empleados()).commit(); ///cambio de fragment
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        empleadoActivo= findViewById(R.id.TVempleadoActivo);
        int x;
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            for (x = 0; x < backStackCount; x++){
                Toast.makeText(getApplicationContext(), getSupportFragmentManager().getBackStackEntryAt(x).getName(), Toast.LENGTH_LONG ).show();
                //getSupportFragmentManager().getBackStackEntryAt(x).getName();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user) {
           getSupportFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Empleados()).commit(); ///cambio de fragment
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
