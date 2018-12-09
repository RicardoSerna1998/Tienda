package com.example.ricardosernam.tienda.Ventas.Historial;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ricardosernam.tienda.R;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class Historial_DialogFragment extends android.support.v4.app.DialogFragment {
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private SQLiteDatabase db;
    private Cursor fila;
    ArrayList<Historial_ventas_class> itemsHistorial = new ArrayList<>();   ///array para productos seleccionados
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.fragment_historial, container, false);
        /*BaseDeDatosLocal admin=new BaseDeDatosLocal(getActivity());
        db=admin.getWritableDatabase();
        fila=db.rawQuery("select tipo, fecha, fecha_entrega, descripcion, tipo_cobro from Ventas" ,null);

        if(fila.moveToFirst()) {///si hay un elemento
            itemsHistorial.add(new Productos_historial_class(fila.getString(0), fila.getString(1), fila.getString(2), fila.getString(3), fila.getString(4)));
            while (fila.moveToNext()) {
                itemsHistorial.add(new Productos_historial_class(fila.getString(0), fila.getString(1), fila.getString(2), fila.getString(3), fila.getString(4)));
            }
        }
        adapter = new Productos_historialAdapter(getActivity(), itemsHistorial, new interfaz_OnClick() {
                  @Override
                  public void onClick(View v) {
                      dismiss();
                  }
              });///llamamos al adaptador y le enviamos el array como parametro
                recycler = view2.findViewById(R.id.RVrecicladorHistorial);///declaramos el recycler
                lManager = new LinearLayoutManager(getActivity());  //declaramos el layoutmanager
                recycler.setLayoutManager(lManager);
                recycler.setAdapter(adapter);
                this.getDialog().setTitle("Historial");///cambiamos titulo del DialogFragment*/
        return view2;
    }
}
