package com.example.ricardosernam.tienda.Empleados;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
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
    private Cursor empleados;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private android.support.v4.app.FragmentManager fm;
    private FragmentManager fm2;
    private SQLiteDatabase db;
    private ArrayList<Empleados_class> itemsEmpleados = new ArrayList<>();  ///Arraylist que contiene los cardviews seleccionados de productos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_empleados, container, false);
        onViewCreated(view, savedInstanceState);
        DatabaseHelper admin=new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db=admin.getWritableDatabase();
        recycler = view.findViewById(R.id.RVempleados); ///declaramos el recycler
        relleno();
        return view;
    }
   public void relleno(){    ///llamamos el adapter del recycler
       empleados=db.rawQuery("select nombre_empleado, tipo_empleado,  activo from empleados ORDER by activo desc",null);
        fm=getFragmentManager();

       if(empleados.moveToFirst()) {///si hay un elemento
           itemsEmpleados.add(new Empleados_class(empleados.getString(0), empleados.getString(1), empleados.getInt(2)));
           while (empleados.moveToNext()) {
               itemsEmpleados.add(new Empleados_class(empleados.getString(0), empleados.getString(1), empleados.getInt(2)));
           }
       }
        adapter = new Empleados_ventasAdapter(itemsEmpleados, fm, getContext());///llamamos al adaptador y le enviamos el array como parametro
        lManager = new LinearLayoutManager(this.getActivity());  //declaramos el layoutmanager
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }
}
