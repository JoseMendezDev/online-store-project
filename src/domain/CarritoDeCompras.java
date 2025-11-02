/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

import domain.Producto;
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
    
    /*
    * Cuenta el total de items en el carrito
    */
    public int contarItems() {
        return items.size();
    }
    
    /*
    * Cuenta el total de productos (suma de cantidades)
    */
    public int contarProductos() {
        return items.values().stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }
    
    // CONSULTAS
    
    /*
    * Obtiene una copia de los items del carrito
    */
    public Map<Producto, Integer> getItems() {
        Map<Producto, Integer> resultado = new LinkedHashMap<>();
        items.values().forEach(item -> 
            resultado.put(item.getProducto(), item.getCantidad())
        );
        return Collections.unmodifiableMap(resultado);
    }
    
        
    /*
    * Obtiene una lista de items del carrito
    */
    public List<ItemCarrito> getItemsList() {
        return new ArrayList<>(items.values());
    }
    
    /**
     * Verifica si el carrito está vacío
     */
    public boolean estaVacio() {
        return items.isEmpty();
    }
    
    /**
     * Verifica si un producto está en el carrito
     */
    public boolean contieneProducto(String codigoProducto) {
        return items.containsKey(codigoProducto);
    }
    
    /**
     * Obtiene la cantidad de un producto en el carrito
     */
    public int obtenerCantidad(String codigoProducto) {
        ItemCarrito item = items.get(codigoProducto);
        return item != null ? item.getCantidad() : 0;
    }
    
    // CHECKOUT
    
    /*
     * Procesa la compra y actualiza el stock 
     */

    public ResultadoOperacion procesarCompra() {
        if (estaVacio()) {
            return ResultadoOperacion.fallo("El carrito está vacío");
        }
        
        // Primero verificamos que todo el stock esté disponible
        List<String> errores = new ArrayList<>();
        
        for (ItemCarrito item : items.values()) {
            Producto producto = item.getProducto();
            int cantidad = item.getCantidad();
            
            if (!producto.hayStockDisponible(cantidad)) {
                errores.add(String.format("%s: stock insuficiente (disponible: %d, necesario: %d)",
                    producto.getNombre(), producto.getStock(), cantidad));
            }
        }
        
        if (!errores.isEmpty()) {
            return ResultadoOperacion.fallo(
                "No se puede completar la compra:\n" + String.join("\n", errores)
            );
        }
        
        // Actualizar stock de todos los productos
        for (ItemCarrito item : items.values()) {
            Producto producto = item.getProducto();
            int cantidad = item.getCantidad();
            
            if (!producto.reducirStock(cantidad)) {
                // Esto no debería ocurrir si las validaciones son correctas
                return ResultadoOperacion.fallo(
                    "Error inesperado al actualizar stock de " + producto.getNombre()
                );
            }
        }
        
        double total = calcularTotal();
        int totalItems = contarProductos();
        
        vaciar();
        
        return ResultadoOperacion.exito(
            String.format("Compra exitosa! Total: S/.%.2f (%d productos)", total, totalItems)
        );
    }
    
    /**
     * Vacía el carrito completamente
     */
    public void vaciar() {
        items.clear();
    }
    
    // VALIDACIONES
    
    /**
     * Valida que todos los productos en el carrito tengan stock disponible
     */
    public List<String> validarDisponibilidad() {
        List<String> errores = new ArrayList<>();
        
        for (ItemCarrito item : items.values()) {
            Producto producto = item.getProducto();
            int cantidad = item.getCantidad();
            
            if (!producto.hayStockDisponible(cantidad)) {
                errores.add(String.format(
                    "%s: stock insuficiente (disponible: %d, en carrito: %d)",
                    producto.getNombre(), producto.getStock(), cantidad
                ));
            }
        }
        
        return errores;
    }
    
    // UTILIDADES
    
    /**
     * Genera un resumen del carrito
     */
    public String generarResumen() {
        if (estaVacio()) {
            return "Carrito vacío";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("🛒 Carrito de Compras (%d items)\n", contarItems()));
        sb.append("─".repeat(50)).append("\n");
        
        for (ItemCarrito item : items.values()) {
            sb.append(String.format("• %s\n", item.toString()));
        }
        
        sb.append("─".repeat(50)).append("\n");
        sb.append(String.format("TOTAL: S/.%.2f\n", calcularTotal()));
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return generarResumen();
    }
    
    // MÉTODOS DE COMPATIBILIDAD
    
    @Deprecated
    public boolean agregarProducto_OLD(Producto producto, int cantidad) {
        return agregarProducto(producto, cantidad).isExitoso();
    }
    
    @Deprecated
    public boolean checkout() {
        return procesarCompra().isExitoso();
    }
    
    @Deprecated
    public void vaciarCarrito() {
        vaciar();
    }
    
}
