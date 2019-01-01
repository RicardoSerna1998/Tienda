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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

;import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.utils.Constantes;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

@SuppressLint("ValidFragment")
public class Empleados extends Fragment {     /////Fragment de categoria ventas
    public static Cursor empleados;
    public static RecyclerView recycler;
    public static RecyclerView.Adapter adapter;
    public static RecyclerView.LayoutManager lManager;
    public static android.support.v4.app.FragmentManager fm;
    public static SQLiteDatabase db;
    public static Button establecer;
    public static EditText ip;
    public static Context context;
    private static ArrayList<Empleados_class> itemsEmpleados = new ArrayList<>();  ///Arraylist que contiene los cardviews seleccionados de productos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);
        onViewCreated(view, savedInstanceState);

        DatabaseHelper admin = new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db = admin.getWritableDatabase();
        context=getContext();
        recycler = view.findViewById(R.id.RVempleados); ///declaramos el recycler
        fm = getFragmentManager();
        ip = view.findViewById(R.id.ETip);
        establecer = view.findViewById(R.id.BtnEstablecer);

        relleno();
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
        return view;
    }

    public static void relleno() {    ///llamamos el adapter del recycler
        itemsEmpleados.clear();

        empleados = db.rawQuery("select nombre_empleado, tipo_empleado, activo, codigo from empleados ORDER by activo desc", null);
        if (empleados.moveToFirst()) {///si hay un elemento
            Toast.makeText(context, "Entra", LENGTH_LONG).show();
            itemsEmpleados.add(new Empleados_class(empleados.getString(0), empleados.getString(1), empleados.getInt(2), empleados.getString(3)));
            while (empleados.moveToNext()) {
                itemsEmpleados.add(new Empleados_class(empleados.getString(0), empleados.getString(1), empleados.getInt(2), empleados.getString(3)));
            }
        }
        adapter = new Empleados_ventasAdapter(itemsEmpleados, fm, context);///llamamos al adaptador y le enviamos el array como parametro
        lManager = new LinearLayoutManager(context);  //declaramos el layoutmanager
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }

    //public static void relleno(Context context) {    ///llamamos el adapter del recycler

    //}
}
