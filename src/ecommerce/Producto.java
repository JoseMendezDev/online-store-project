/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author USER
 */
public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int stock;
    private String categoria;

    public Producto(int id, String nombre, double precio, int stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Nombre: %s, Precio: $%.2f, Stock: %d, Categoría: %s",
                id, nombre, precio, stock, categoria);
    }
}
