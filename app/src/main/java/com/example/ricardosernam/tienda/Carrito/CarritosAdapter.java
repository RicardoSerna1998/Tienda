package com.example.ricardosernam.tienda.Carrito;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Context context;


    public CarritosAdapter(ArrayList<ProductosVenta_class> itemsProductosVenta, Context context, actualizado Interfaz) {  ///recibe el arrayProductos como parametro y la interface
        this.itemsProductosVenta=itemsProductosVenta;
        this.context=context;
        this.Interfaz=Interfaz;
    }
    public  class Productos_ventasViewHolder extends RecyclerView.ViewHolder{    ////clase donde van los elementos del cardview
        // Campos respectivos de un item
        public TextView producto, precio, unidad, subtotal;
        public EditText cantidad;
        public Button eliminar;
        public Productos_ventasViewHolder(final View v) {   ////lo que se programe aqui es para cuando se le de clic a un item del recycler
            super(v);
            producto = v.findViewById(R.id.TVproductoCarrito);  ////Textview donde se coloca el nombre del producto
            unidad = v.findViewById(R.id.TVunidadCarrito);  ////Textview donde se coloca el nombre del producto
            precio=v.findViewById(R.id.TVprecioCarrito);
            subtotal=v.findViewById(R.id.TVsubTotalCarrito);
            cantidad=v.findViewById(R.id.ETcantidadCarrito);
            eliminar=v.findViewById(R.id.BtnEliminarProducto);

        }
    }
    public static class watcherCalculo1 implements TextWatcher {   ///detecta cambios en los editText
        private EditText cantidad;
        private actualizado Interfaz;
        private String  nombre;
        private TextView subtotal;
        private Float precio;

        watcherCalculo1(String nombre, EditText cantidad, actualizado Interfaz, TextView subtotal, Float precio) {
            this.nombre=nombre;
            this.cantidad = cantidad;
            this.Interfaz=Interfaz;
            this.subtotal=subtotal;
            this.precio=precio;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!(TextUtils.isEmpty(cantidad.getText()))){
                subtotal.setText("$"+String.valueOf((Float.parseFloat(cantidad.getText().toString()))*precio));
                Interfaz.actualizar(Integer.parseInt(String.valueOf((cantidad.getText()))), nombre);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
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
        holder.cantidad.setText(String.valueOf(Integer.valueOf(String.valueOf(itemsProductosVenta.get(position).getCantidad()))));
        holder.subtotal.setText("$"+String.valueOf(itemsProductosVenta.get(position).getSubtotal()));


        if(itemsProductosVenta.get(position).getTipo()==0) { ////0 son gramos
            holder.unidad.setText("Gramos");
        }
        else{ //1 es piezas
            holder.unidad.setText("Pieza(s)");
        }
        holder.cantidad.addTextChangedListener(new watcherCalculo1(String.valueOf(holder.producto.getText()), holder.cantidad, Interfaz, holder.subtotal, itemsProductosVenta.get(position).getPrecio()));

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {////eliminamos un producto
                AlertDialog.Builder eliminarProducto = new AlertDialog.Builder(context);
                eliminarProducto .setTitle("Cuidado");
                eliminarProducto .setMessage("¿Seguro que quieres eliminar este producto?");
                eliminarProducto .setCancelable(false);
                eliminarProducto .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface eliminarProducto , int id) {
                        itemsProductosVenta.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,itemsProductosVenta.size());
                        eliminarProducto.dismiss();
                        Carrito.calcularTotal();
                    }
                });
                eliminarProducto .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface eliminarProducto , int id) {
                        eliminarProducto .dismiss();
                    }
                });
                eliminarProducto .show();
            }
        });
    }
    }
