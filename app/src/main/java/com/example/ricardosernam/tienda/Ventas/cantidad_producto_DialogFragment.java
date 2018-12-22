package com.example.ricardosernam.tienda.Ventas;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricardosernam.tienda.Carrito.Carrito;
import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;

@SuppressLint("ValidFragment")
public class cantidad_producto_DialogFragment extends android.support.v4.app.DialogFragment {
    private LinearLayout botones;
    private Cursor productoElegido;
    private SQLiteDatabase db;
    private Button aceptar, cancelar, sumar, restar;
    private TextView precioProducto, unidad, productoSelecionado;
    private EditText cantidad, subtotal;
    private float precio, cantidadSubtotal, cantidadProducto;
    private String producto;
    int tipo, porcion;

    @SuppressLint("ValidFragment")
    public cantidad_producto_DialogFragment(String producto, float precio, int tipo) {
        this.producto = producto;
        this.precio = precio;
        this.tipo = tipo;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.dialog_fragment_cantidad_producto, container);
        this.getDialog().setTitle("Cobrar");///cambiamos titulo del DialogFragment
        precioProducto = rootView.findViewById(R.id.TVprecio);
        unidad = rootView.findViewById(R.id.TVunidad);
        productoSelecionado = rootView.findViewById(R.id.TVproducto);
        cantidad = rootView.findViewById(R.id.ETcantidadPago);
        subtotal = rootView.findViewById(R.id.ETsubtotalCantidad);
        botones = rootView.findViewById(R.id.LLBotones);
        aceptar = rootView.findViewById(R.id.BtnAceptarPago);
        cancelar = rootView.findViewById(R.id.BtnCancelarPago);

        sumar = rootView.findViewById(R.id.BtnSumar);
        restar = rootView.findViewById(R.id.BtnRestar);

        cantidad.setText("0");
        cantidad.setSelection(cantidad.getText().length());
        DatabaseHelper admin = new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db = admin.getWritableDatabase();

        if (tipo == 1) {   ///pieza
            unidad.setText("Pieza(s)");
            porcion = 1;
        } else {  ///gramos
            unidad.setText("Gramos");
            porcion = 500;
        }
        sumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cantidad.isFocused()) {
                    cantidad.setText(String.valueOf(Float.parseFloat(String.valueOf(cantidad.getText())) + porcion));
                } else if (subtotal.isFocused()) {
                    subtotal.setText(String.valueOf(Float.parseFloat(String.valueOf(subtotal.getText())) + 5));
                }
            }
        });
        restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cantidad.isFocused()) {
                    if (Float.parseFloat(String.valueOf(cantidad.getText())) >= porcion) {
                        cantidad.setText(String.valueOf(Float.parseFloat(String.valueOf(cantidad.getText())) - porcion));
                    }
                } else if (subtotal.isFocused()) {
                    subtotal.setText(String.valueOf(Float.parseFloat(String.valueOf(subtotal.getText())) - 5));
                }

            }
        });

        productoSelecionado.setText(producto);
        precioProducto.setText("$" + String.valueOf(precio));

        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (cantidad.isFocused()) {
                    if (!(TextUtils.isEmpty(cantidad.getText()))) {
                        if (tipo == 1) {   ///pieza
                            cantidadSubtotal = Float.parseFloat(String.valueOf(cantidad.getText())) * precio;
                        } else {  ///gramos
                            cantidadSubtotal = ((Float.parseFloat(String.valueOf(cantidad.getText()))) / 1000) * precio;
                        }
                        if (cantidadSubtotal >= 0) {
                            subtotal.setText(String.valueOf(cantidadSubtotal));
                        } else {
                            subtotal.setText("0");
                        }
                    } else {
                        cantidad.setText("0");
                    }
                }
                cantidad.setSelection(cantidad.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
            subtotal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (subtotal.isFocused()) {
                        if (!(TextUtils.isEmpty(subtotal.getText()))) {
                            if (tipo == 1) {   ///pieza
                                cantidadProducto = Float.parseFloat(String.valueOf(subtotal.getText())) / precio;
                            } else {  ///gramos
                                cantidadProducto = ((Float.parseFloat(String.valueOf(subtotal.getText()))) * 1000) / precio;
                            }
                            if (cantidadProducto >= 0) {
                                cantidad.setText(String.valueOf(cantidadProducto));
                            } else {
                                cantidad.setText("0");
                            }
                        } else {
                            subtotal.setText("0");
                        }
                    }
                    subtotal.setSelection(subtotal.getText().length());
                }
                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){   /////si  ya se pago todo bien
                    productoElegido= db.rawQuery("select idRemota from inventario where nombre_producto='"+producto+"'", null);
                    if(productoElegido.moveToFirst()){
                        repetido(producto);
                        ContractParaProductos.itemsProductosVenta.add(new ProductosVenta_class(producto, Float.parseFloat(cantidad.getText().toString()),precio, tipo, Float.parseFloat(subtotal.getText().toString()) , productoElegido.getInt(0)));
                        dismiss();
                        Toast.makeText(getContext(), "Agregado a Carrito", Toast.LENGTH_LONG).show();
                    }
                    //aceptarCompra.actualizar(0, null);
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }
    public Boolean validar(){
         Boolean validado=true;
        if(((TextUtils.isEmpty(cantidad.getText())))) {  /// es vacio
            validado=false;
            cantidad.setError("Ingresa una cantidad");
        }
        else{  ///hay algo
            if((Float.parseFloat(String.valueOf(cantidad.getText()))<=0)){
                validado=false;
                cantidad.setError("Ingresa una cantidad válida");
            }
        }
        return validado;
    }
    public static void repetido(String nombre){
        for(int i=0; i<ContractParaProductos.itemsProductosVenta.size(); i++) {
            if(nombre.equals(ContractParaProductos.itemsProductosVenta.get(i).getNombre())){  ///si se repite
                ContractParaProductos.itemsProductosVenta.remove(i);   ////eliminamos el previamente agregado
            }
        }
    }

}
