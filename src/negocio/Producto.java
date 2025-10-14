/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

/**
 *
 * Clase principal que representa un producto en el sistema
 */
public class Producto implements Comparable<Producto> {

    private String codigo;
    private String nombre;
    //private String descripcion;
    private double precio;
    private int stock;
    private String categoria;
    //private List<String> tags;
    private double rating;
    //private int numeroVentas;
    //private LocalDateTime fechaCreacion;

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

    public String toFileString() {
        return String.format("%s|%s|%.2f|%d|%s|%.1f", codigo, nombre, precio, stock, categoria, rating);
    }

    public static Producto fromString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(";");
        if (parts.length < 5) {
            // Manejo de error si la línea no tiene suficientes partes
            return null;
        }

        try {
            String codigo = parts[0].trim();
            String nombre = parts[1].trim();
            double precio = Double.parseDouble(parts[2].trim());
            int stock = Integer.parseInt(parts[3].trim());
            String categoria = parts[4].trim();
            double rating = (parts.length > 5) ? Double.parseDouble(parts[5].trim()) : 0.0;

            return new Producto(codigo, nombre, precio, stock, categoria, rating);
        } catch (NumberFormatException e) {
            System.err.println("Error de formato al crear Producto: " + line);
            return null;
        }
    }

    public static String getCodigoFromLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length > 0) {
            return parts[0].trim();
        }
        return "";
    }
}
