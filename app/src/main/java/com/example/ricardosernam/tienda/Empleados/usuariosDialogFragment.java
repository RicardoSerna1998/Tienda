package com.example.ricardosernam.tienda.Empleados;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.MainActivity;
import com.example.ricardosernam.tienda.Provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.Ventas.Ventas;

import static com.example.ricardosernam.tienda.Empleados.Empleados.relleno;
import static com.example.ricardosernam.tienda.Provider.ContractParaProductos.rojo;
import static com.example.ricardosernam.tienda.Provider.ContractParaProductos.verde;


@SuppressLint("ValidFragment")
public class usuariosDialogFragment extends android.support.v4.app.DialogFragment {     //clase que me crea el dialogFragment con productos
    public String usuario, sesion, codigo, puesto;
    public TextView usuarioSeleccionado;
    public Button aceptar, cancelar;
    public EditText contrasena;
    private ContentValues values;
    private SQLiteDatabase db;
    private Cursor activos;



    public usuariosDialogFragment(String usuario, String sesion, String codigo, String puesto) {
        this.usuario=usuario;
        this.sesion=sesion;
        this.codigo=codigo;
        this.puesto=puesto;
    }

    @Override
    public View onCreateView (final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView=inflater.inflate(R.layout.dialog_fragment_sesion_usuarios,container);
        usuarioSeleccionado=rootView.findViewById(R.id.TVusuario);
        aceptar=rootView.findViewById(R.id.BtnAceptarSesion);
        cancelar=rootView.findViewById(R.id.BtnCancelarSesion);
        contrasena=rootView.findViewById(R.id.ETcodigo);
        ///activo= rootView.findViewById(R.id.RBactivo);


        values = new ContentValues();

        DatabaseHelper admin=new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db=admin.getWritableDatabase();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(contrasena.getText().toString().equals(codigo)){  //codigo correcto
                    if(sesion.equals("Iniciar Sesión")){
                        if(puesto.equals("Admin.") || puesto.equals("Cajero")){
                            activos= db.rawQuery("select * from empleados where tipo_empleado='Admin.' and activo=1 or tipo_empleado='Cajero' and activo=1", null);
                            if(activos.moveToFirst()){  ///hay un cajero/admin activo
                                Toast.makeText(getContext(), "Cerrar sesión de otro cajero", Toast.LENGTH_LONG).show();
                            }
                            else{  ///
                                values.put("activo", 1);
                                db.update("empleados", values, "nombre_empleado='" + usuario + "'", null);
                                MainActivity.empleadoActivo.setText("Cajer@: "+usuario);
                                relleno(getContext());
                                getDialog().dismiss();
                                getFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Ventas()).commit(); ///cambio de fragment
                            }
                        }
                        else{///otro puesto que no implica caja
                            values.put("activo", 1);
                            db.update("empleados", values, "nombre_empleado='" + usuario + "'", null);
                            relleno(getContext());
                            getDialog().dismiss();
                            }
                    }
                    else{  ///cerrar sesión
                        if(puesto.equals("Admin.") || puesto.equals("Cajero")){
                            MainActivity.empleadoActivo.setText("");
                            }
                        values.put("activo", 0);
                        db.update("empleados", values, "nombre_empleado='" + usuario + "'", null);
                        relleno(getContext());
                        getDialog().dismiss();
                        }
                }
                else{  //codigo incorrecto
                    contrasena.setError("Contraseña invalida");
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        usuarioSeleccionado.setText(usuario);
        this.getDialog().setTitle(sesion);///cambiamos titulo del DialogFragment
        return rootView;
    }
}
