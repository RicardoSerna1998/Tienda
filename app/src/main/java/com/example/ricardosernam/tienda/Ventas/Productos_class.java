package com.example.ricardosernam.tienda.Ventas;

public class Productos_class {  ///clase para obtener productos para cobrar
    public String nombre;
    public String codigo_barras;
    public Float precio;

    public Productos_class(String nombre, Float precio, String codigo_barras) {   ///se manda desde el arrayProductos
        this.nombre = nombre;
        this.precio = precio;
        this.codigo_barras=codigo_barras;
    }

    public String getNombre() {
        return nombre;
    }
    public Float getPrecio() {
        return precio;
    }
    public String getCodigo_barras() {
        return codigo_barras;
    }
}
