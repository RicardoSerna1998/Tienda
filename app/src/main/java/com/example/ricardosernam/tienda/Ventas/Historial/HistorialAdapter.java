package com.example.ricardosernam.tienda.Ventas.Historial;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ricardosernam.tienda.R;

import java.util.ArrayList;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialVentasViewHolder> {

    private ArrayList<Historial_class> itemsHistorial;
    private Context context;

    public HistorialAdapter(Context context, ArrayList<Historial_class> itemsCobrar) {  ///recibe el arrayCobrar como parametro
        this.itemsHistorial=itemsCobrar;
        this.context=context;

    }

    public class HistorialVentasViewHolder extends RecyclerView.ViewHolder {
        public TextView empleado, fecha;

        public HistorialVentasViewHolder(View itemView) {
            super(itemView);
            empleado=itemView.findViewById(R.id.TVempleadoHistorial);
            fecha=itemView.findViewById(R.id.TVfechaHistorial);
        }

    }

    @Override
    public HistorialVentasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjetas_historial, parent, false);////mencionamos el archivo del layout
        return new HistorialVentasViewHolder(v);
    }
    @Override
    public int getItemCount() {
        return itemsHistorial.size();
    }
    @Override
    public void onBindViewHolder(HistorialVentasViewHolder holder, final int position) {
        holder.empleado.setText(itemsHistorial.get(position).getEmpleado());
        holder.fecha.setText(itemsHistorial.get(position).getFecha());
    }
}
