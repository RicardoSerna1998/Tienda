package com.example.ricardosernam.tienda.Empleados;

/**
 * Created by Ricardo Serna M on 28/02/2018.
 */

public class Empleados_class {
    public String nombre;
    public String puesto;
    public int activo;



    public Empleados_class(String nombre, String puesto, int activo) {
        this.nombre=nombre;
        this.puesto=puesto;
        this.activo=activo;


    }
    public String getNombre() {
        return nombre;
    }
    public String getPuesto() {
        return puesto;
    }

    public int getActivo() {
        return activo;
    }
}
