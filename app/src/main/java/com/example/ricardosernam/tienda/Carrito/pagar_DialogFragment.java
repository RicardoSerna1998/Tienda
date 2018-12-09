package com.example.ricardosernam.tienda.Carrito;

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
import android.widget.TextView;

import com.example.ricardosernam.tienda.R;

@SuppressLint("ValidFragment")
public class pagar_DialogFragment extends android.support.v4.app.DialogFragment {
    private Button aceptar,cancelar;
    private TextView total,cambio, deuda, abono;
    private EditText cantidad;
    private float totalPagar;


     @SuppressLint("ValidFragment")
     public pagar_DialogFragment(float totalPagar){
         this.totalPagar=totalPagar;
     }

    @Override
    public View onCreateView (final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView=inflater.inflate(R.layout.dialog_fragment_pagar,container);
        this.getDialog().setTitle("Cobrar");///cambiamos titulo del DialogFragment
        total=rootView.findViewById(R.id.TVtotalCompra);
        cambio=rootView.findViewById(R.id.TVcambio);

        abono=rootView.findViewById(R.id.TVpagoAbono);
        deuda=rootView.findViewById(R.id.TVdeuda);
        cantidad=rootView.findViewById(R.id.ETcantidadPago);
        aceptar=rootView.findViewById(R.id.BtnAceptarPago);
        cancelar=rootView.findViewById(R.id.BtnCancelarPago);

        total.setText(String.valueOf(totalPagar));
        cantidad.setText("0");

        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(TextUtils.isEmpty(cantidad.getText()))) {
                    float cantidadCambio= Float.parseFloat(String.valueOf(cantidad.getText()))-totalPagar;///feria
                    //float cantidadDeuda=totalPagar-(Float.parseFloat(String.valueOf(cantidad.getText())));
                    if(cantidadCambio>=0) {
                        cambio.setText(String.valueOf(cantidadCambio));
                    }
                    else{
                        cambio.setText("0");
                    }
                    /*if(cantidadDeuda>=0) {
                        total.setText(String.valueOf(cantidadDeuda));
                    }
                    else{
                        total.setText("0");
                    }*/
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar(totalPagar)){   /////si  ya se pago todo bien
                    dismiss();
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
    public Boolean validar(float total){
         Boolean validado=true;
        if(((TextUtils.isEmpty(cantidad.getText())))) {  /// es vacio
            validado=false;
            cantidad.setError("Ingresa una cantidad válida");
        }
        else{  ///hay algo
            if((Float.parseFloat(String.valueOf(cantidad.getText()))<total)){
                validado=false;
                cantidad.setError("Ingresa una cantidad válida");
            }
        }
        return validado;
    }

}
