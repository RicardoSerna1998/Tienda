package com.example.ricardosernam.tienda.web;

/**
 * Esta clase representa un gasto individual de cada registro de la base de datos
 */
public class Empleados {
    public int idempleado;
    public String nombre;
    public String tipo;
    public String codigo;
    public int disponible;
    public int activo;


    public Empleados(int idempleado, String nombre, String tipo, String codigo, int disponible, int activo) {
        this.idempleado = idempleado;
        this.nombre =nombre;
        this.tipo = tipo;
        this.codigo = codigo;
        this.disponible=disponible;
        this.activo=activo;
    }
}
