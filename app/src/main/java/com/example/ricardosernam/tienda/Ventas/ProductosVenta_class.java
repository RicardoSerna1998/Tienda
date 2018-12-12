package com.example.ricardosernam.tienda.Ventas;

public class ProductosVenta_class {  ///clase para obtener productos para cobrar
    public String nombre;
    public Float precio;
    public int cantidad;
    public Float subtotal;
    public int tipo;


    public ProductosVenta_class(String nombre, int cantidad, Float precio, int tipo, Float subtotal) {   ///se manda desde el arrayProductos
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.tipo = tipo;
        this.subtotal=subtotal;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Float getPrecio() {
        return precio;
    }
    public Float getSubtotal() {
        return subtotal;
    }

    public int getTipo() {
        return tipo;
}

}
