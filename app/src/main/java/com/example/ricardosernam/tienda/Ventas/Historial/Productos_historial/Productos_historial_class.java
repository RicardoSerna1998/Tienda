package com.example.ricardosernam.tienda.Ventas.Historial.Productos_historial;



public class Productos_historial_class {
    private String tipo_venta, fecha, fecha_entrega, descripcion, tipo_cobro;

    public Productos_historial_class(String tipo_venta, String fecha, String fecha_entrega, String descripcion, String tipo_cobro) {   ///se manda desde el arrayProductos
        this.tipo_venta= tipo_venta;
        this.fecha= fecha;
        this.fecha_entrega= fecha_entrega;
        this.descripcion= descripcion;
        this.tipo_cobro= tipo_cobro;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }
    public String getFecha() {
        return fecha;
    }
    public String getFecha_entrega() {
        return fecha_entrega;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getTipo_cobro() {
        return tipo_cobro;
    }
}
