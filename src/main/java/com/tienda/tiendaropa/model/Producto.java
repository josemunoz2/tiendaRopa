package com.tienda.tiendaropa.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Producto {
    private String id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @Positive(message = "El precio debe ser mayor que 0")
    private double precio;

    @Positive(message = "El stock debe ser mayor que 0")
    private int stock;

    // Getters y Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}
