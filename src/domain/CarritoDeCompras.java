/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

import domain.Producto;
import domain.Ecommerce;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author USER
 */
public class CarritoDeCompras {

    private final Map<String, ItemCarrito> items = new ConcurrentHashMap<>();
    /*
    * Clase interna para representar un ítem en el carrito
    */
    public static class ItemCarrito {

        private Producto producto;
        private int cantidad;

        public ItemCarrito(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
        
        public double calcularSubtotal() {
            return producto.getPrecio() * cantidad;
        }
        
        @Override
        public String toString() {
            return String.format("%dx %s - S/.%.2f", 
                cantidad, producto.getNombre(), calcularSubtotal());
        }
    }
    
    /*
    * Resultado de operaciones del carrito
    */
    public static class ResultadoOperacion {

        private final boolean exitoso;
        private final String mensaje;

        private ResultadoOperacion(boolean exitoso, String mensaje) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
        }

        public static ResultadoOperacion exito(String mensaje) {
            return new ResultadoOperacion(true, mensaje);
        }

        public static ResultadoOperacion fallo(String mensaje) {
            return new ResultadoOperacion(false, mensaje);
        }

        public boolean isExitoso() {
            return exitoso;
        }

        public String getMensaje() {
            return mensaje;
        }
    }
    
    // OPERACIONES PRINCIPALES
    
    /*
    * Agrega un producto al carrito.
    */
    public ResultadoOperacion agregarProducto(Producto producto, int cantidad) {
        // Validaciones
        if (producto == null) {
            return ResultadoOperacion.fallo("El producto no puede ser nulo");
        }

        if (cantidad <= 0) {
            return ResultadoOperacion.fallo("La cantidad debe ser mayor a cero");
        }

        String codigo = producto.getCodigo();

        // Verificar si ya existe en el carrito
        if (items.containsKey(codigo)) {
            ItemCarrito itemExistente = items.get(codigo);
            int nuevaCantidad = itemExistente.getCantidad() + cantidad;

            // Validar stock disponible
            if (!producto.hayStockDisponible(nuevaCantidad)) {
                return ResultadoOperacion.fallo(
                        String.format("Stock insuficiente. Disponible: %d, En carrito: %d, Solicitado: %d",
                                producto.getStock(), itemExistente.getCantidad(), cantidad)
                );
            }

            itemExistente.setCantidad(nuevaCantidad);
            return ResultadoOperacion.exito(
                    String.format("Cantidad actualizada: %dx %s", nuevaCantidad, producto.getNombre())
            );
        }

        // Nuevo producto en el carrito
        if (!producto.hayStockDisponible(cantidad)) {
            return ResultadoOperacion.fallo(
                    String.format("Stock insuficiente. Disponible: %d, Solicitado: %d",
                            producto.getStock(), cantidad)
            );
        }

        items.put(codigo, new ItemCarrito(producto, cantidad));
        return ResultadoOperacion.exito(
                String.format("Agregado: %dx %s", cantidad, producto.getNombre())
        );
    }
    
    /*
    * Remueve un producto del carrito completamente o reduce su cantidad
    */
    public ResultadoOperacion removerProducto(String codigoProducto, int cantidad) {
        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            return ResultadoOperacion.fallo("Código de producto inválido");
        }

        ItemCarrito item = items.get(codigoProducto);

        if (item == null) {
            return ResultadoOperacion.fallo("El producto no está en el carrito");
        }

        // Si cantidad es 0 o mayor/igual a la cantidad actual, remover completamente
        if (cantidad <= 0 || cantidad >= item.getCantidad()) {
            items.remove(codigoProducto);
            return ResultadoOperacion.exito(
                    String.format("Producto removido: %s", item.getProducto().getNombre())
            );
        }

        // Reducir cantidad
        int nuevaCantidad = item.getCantidad() - cantidad;
        item.setCantidad(nuevaCantidad);
        return ResultadoOperacion.exito(
                String.format("Cantidad reducida: %dx %s", nuevaCantidad, item.getProducto().getNombre())
        );
    }
    
    /*
    * Actualiza la cantidad de un producto específico
    */
    public ResultadoOperacion actualizarCantidad(String codigoProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            return removerProducto(codigoProducto, Integer.MAX_VALUE);
        }

        ItemCarrito item = items.get(codigoProducto);

        if (item == null) {
            return ResultadoOperacion.fallo("El producto no está en el carrito");
        }

        if (!item.getProducto().hayStockDisponible(nuevaCantidad)) {
            return ResultadoOperacion.fallo(
                    String.format("Stock insuficiente. Disponible: %d",
                            item.getProducto().getStock())
            );
        }

        item.setCantidad(nuevaCantidad);
        return ResultadoOperacion.exito(
                String.format("Cantidad actualizada: %dx %s",
                        nuevaCantidad, item.getProducto().getNombre())
        );
    }
    
    // CÁLCULOS
    
    /*
    * Calcula el total del carrito
    */
    public double calcularTotal() {
        return items.values().stream()
                .mapToDouble(ItemCarrito::calcularSubtotal)
                .sum();
    }
    
    /*
    * Calcula el subtotal de un producto específico
    */
    public double calcularSubtotal(String codigoProducto) {
        ItemCarrito item = items.get(codigoProducto);
        return item != null ? item.calcularSubtotal() : 0.0;
    }

    public Map<Producto, Integer> getItems() {
        return this.items;
    }

    public void vaciarCarrito() {
        this.items.clear();
    }

    public boolean checkout() {
        for (Map.Entry<Producto, Integer> entry : items.entrySet()) {
            Producto producto = entry.getKey();
            int cantidadComprada = entry.getValue();

            Producto productoEnCatalogo = Ecommerce.buscarProductoPorHash(producto.getCodigo());

            if (productoEnCatalogo != null) {
                productoEnCatalogo.setStock(productoEnCatalogo.getStock() - cantidadComprada);
            }
        }

        vaciarCarrito();
        return true;
    }
}
