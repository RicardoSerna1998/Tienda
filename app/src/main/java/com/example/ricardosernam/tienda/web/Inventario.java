package com.example.ricardosernam.tienda.web;

/**
 * Esta clase representa un gasto individual de cada registro de la base de datos
 */
public class Inventario {
    public String idproducto;
    public String nombre;
    public double precio;
    public String codigo_barras;
    public double existentes;


    public Inventario(String idproducto, String nombre, double precio, String codigo_barras, double existentes) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.precio = precio;
        this.codigo_barras = codigo_barras;
        this.existentes = existentes;
    }
}
