package com.example.ricardosernam.tienda.Carrito;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.ricardosernam.tienda._____interfazes.actualizado;

import java.text.SimpleDateFormat;


public class Carrito extends Fragment {
    private Cursor fila, datosSeleccionado;
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
        eliminar=view.findViewById(R.id.BtnAceptarCompra);
        cerrar=view.findViewById(R.id.BtnCerrarCarrito);
        recycler = view.findViewById(R.id.RVproductosCarrito); ///declaramos el recycler
        total = view.findViewById(R.id.TVtotal); ///declaramos el recycle

        DatabaseHelper admin=new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db=admin.getWritableDatabase();


        /*aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                values = new ContentValues();
                /////obtener fecha actual
                java.util.Calendar c = java.util.Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formattedDate = df.format(c.getTime());

                carrito = db.rawQuery("select idRemota, ubicacion, vendedor from carritos", null);
                inventario = db.rawQuery("select idRemota from inventarios", null);

                if (carrito.moveToFirst()) {
                    values.put("idcarrito", carrito.getString(0));
                    values.put("ubicacion", carrito.getString(1));
                    values.put("vendedor", carrito.getString(2));
                }
                if (inventario.moveToFirst()) {
                    values.put("idinventario", inventario.getString(0));
                }
                values.put("fecha", formattedDate);
                values.put(ContractParaProductos.Columnas.PENDIENTE_INSERCION, 1);
                db.insertOrThrow("ventas", null, values);


/////////////////////////////////incersion-modificación ventas-inventario_detalles
                values2 = new ContentValues();
                for (int i = 0; i < itemsCobrar.size(); i++) {
                    ////////////////venta detalles/////////////////////////////77
                    venta = db.rawQuery("select _id from ventas", null);
                    if (venta.moveToFirst()) {
                        venta.moveToLast();
                        values2.put("idRemota", venta.getString(0));
                        values2.put("cantidad", itemsCobrar.get(i).getCantidad());
                        values2.put("idproducto", itemsCobrar.get(i).getIdRemota());
                        values2.put("precio", itemsCobrar.get(i).getPrecio());
                        db.insertOrThrow("venta_detalles", null, values2);
                        Log.i("Datos", String.valueOf(values2));    ////mostramos que valores se han insertado
                    }
                    //////////////////////////////////////////inventario detalles//////////////////////////////
                    values3 = new ContentValues();
                    if (itemsCobrar.get(i).getPorcion() != 0) {  //////  es Preparado
                        ///obtenemos el guisado donde tenemos que descontar
                        existente = db.rawQuery("select idproducto, inventario_final from inventario_detalles WHERE idproducto=(select idRemota from productos where nombre=(select guisado from productos where nombre='" + itemsCobrar.get(i).getNombre() + "'))", null);
                        if (existente.moveToFirst()) {
                            float porcion = existente.getFloat(1) - (itemsCobrar.get(i).getCantidad() * itemsCobrar.get(i).getPorcion());
                            values3.put("inventario_final", porcion);
                            db.update("inventario_detalles", values3, "idproducto='" + existente.getString(0) + "'", null);
                        }
                    } else {   //////  es Pieza
                        existente = db.rawQuery("select idproducto, inventario_final from inventario_detalles WHERE idproducto='" + itemsCobrar.get(i).getIdRemota() + "'", null);
                        if (existente.moveToFirst()) {
                            float porcion = existente.getFloat(1) - itemsCobrar.get(i).getCantidad();
                            values3.put("inventario_final", porcion);
                            db.update("inventario_detalles", values3, "idproducto='" + existente.getString(0) + "'", null);
                        }

                    }
                }

                Toast.makeText(getContext(), "Venta exitosa", Toast.LENGTH_LONG).show();
                itemsProductos.removeAll(itemsProductos);
            }
        });*/
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Ventas()).commit(); ///cambio de fragment
            }
        });
        rellenado_total();
        calcularTotal();
        return view;

    }
    public void rellenado_total(){  ////volvemos a llenar el racycler despues de actualizar, o de una busqueda
        fm=getFragmentManager();
        adapter = new CarritosAdapter(ContractParaProductos.itemsProductosVenta, fm, new actualizado() {
            @Override
            public void actualizar(int cantidad, String nombre) {
                datosSeleccionado=db.rawQuery("select precio, codigo_barras from inventario where nombre_producto='"+nombre+"'" ,null);
                if(datosSeleccionado.moveToFirst()) {
                    if(datosSeleccionado.getString(1)==null) {  ///fruta
                        ContractParaProductos.itemsProductosVenta.add(new ProductosVenta_class(nombre, cantidad, datosSeleccionado.getFloat(0),0, (cantidad/1000)*datosSeleccionado.getFloat(0)));//obtenemos el cardview seleccionado y lo agregamos a items2
                    }
                    else{   //pieza
                        ContractParaProductos.itemsProductosVenta.add(new ProductosVenta_class(nombre, cantidad, datosSeleccionado.getFloat(0),1, cantidad*datosSeleccionado.getFloat(0)));//obtenemos el cardview seleccionado y lo agregamos a items2
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
