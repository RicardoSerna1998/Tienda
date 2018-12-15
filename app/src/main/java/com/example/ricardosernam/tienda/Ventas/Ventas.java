package com.example.ricardosernam.tienda.Ventas;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.ricardosernam.tienda.Empleados.Empleados_ventasAdapter;
import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.Ventas.Historial.Historial;

import java.util.ArrayList;

public class Ventas extends Fragment {
    private SearchView nombreCodigo;
    private Cursor fila, filaBusqueda, datoEscaneado;
    private SQLiteDatabase db;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private android.support.v4.app.FragmentManager fm;
    private RecyclerView.LayoutManager lManager;
    private Button escanear, carrito, historial;

    private ArrayList<Productos_class> itemsProductos= new ArrayList <>(); ///Arraylist que contiene los productos///

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_ventas, container, false);
        DatabaseHelper admin=new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db=admin.getWritableDatabase();
        recycler = view.findViewById(R.id.RVproductosVenta); ///declaramos el recycler
        escanear= view.findViewById(R.id.BtnEscanearProducto);
        carrito= view.findViewById(R.id.BtnCarrito);
        historial= view.findViewById(R.id.BtnHistorial);

        escanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Escanner.class);//intanciando el activity del scanner
                startActivityForResult(intent,2);//inicializar el activity con RequestCode2
            }
        });

        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContractParaProductos.itemsProductosVenta.isEmpty()){
                    Toast.makeText(getContext(), "No hay productos comprados aun", Toast.LENGTH_SHORT).show();
                }
                else{
                    getFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Carrito(), "Carrito").commit(); ///cambio de fragment
                }
            }
        });

        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Historial(), "Historial").commit(); ///cambio de fragment

            }
        });

        ///buscador
        nombreCodigo=view.findViewById(R.id.ETnombreProducto);
        nombreCodigo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(!(TextUtils.isEmpty(newText))) {   ///el campo tiene algo
                    //if ((TextUtils.isDigitsOnly(newText))) {  ///si el campo tiene tan solo numeros es un codigo
                        filaBusqueda = db.rawQuery("select nombre_producto, precio, codigo_barras, existentes from inventario where nombre_producto='" + newText + "'", null);
                    //}
                    if (filaBusqueda.moveToFirst()) { ///si hay un elemento
                        itemsProductos.removeAll(itemsProductos);
                        itemsProductos.add(new Productos_class(filaBusqueda.getString(0), filaBusqueda.getFloat(1), filaBusqueda.getString(2), filaBusqueda.getFloat(3)));
                        while (filaBusqueda.moveToNext()) {
                            itemsProductos.add(new Productos_class(filaBusqueda.getString(0), filaBusqueda.getFloat(1), filaBusqueda.getString(2), filaBusqueda.getFloat(3)));
                        }
                    }
                    else{ ///El producto no existe
                        Toast.makeText(getContext(), "Producto inexistente", Toast.LENGTH_SHORT).show();
                    }
                }  ////esta vacio
                else {
                    rellenado_total();
                }
                adapter.notifyDataSetChanged();
                return false;
            }

        });
        rellenado_total();
        return view;
    }
    public void rellenado_total(){  ////volvemos a llenar el racycler despues de actualizar, o de una busqueda
        fm=getFragmentManager();
        fila=db.rawQuery("select nombre_producto, precio, codigo_barras, existentes from inventario order by codigo_barras" ,null);

        if(fila.moveToFirst()) {///si hay un elemento
            itemsProductos.removeAll(itemsProductos);
            itemsProductos.add(new Productos_class(fila.getString(0), fila.getFloat(1), fila.getString(2), fila.getFloat(3)));
            while (fila.moveToNext()) {
                itemsProductos.add(new Productos_class(fila.getString(0), fila.getFloat(1), fila.getString(2), fila.getFloat(3)));
            }
        }
        adapter = new VentasAdapter(itemsProductos, fm, getContext());///llamamos al adaptador y le enviamos el array como parametro
        lManager = new LinearLayoutManager(this.getActivity());  //declaramos el layoutmanager
        //lManager = new GridLayoutManager(this.getActivity(), 2);  //declaramos el layoutmanager
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && data != null) {
            //obtener resultados
            datoEscaneado=db.rawQuery("select nombre_producto, precio from inventario where codigo_barras='"+data.getStringExtra("BARCODE")+"'" ,null);
            if(datoEscaneado.moveToFirst()) {
                new cantidad_producto_DialogFragment(datoEscaneado.getString(0), datoEscaneado.getFloat(1), 1).show(fm, "Producto_ventas");
            }
            else{
                Toast.makeText(getContext(), "Este producto no esta registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
