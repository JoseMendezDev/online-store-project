/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author USER
 */
public class Producto implements Comparable<Producto>{

    private String codigo;
    private String nombre;
    private double precio;
    private int stock;
    private String categoria;
    private double rating;

    public Producto(String codigo, String nombre, double precio, int stock, String categoria, double rating) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.rating = rating;
    }

    public String getCodigo() {
        return codigo;
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

    public double getRating() {
        return rating;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public int compareTo(Producto other) {
        return this.codigo.compareTo(other.codigo);
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=%.2f, stock=%d, category='%s', rating=%.1f}",
                codigo, nombre, precio, stock, categoria, rating);
    }
    
    public static String getCodigoPorLinea(String linea) {
        String[] partes = linea.split("\\|");
        return partes[0].trim();
    }
}
