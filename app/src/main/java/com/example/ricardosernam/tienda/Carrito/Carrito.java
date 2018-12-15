package com.example.ricardosernam.tienda.Carrito;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.Ventas.ProductosVenta_class;
import com.example.ricardosernam.tienda.Ventas.Productos_class;
import com.example.ricardosernam.tienda.Ventas.Ventas;
import com.example.ricardosernam.tienda.Ventas.VentasAdapter;
import com.example.ricardosernam.tienda.Ventas.cantidad_producto_DialogFragment;
import com.example.ricardosernam.tienda._____interfazes.actualizado;

import java.text.SimpleDateFormat;


public class Carrito extends Fragment {
    private Cursor datosSeleccionado;
    private SQLiteDatabase db;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private android.support.v4.app.FragmentManager fm;
    private RecyclerView.LayoutManager lManager;
    private TextView total;
    private Button aceptar, eliminar, cerrar;

    public Carrito() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_carrito, container, false);
        aceptar=view.findViewById(R.id.BtnAceptarCompra);
        eliminar=view.findViewById(R.id.BtnEliminarCompra);
        cerrar=view.findViewById(R.id.BtnCerrarCarrito);
        recycler = view.findViewById(R.id.RVproductosCarrito); ///declaramos el recycler
        total = view.findViewById(R.id.TVtotal); ///declaramos el recycle
        fm=getFragmentManager();


        DatabaseHelper admin=new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db=admin.getWritableDatabase();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new pagar_DialogFragment(Float.parseFloat(String.valueOf(total.getText()))).show(fm, "Producto_ventas");
                }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder aceptarVenta = new AlertDialog.Builder(getContext());
                aceptarVenta .setTitle("Cuidado");
                aceptarVenta .setMessage("¿Seguro que quieres eliminar este producto?");
                aceptarVenta .setCancelable(false);
                aceptarVenta .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface aceptarVenta , int id) {
                        aceptar_cancelar(fm);
                        aceptarVenta.dismiss();
                    }
                });
                aceptarVenta .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface aceptarVenta, int id) {
                        aceptarVenta .dismiss();
                    }
                });
                aceptarVenta .show();
            }
        });
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Ventas(), "Ventas").commit(); ///cambio de fragment
                getFragmentManager().beginTransaction().replace(R.id.LLprincipal, getFragmentManager().findFragmentByTag("Ventas")).addToBackStack("Ventas").commit(); ///cambio de fragment

            }
        });
        rellenado_total();
        calcularTotal();
        return view;

    }
    public static void aceptar_cancelar(FragmentManager fm){
        ContractParaProductos.itemsProductosVenta.removeAll(ContractParaProductos.itemsProductosVenta);
        fm.beginTransaction().replace(R.id.LLprincipal, fm.findFragmentByTag("Ventas")).addToBackStack("Ventas").commit(); ///cambio de fragment
    }
    public void rellenado_total(){  ////volvemos a llenar el racycler despues de actualizar, o de una busqueda
        fm=getFragmentManager();
        adapter = new CarritosAdapter(ContractParaProductos.itemsProductosVenta, getContext(), new actualizado() {
            @Override
            public void actualizar(int cantidad, String nombre) {
                datosSeleccionado=db.rawQuery("select precio, codigo_barras, idRemota from inventario where nombre_producto='"+nombre+"'" ,null);
                if(datosSeleccionado.moveToFirst()) {
                    if(datosSeleccionado.getString(1)==null) {  ///fruta
                        ContractParaProductos.itemsProductosVenta.add(new ProductosVenta_class(nombre, cantidad, datosSeleccionado.getFloat(0),0, (cantidad/1000)*datosSeleccionado.getFloat(0), datosSeleccionado.getInt(2)));//obtenemos el cardview seleccionado y lo agregamos a items2
                    }
                    else{   //pieza
                        ContractParaProductos.itemsProductosVenta.add(new ProductosVenta_class(nombre, cantidad, datosSeleccionado.getFloat(0),1, cantidad*datosSeleccionado.getFloat(0), datosSeleccionado.getInt(2)));//obtenemos el cardview seleccionado y lo agregamos a items2
                    }
                }
                ///comprobamos si se repite
                for(int i=0; i<ContractParaProductos.itemsProductosVenta.size(); i++) {
                    String dato=ContractParaProductos.itemsProductosVenta.get(i).getNombre();
                    for(int j=i+1; j<ContractParaProductos.itemsProductosVenta.size(); j++) {
                        if(dato.equals(ContractParaProductos.itemsProductosVenta.get(j).getNombre())){  ///si se repite
                            if(ContractParaProductos.itemsProductosVenta.get(j).getCantidad()==0){  ///si es cero, lo eliminamos de la lista
                                ContractParaProductos.itemsProductosVenta.remove(j);   ////eliminamos el previamente gregado
                                ContractParaProductos.itemsProductosVenta.remove(i);   ////eliminamos el previamente agregado
                            }
                            else {
                                ContractParaProductos.itemsProductosVenta.remove(i);   ////eliminamos el previamente agregado
                            }
                        }
                    }
                }
                calcularTotal();
            }
        });///llamamos al adaptador y le enviamos el array como parametro
        lManager = new LinearLayoutManager(this.getActivity());  //declaramos el layoutmanager
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void calcularTotal(){
        float suma=0;
        for(int i=0; i<ContractParaProductos.itemsProductosVenta.size(); i++){
            suma=suma+ContractParaProductos.itemsProductosVenta.get(i).getSubtotal();
            total.setText(String.valueOf(suma));
        }
    }
}
