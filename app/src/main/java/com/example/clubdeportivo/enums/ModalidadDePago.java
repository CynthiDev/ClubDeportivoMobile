package com.example.clubdeportivo.enums;

public enum ModalidadDePago {
    EFECTIVO("EFECTIVO"),
    UNA_CUOTA("1 CUOTA"),
    TRES_CUOTAS("3 CUOTAS");

    private String descripcion;

    ModalidadDePago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
