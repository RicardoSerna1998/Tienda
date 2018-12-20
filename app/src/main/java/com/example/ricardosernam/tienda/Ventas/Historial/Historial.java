package com.example.ricardosernam.tienda.Ventas.Historial;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ricardosernam.tienda.Carrito.Carrito;
import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.Ventas.Escanner;
import com.example.ricardosernam.tienda.Ventas.Productos_class;
import com.example.ricardosernam.tienda.Ventas.Ventas;
import com.example.ricardosernam.tienda.Ventas.VentasAdapter;
import com.example.ricardosernam.tienda.Ventas.cantidad_producto_DialogFragment;

import java.util.ArrayList;

public class Historial extends Fragment {
    private Cursor fila, filaEmpleado;
    private SQLiteDatabase db;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private android.support.v4.app.FragmentManager fm;
    private RecyclerView.LayoutManager lManager;
    public static ArrayList<Historial_class> itemsHistorial= new ArrayList <>(); ///Arraylist que contiene los productos///
    public Button cerrar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_historial, container, false);
        DatabaseHelper admin=new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db=admin.getWritableDatabase();
        cerrar=view.findViewById(R.id.BtnCerrarHistorial);
        recycler = view.findViewById(R.id.RVhistorial); ///declaramos el recycler
        fm=getActivity().getSupportFragmentManager();
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Ventas(), "Ventas").commit(); ///cambio de fragment
                fm.beginTransaction().replace(R.id.LLprincipal, fm.findFragmentByTag("Ventas")).addToBackStack("Ventas").commit(); ///cambio de fragment
            }
        });
        rellenado_total();
        return view;
    }
    public void rellenado_total(){  ////volvemos a llenar el racycler despues de actualizar, o de una busqueda
        itemsHistorial.clear();
        fm=getFragmentManager();
        fila=db.rawQuery("select id_empleado, fecha from ventas order by fecha desc" ,null);

        if(fila.moveToFirst()) {///si hay un elemento
            filaEmpleado=db.rawQuery("select nombre_empleado from empleados where idRemota='"+fila.getInt(0)+"'" ,null);

            if(filaEmpleado.moveToFirst()) {///si hay un elemento
                itemsHistorial.add(new Historial_class(fila.getString(0), fila.getString(1)));
            }

            while (fila.moveToNext()) {
                filaEmpleado=db.rawQuery("select nombre_empleado from empleados where idRemota='"+fila.getInt(0)+"'" ,null);
                while (filaEmpleado.moveToNext()) {
                    itemsHistorial.add(new Historial_class(fila.getString(0), fila.getString(1)));
                }
            }
        }
        adapter = new HistorialAdapter(getContext(), itemsHistorial);///llamamos al adaptador y le enviamos el array como parametro
        lManager = new LinearLayoutManager(this.getActivity());  //declaramos el layoutmanager
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
