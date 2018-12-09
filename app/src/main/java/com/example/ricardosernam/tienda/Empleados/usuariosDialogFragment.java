package com.example.ricardosernam.tienda.Empleados;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.Ventas.Ventas;


@SuppressLint("ValidFragment")
public class usuariosDialogFragment extends android.support.v4.app.DialogFragment {     //clase que me crea el dialogFragment con productos
    public String usuario, sesion;
    public TextView usuarioSeleccionado;
    public Button aceptar, cancelar;


    public usuariosDialogFragment(String usuario, String sesion) {
        this.usuario=usuario;
        this.sesion=sesion;
    }

    @Override
    public View onCreateView (final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView=inflater.inflate(R.layout.dialog_fragment_sesion_usuarios,container);
        usuarioSeleccionado=rootView.findViewById(R.id.TVusuario);
        aceptar=rootView.findViewById(R.id.BtnAceptarSesion);
        cancelar=rootView.findViewById(R.id.BtnCancelarSesion);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                getFragmentManager().beginTransaction().replace(R.id.LLprincipal, new Ventas()).commit(); ///cambio de fragment
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
