package com.example.developer2.appmap.models;

import java.util.Date;

public class Registro {

    private int idPersona;
    private Date fecha;
    private double latitud;
    private double longitud;

    public void registro(){}

    public Registro(int idPersona, Date fecha, double latitud, double longitud) {
        this.idPersona = idPersona;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
