package com.example.ricardosernam.tienda.Ventas.Historial.Productos_historial;



public class Productos_historial_class {
    private String producto;
    private Float precio, subTotal;
    private int cantidad;

    public Productos_historial_class(String producto, int cantidad, Float precio, Float subTotal) {   ///se manda desde el arrayProductos
        this.producto= producto;
        this.cantidad= cantidad;
        this.precio= precio;
        this.subTotal= subTotal;
    }

    public String getProducto() {
        return producto;
    }
    public Float getPrecio() {
        return precio;
    }
    public Float getSubTotal() {
        return subTotal;
    }
    public int getCantidad() {
        return cantidad;
    }
}
