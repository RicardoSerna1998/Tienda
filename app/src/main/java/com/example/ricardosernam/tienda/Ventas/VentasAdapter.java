package com.example.ricardosernam.tienda.Ventas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ricardosernam.tienda.Empleados.Empleados_class;
import com.example.ricardosernam.tienda.Empleados.usuariosDialogFragment;
import com.example.ricardosernam.tienda.R;

import java.util.ArrayList;

public class VentasAdapter extends RecyclerView.Adapter <VentasAdapter.Productos_ventasViewHolder>{  ///adaptador para el Fragmet Ventas
    private ArrayList<Productos_class> itemsProductos;
    private FragmentManager fm;
    private Context context;

    public VentasAdapter(ArrayList<Productos_class> itemsProductos, FragmentManager fm, Context context) {  ///recibe el arrayProductos como parametro y la interface
        this.itemsProductos=itemsProductos;
        this.fm=fm;
        this.context=context;
    }
    public  class Productos_ventasViewHolder extends RecyclerView.ViewHolder{    ////clase donde van los elementos del cardview
        // Campos respectivos de un item
        public TextView producto, precio, existentes;
        public Productos_ventasViewHolder(final View v) {   ////lo que se programe aqui es para cuando se le de clic a un item del recycler
            super(v);
            producto = v.findViewById(R.id.TVproducto);  ////Textview donde se coloca el nombre del producto
            precio=v.findViewById(R.id.TVprecio);
            existentes=v.findViewById(R.id.TVexistentes);
        }
    }

    @Override
    public int getItemCount() {
        return itemsProductos.size();
    }

    @Override
    public Productos_ventasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tarjetas_productos_ventas, viewGroup, false);
        return new Productos_ventasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Productos_ventasViewHolder holder, final int position) {
        holder.producto.setText(itemsProductos.get(position).getNombre());
        holder.precio.setText("$"+String.valueOf(itemsProductos.get(position).getPrecio()));
        if(itemsProductos.get(position).getCodigo_barras()==null) { ////0 son gramos
            holder.existentes.setText(String.valueOf(itemsProductos.get(position).getExistentes()) +" Gramos aún");
        }
        else{ //1 es piezas
            holder.existentes.setText(String.valueOf(itemsProductos.get(position).getExistentes()) +" Pieza(s) aún");
            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemsProductos.get(position).getCodigo_barras()==null) { ////0 son gramos
                    new cantidad_producto_DialogFragment(itemsProductos.get(position).getNombre(), itemsProductos.get(position).getPrecio(), 0).show(fm, "Producto_ventas");
                }
                else{ //1 es piezas
                    new cantidad_producto_DialogFragment(itemsProductos.get(position).getNombre(), itemsProductos.get(position).getPrecio(), 1).show(fm, "Producto_ventas");
                }
                }
        });
        }
    }
