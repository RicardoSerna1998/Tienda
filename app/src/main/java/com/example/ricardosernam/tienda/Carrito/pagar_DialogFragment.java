package com.example.ricardosernam.tienda.Carrito;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;

import java.text.SimpleDateFormat;

import static com.example.ricardosernam.tienda.Carrito.Carrito.aceptar_cancelar;

@SuppressLint("ValidFragment")
public class pagar_DialogFragment extends android.support.v4.app.DialogFragment {
    private Button aceptar,cancelar;
    private SQLiteDatabase db;
    private android.support.v4.app.FragmentManager fm;
    private Cursor empleado, venta, existente;
    private ContentValues values, values2, values3;
    private TextView total,cambio, deuda, abono;
    private EditText cantidad;
    private float totalPagar;
    private CheckBox imprimir;


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
        imprimir=rootView.findViewById(R.id.CBimprimir);

        abono=rootView.findViewById(R.id.TVpagoAbono);
        deuda=rootView.findViewById(R.id.TVdeuda);
        cantidad=rootView.findViewById(R.id.ETcantidadPago);
        aceptar=rootView.findViewById(R.id.BtnAceptarPago);
        cancelar=rootView.findViewById(R.id.BtnCancelarPago);
        fm=getFragmentManager();


        total.setText(String.valueOf(totalPagar));
        cantidad.setText(String.valueOf(totalPagar));
        DatabaseHelper admin=new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db=admin.getWritableDatabase();

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
                    if(imprimir.isChecked()){
                        ///imprimimos el recibo
                    }
                    dismiss();
                    values = new ContentValues();
                    /////obtener fecha actual
                    java.util.Calendar c = java.util.Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String formattedDate = df.format(c.getTime());

                    empleado= db.rawQuery("select idRemota from empleados where tipo_empleado='Cajero' or tipo_empleado='Administrador' and activo=1", null);

                    if (empleado.moveToFirst()) {
                        values.put("id_empleado", empleado.getString(0));
                    }

                    values.put("fecha", formattedDate);
                    values.put(ContractParaProductos.Columnas.PENDIENTE_INSERCION, 1);
                    db.insertOrThrow("ventas", null, values);
                    Log.i("Venta", String.valueOf(values));    ////mostramos que valores se han insertado

/////////////////////////////////incersion-modificación ventas-inventario_detalles
                    values2 = new ContentValues();
                    venta = db.rawQuery("select * from ventas", null);

                    for (int i = 0; i < ContractParaProductos.itemsProductosVenta.size(); i++) {
                        ////////////////venta detalles/////////////////////////////77
                        if (venta.moveToFirst()) {
                            venta.moveToLast();
                            values2.put("idRemota", venta.getString(0));
                            values2.put("id_producto", ContractParaProductos.itemsProductosVenta.get(i).getIdRemota());
                            values2.put("cantidad", ContractParaProductos.itemsProductosVenta.get(i).getCantidad());
                            values2.put("precio",ContractParaProductos.itemsProductosVenta.get(i).getPrecio());
                            db.insertOrThrow("venta_detalles", null, values2);
                            Log.i("Venta_detalles", String.valueOf(values2));    ////mostramos que valores se han insertado
                        }
                        //////////////////////////////////////////inventario detalles//////////////////////////////
                        values3 = new ContentValues();
                        ///obtenemos el guisado donde tenemos que descontar
                        existente = db.rawQuery("select existentes from inventario where nombre_producto='" + ContractParaProductos.itemsProductosVenta.get(i).getNombre() + "'", null);
                        if (existente.moveToFirst()) {
                            float porcion = existente.getFloat(0) - (ContractParaProductos.itemsProductosVenta.get(i).getCantidad());
                            values3.put("existentes", porcion);
                            db.update("inventario", values3, "idRemota='" + ContractParaProductos.itemsProductosVenta.get(i).getIdRemota() + "'", null);
                            Log.i("Inventario", String.valueOf(values3));    ////mostramos que valores se han insertado
                        }
                    }
                    Toast.makeText(getContext(), "Venta exitosa", Toast.LENGTH_LONG).show();
                    aceptar_cancelar(fm);

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
