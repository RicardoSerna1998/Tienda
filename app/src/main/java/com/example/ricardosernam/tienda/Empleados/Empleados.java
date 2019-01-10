package com.example.ricardosernam.tienda.Empleados;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

;import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda._____interfazes.actualizado;
import com.example.ricardosernam.tienda.provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.sync.SyncAdapter;
import com.example.ricardosernam.tienda.utils.Constantes;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_LONG;

@SuppressLint("ValidFragment")
public class Empleados extends Fragment {     /////Fragment de categoria ventas
    public static Cursor empleados, informacion;
    @SuppressLint("StaticFieldLeak")
    public static RecyclerView recycler;
    public static RecyclerView.Adapter adapter;
    public static RecyclerView.LayoutManager lManager;
    public static android.support.v4.app.FragmentManager fm;
    public static SQLiteDatabase db;
    public static TextView nombre, direccion, telefono;
    @SuppressLint("StaticFieldLeak")
    public static Button establecer;
    public static ImageButton sync;
    @SuppressLint("StaticFieldLeak")
    public static EditText ip;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private static ArrayList<Empleados_class> itemsEmpleados = new ArrayList<>();  ///Arraylist que contiene los cardviews seleccionados de productos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);
        onViewCreated(view, savedInstanceState);

        context=getContext();
        fm = getFragmentManager();
        ip = view.findViewById(R.id.ETip);
        nombre= view.findViewById(R.id.TVnombreNegocio);
        direccion= view.findViewById(R.id.TVdireccionNegocio);
        telefono= view.findViewById(R.id.TVtelefonoNegocio);

        establecer = view.findViewById(R.id.BtnEstablecer);
        sync = view.findViewById(R.id.BtnSync);
        recycler = view.findViewById(R.id.RVempleados); ///declaramos el recycler

        establecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (establecer.getText().equals(" Establecer IP ")) {  //validamos que no este vacio
                    if (TextUtils.isEmpty(ip.getText().toString().trim())) {
                        Toast.makeText(getContext(), "Ingresa un valor", LENGTH_LONG).show();
                    } else {
                        establecer.setText(" Modificar IP ");
                        ip.setEnabled(false);
                        ///guardamo el estado de la pantalla
                        //values.put(ContractParaProductos.Columnas.IP,  String.valueOf(ip.getText()));
                        //db.update("estados", values, null, null);
                        new Constantes("http://" + String.valueOf(ip.getText()));
                    }
                } else {
                    establecer.setText(" Establecer IP ");
                    ip.setEnabled(true);
                }
            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Empleados.ip.isEnabled()){
                    Toast.makeText(getContext(), "Establece la IP", LENGTH_LONG).show();
                } else {
                    SyncAdapter.sincronizarAhora(getContext(), false, 0, Constantes.GET_URL_INFORMACION);
                }
            }
        });
        relleno(getContext());
        return view;
    }

    public static void relleno(Context context) {    ///llamamos el adapter del recycler
        itemsEmpleados.clear();
        DatabaseHelper admin = new DatabaseHelper(context, ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db = admin.getWritableDatabase();

        empleados = db.rawQuery("select nombre_empleado, tipo_empleado, activo, codigo from empleados ORDER by tipo_empleado, activo desc", null);
        informacion= db.rawQuery("select nombre_negocio, direccion, telefono from informacion", null);
        if(informacion.moveToFirst()){
            if(!informacion.getString(0).isEmpty()){
                nombre.setVisibility(View.VISIBLE);
                nombre.setText(informacion.getString(0));
            }
            else{
                nombre.setVisibility(View.GONE);
                nombre.setText("");
            }
            if(!informacion.getString(1).isEmpty()){
                direccion.setVisibility(View.VISIBLE);
                direccion.setText(informacion.getString(1));
            }
            else{
                direccion.setVisibility(View.GONE);
                direccion.setText("");
            }
            if(!informacion.getString(2).isEmpty()){
                telefono.setVisibility(View.VISIBLE);
                telefono.setText(informacion.getString(2));
            }
            else{
                telefono.setVisibility(View.GONE);
                telefono.setText("");
            }
        }
        if (empleados.moveToFirst()) {///si hay un elemento

            itemsEmpleados.add(new Empleados_class(empleados.getString(0), empleados.getString(1), empleados.getInt(2), empleados.getString(3)));
            while (empleados.moveToNext()) {
                itemsEmpleados.add(new Empleados_class(empleados.getString(0), empleados.getString(1), empleados.getInt(2), empleados.getString(3)));
            }
        }
        adapter = new Empleados_ventasAdapter(itemsEmpleados, fm);///llamamos al adaptador y le enviamos el array como parametro
        lManager = new LinearLayoutManager(context);  //declaramos el layoutmanager
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

}
