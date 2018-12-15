package com.example.ricardosernam.tienda.Empleados;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

;import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class Empleados extends Fragment {     /////Fragment de categoria ventas
    private static Cursor empleados;
    private static RecyclerView recycler;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView.LayoutManager lManager;
    private static android.support.v4.app.FragmentManager fm;
    private static SQLiteDatabase db;
    private static ArrayList<Empleados_class> itemsEmpleados = new ArrayList<>();  ///Arraylist que contiene los cardviews seleccionados de productos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_empleados, container, false);
        onViewCreated(view, savedInstanceState);
        DatabaseHelper admin=new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db=admin.getWritableDatabase();
        recycler = view.findViewById(R.id.RVempleados); ///declaramos el recycler
        fm=getFragmentManager();

        relleno(getContext());
        return view;
    }
   public static void relleno(Context context){    ///llamamos el adapter del recycler
        itemsEmpleados.clear();
        empleados=db.rawQuery("select nombre_empleado, tipo_empleado, activo, codigo from empleados ORDER by activo desc",null);
        if(empleados.moveToFirst()) {///si hay un elemento
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
}
