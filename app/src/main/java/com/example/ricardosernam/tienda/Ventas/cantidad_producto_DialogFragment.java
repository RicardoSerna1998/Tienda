package com.example.ricardosernam.tienda.Ventas;

import android.annotation.SuppressLint;
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

import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;

@SuppressLint("ValidFragment")
public class cantidad_producto_DialogFragment extends android.support.v4.app.DialogFragment {
    private LinearLayout botones;
    private Button aceptar,cancelar, sumar, restar;
    private TextView precioProducto,subtotal, unidad, productoSelecionado;
    private EditText cantidad;
    private float precio, cantidadSubtotal;
    private String producto;
    int tipo, porcion;

    @SuppressLint("ValidFragment")
     public cantidad_producto_DialogFragment(String producto, float precio, int tipo){
         this.producto=producto;
         this.precio=precio;
         this.tipo=tipo;
     }

    @Override
    public View onCreateView (final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView=inflater.inflate(R.layout.dialog_fragment_cantidad_producto,container);
        this.getDialog().setTitle("Cobrar");///cambiamos titulo del DialogFragment
        precioProducto=rootView.findViewById(R.id.TVprecio);
        subtotal=rootView.findViewById(R.id.TVcambio);
        unidad=rootView.findViewById(R.id.TVunidad);
        productoSelecionado=rootView.findViewById(R.id.TVproducto);
        cantidad=rootView.findViewById(R.id.ETcantidadPago);
        botones=rootView.findViewById(R.id.LLBotones);
        aceptar=rootView.findViewById(R.id.BtnAceptarPago);
        cancelar=rootView.findViewById(R.id.BtnCancelarPago);

        sumar=rootView.findViewById(R.id.BtnSumar);
        restar=rootView.findViewById(R.id.BtnRestar);

        cantidad.setSelection(cantidad.getText().length());
        if(tipo==1){   ///pieza
            unidad.setText("Pieza(s)");
            porcion=1;
            }
        else{  ///gramos
            unidad.setText("Gramos");
            porcion=500;
        }
        sumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidad.setText(String.valueOf(Integer.parseInt(String.valueOf(cantidad.getText()))+porcion));
                }
        });
        restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(String.valueOf(cantidad.getText()))>=porcion) {
                    cantidad.setText(String.valueOf(Integer.parseInt(String.valueOf(cantidad.getText())) - porcion));
                }
            }
        });

        productoSelecionado.setText(producto);
        precioProducto.setText("$"+String.valueOf(precio));
        cantidad.setText("0");

        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(TextUtils.isEmpty(cantidad.getText()))) {
                    if(tipo==1){   ///pieza
                        cantidadSubtotal= Float.parseFloat(String.valueOf(cantidad.getText()))*precio;
                    }
                    else{  ///gramos
                        cantidadSubtotal= ((Float.parseFloat(String.valueOf(cantidad.getText())))/1000)*precio;
                    }
                    if(cantidadSubtotal>=0) {
                        subtotal.setText(String.valueOf(cantidadSubtotal));
                    }
                    else{
                        subtotal.setText("0");
                    }
                }
                else{
                    cantidad.setText("0");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){   /////si  ya se pago todo bien
                    ContractParaProductos.itemsProductosVenta.add(new ProductosVenta_class(producto,Float.parseFloat(cantidad.getText().toString()),precio, tipo, cantidadSubtotal));
                    dismiss();
                    Toast.makeText(getContext(), "Agregado a Carrito", Toast.LENGTH_LONG).show();
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

}
