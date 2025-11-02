/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Clase que representa un producto en el sistema.
 * @author USER
 */
public final class Producto implements Comparable<Producto> {
    // Constantes de validación
    private static final Pattern CODIGO_PATTERN = Pattern.compile("\\d{6}");
    private static final double RATING_MIN = 0.0;
    private static final double RATING_MAX = 5.0;
    private static final String SEPARADOR = "|";
    
    // Campos inmutalbes
    private String codigo;
    private String nombre;
    private double precio;
    private String categoria;
    private double rating;
    //private String descripcion;
    //private List<String> tags;
    //private int numeroVentas;
    //private LocalDateTime fechaCreacion;
    
    // Campo mutable (stock cambia con las ventas)
    private volatile int stock;

    public Producto(String codigo, String nombre, double precio, int stock, String categoria, double rating) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.rating = rating;
    }
    
    public static Producto crear(String codigo, String nombre, double precio, int stock, String categoria, double rating) {
        validarCodigo(codigo);
        validarNombre(nombre);
        validarPrecio(precio);
        validarStock(stock);
        validarCategoria(categoria);
        validarRating(rating);

        return new Producto(
                codigo.trim(),
                nombre.trim(),
                precio,
                stock,
                categoria.trim(),
                rating
        );
    }
    
    /**
     * Crea un producto desde una línea de archivo
     * Formato: codigo|nombre|precio|stock|categoria|rating
     */
    
    public static Producto fromString(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Línea vacía o nula");
        }
        
        String[] parts = line.split(Pattern.quote(SEPARADOR));
        
        if (parts.length != 6) {
            throw new IllegalArgumentException(
                String.format("Formato inválido. Esperado 6 campos, encontrados %d en: %s", 
                              parts.length, line)
            );
        }
        
        try {
            return crear(
                parts[0].trim(),
                parts[1].trim(),
                Double.parseDouble(parts[2].trim()),
                Integer.parseInt(parts[3].trim()),
                parts[4].trim(),
                Double.parseDouble(parts[5].trim())
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Formato numérico inválido en línea: " + line, e
            );
        }
    }
    
    /**
     * Extrae solo el código de una línea de archivo (para ordenación externa)
     */
    
    public static String getCodigoFromLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return "";
        }
        
        int separadorIndex = line.indexOf(SEPARADOR);
        if (separadorIndex > 0) {
            return line.substring(0, separadorIndex).trim();
        }
        
        return line.trim();
    }
    
    // VALIDACIONES
    
    private static void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        if (!CODIGO_PATTERN.matcher(codigo.trim()).matches()) {
            throw new IllegalArgumentException(
                "El código debe tener exactamente 6 dígitos numéricos: " + codigo
            );
        }
    }
    
    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.trim().length() < 3) {
            throw new IllegalArgumentException(
                "El nombre debe tener al menos 3 caracteres"
            );
        }
    }
    
    private static void validarPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (Double.isNaN(precio) || Double.isInfinite(precio)) {
            throw new IllegalArgumentException("El precio debe ser un número válido");
        }
    }
    
    private static void validarStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
    
    private static void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }
    }
    
    private static void validarRating(double rating) {
        if (rating < RATING_MIN || rating > RATING_MAX) {
            throw new IllegalArgumentException(
                String.format("El rating debe estar entre %.1f y %.1f", 
                              RATING_MIN, RATING_MAX)
            );
        }
    }
    
    // GETTERS

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
    
    // OPERACIONES DE STOCK
    
    /**
     * Reduce el stock en la cantidad especificada
     */
    public synchronized boolean reducirStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        
        if (this.stock >= cantidad) {
            this.stock -= cantidad;
            return true;
        }
        return false;
    }
    
    /**
     * Aumenta el stock en la cantidad especificada
     */
    public synchronized void aumentarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.stock += cantidad;
    }
    
    /**
     * Establece el stock directamente
     */
    public synchronized void setStock(int stock) {
        validarStock(stock);
        this.stock = stock;
    }
    
    /**
     * Veritifaca si hay stock suficiente
     */
    public boolean hayStockDisponible(int cantidad) {
        return this.stock >= cantidad;
    }
    
    // SERIALIZACIÓN
    /**
     * Convierte el producto a formato de archivo
     */
    public String toFileString() {
        return String.format("%s%s%s%s%.2f%s%d%s%s%s%.1f",
            codigo, SEPARADOR,
            nombre, SEPARADOR,
            precio, SEPARADOR,
            stock, SEPARADOR,
            categoria, SEPARADOR,
            rating
        );
    }
    
    // COMPARABLE
    
    @Override
    public int compareTo(Producto other) {
        if (other == null) {
            throw new NullPointerException("No se puede comparar con null");
        }
        return this.codigo.compareTo(other.codigo);
    }
    
    // EQUALS & HASHCODE
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Producto producto = (Producto) o;
        return codigo.equals(producto.codigo); // Los productos son iguales si tienen el mismo código
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
    
    // TO STRING
    
    @Override
    public String toString() {
        return String.format(
            "Producto{codigo='%s', nombre='%s', precio=%.2f, stock=%d, categoria='%s', rating=%.1f}",
            codigo, nombre, precio, stock, categoria, rating
        );
    }
    
    /**
     * Representación compacta para logs
     */
    public String toShortString() {
        return String.format("%s - %s (S/.%.2f)", codigo, nombre, precio);
    }
    
    // BUILDER PATTERN
    
    public static class Builder {
        private String codigo;
        private String nombre;
        private double precio;
        private int stock;
        private String categoria;
        private double rating = 0.0; // Default
        
        public Builder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }
        
        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        
        public Builder precio(double precio) {
            this.precio = precio;
            return this;
        }
        
        public Builder stock(int stock) {
            this.stock = stock;
            return this;
        }
        
        public Builder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }
        
        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }
        
        public Producto build() {
            return Producto.crear(codigo, nombre, precio, stock, categoria, rating);
        }
    }
}
