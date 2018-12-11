package com.example.ricardosernam.tienda.Carrito;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.Ventas.ProductosVenta_class;
import com.example.ricardosernam.tienda.Ventas.Productos_class;
import com.example.ricardosernam.tienda.Ventas.cantidad_producto_DialogFragment;
import com.example.ricardosernam.tienda._____interfazes.actualizado;

import java.util.ArrayList;

public class CarritosAdapter extends RecyclerView.Adapter <CarritosAdapter.Productos_ventasViewHolder>{  ///adaptador para el Fragmet Ventas
    private ArrayList<ProductosVenta_class> itemsProductosVenta;
    private actualizado Interfaz;
    private FragmentManager fm;
    private Context context;


    public CarritosAdapter(ArrayList<ProductosVenta_class> itemsProductosVenta, FragmentManager fm, Context context) {  ///recibe el arrayProductos como parametro y la interface
        this.itemsProductosVenta=itemsProductosVenta;
        this.fm=fm;
        this.context=context;
    }
    public  class Productos_ventasViewHolder extends RecyclerView.ViewHolder{    ////clase donde van los elementos del cardview
        // Campos respectivos de un item
        public TextView producto, precio, unidad, subtotal;
        public EditText cantidad;
        public Productos_ventasViewHolder(final View v) {   ////lo que se programe aqui es para cuando se le de clic a un item del recycler
            super(v);
            producto = v.findViewById(R.id.TVproductoCarrito);  ////Textview donde se coloca el nombre del producto
            unidad = v.findViewById(R.id.TVunidadCarrito);  ////Textview donde se coloca el nombre del producto
            precio=v.findViewById(R.id.TVprecioCarrito);
            subtotal=v.findViewById(R.id.TVsubTotalCarrito);
            cantidad=v.findViewById(R.id.ETcantidadCarrito);
            }
    }

    @Override
    public int getItemCount() {
        return itemsProductosVenta.size();
    }

    @Override
    public Productos_ventasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tarjeta_productos_carrito, viewGroup, false);
        return new Productos_ventasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Productos_ventasViewHolder holder, final int position) {
        holder.producto.setText(itemsProductosVenta.get(position).getNombre());
        holder.precio.setText("$"+String.valueOf(itemsProductosVenta.get(position).getPrecio()));
        holder.cantidad.setText(String.valueOf(itemsProductosVenta.get(position).getCantidad()));
        holder.subtotal.setText("$"+String.valueOf(itemsProductosVenta.get(position).getSubtotal()));

        if(itemsProductosVenta.get(position).getTipo()==0) { ////0 son gramos
            holder.unidad.setText("Gramos");
        }
        else{ //1 es piezas
            holder.unidad.setText("Pieza(s)");
        }
        }
    }
