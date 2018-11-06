package com.example.developer2.appmap.models;

public class Marcadores {

    private int idMarcador;
    private String titulo;
    private String detalle;
    private Double latitud;
    private Double longitud;

    public Marcadores(){

    }
    public Marcadores(int idMarcador, String titulo, String detalle, Double latitud, Double longitud) {
        this.idMarcador = idMarcador;
        this.titulo = titulo;
        this.detalle = detalle;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdMarcador() {
        return idMarcador;
    }

    public void setIdMarcador(int idMarcador) {
        this.idMarcador = idMarcador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
