package com.example.ricardosernam.tienda.Ventas.Historial;



public class Historial_class {
    private String empleado, fecha, fecha_entrega, descripcion, tipo_cobro;

    public Historial_class(String empleado, String fecha) {   ///se manda desde el arrayProductos

        this.empleado= empleado;
        this.fecha= fecha;

    }

    public String getEmpleado() {
        return empleado;
    }
    public String getFecha() {
        return fecha;
    }
}
