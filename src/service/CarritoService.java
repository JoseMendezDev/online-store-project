/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import domain.Producto;
import config.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author USER
 */
public class CarritoService {

    private Map<Producto, Integer> items;
    private CatalogoService catalogoService;

    public CarritoService() {
        this.items = new HashMap<>();
        this.catalogoService = CatalogoService.getInstance();
    }

    public ResultadoOperacion agregarProducto(Producto producto, int cantidad) {
        if (producto == null) {
            return new ResultadoOperacion(false, "Producto no válido");
        }

        if (cantidad <= 0) {
            return new ResultadoOperacion(false, "La cantidad debe ser mayor a 0");
        }

        if (cantidad > producto.getStock()) {
            return new ResultadoOperacion(false, "Stock insuficiente. Disponible: " + producto.getStock());
        }

        int cantidadActual = items.getOrDefault(producto, 0);
        int nuevaCantidad = cantidadActual + cantidad;

        if (nuevaCantidad > producto.getStock()) {
            return new ResultadoOperacion(false, "Excede el stock disponible");
        }

        if (nuevaCantidad > AppConfig.CANTIDAD_MAXIMA_POR_PRODUCTO) {
            return new ResultadoOperacion(false, "Máximo " + AppConfig.CANTIDAD_MAXIMA_POR_PRODUCTO + " unidades por producto");
        }

        items.put(producto, nuevaCantidad);

        String mensaje = cantidadActual == 0
                ? "Producto agregado al carrito"
                : "Cantidad actualizada en el carrito";

        return new ResultadoOperacion(true, mensaje);
    }

    public ResultadoOperacion actualizarCantidad(String codigo, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            return removerProducto(codigo);
        }

        Producto producto = buscarProductoEnCarrito(codigo);
        if (producto == null) {
            return new ResultadoOperacion(false, "Producto no encontrado en el carrito");
        }

        if (nuevaCantidad > producto.getStock()) {
            return new ResultadoOperacion(false, "Stock insuficiente. Disponible: " + producto.getStock());
        }

        if (nuevaCantidad > AppConfig.CANTIDAD_MAXIMA_POR_PRODUCTO) {
            return new ResultadoOperacion(false, "Máximo " + AppConfig.CANTIDAD_MAXIMA_POR_PRODUCTO + " unidades");
        }

        items.put(producto, nuevaCantidad);
        return new ResultadoOperacion(true, "Cantidad actualizada");
    }

    public ResultadoOperacion removerProducto(String codigo) {
        Producto producto = buscarProductoEnCarrito(codigo);
        if (producto == null) {
            return new ResultadoOperacion(false, "Producto no encontrado en el carrito");
        }

        items.remove(producto);
        return new ResultadoOperacion(true, "Producto eliminado del carrito");
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public Map<Producto, Integer> getItems() {
        return new HashMap<>(items);
    }

    public int obtenerCantidad(String codigo) {
        Producto producto = buscarProductoEnCarrito(codigo);
        return producto != null ? items.get(producto) : 0;
    }

    public int contarProductos() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int contarItems() {
        return items.size();
    }

    public double calcularTotal() {
        return items.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
                .sum();
    }

    public ResultadoOperacion procesarCompra() {
        if (estaVacio()) {
            return new ResultadoOperacion(false, "El carrito está vacío");
        }

        for (Map.Entry<Producto, Integer> entry : items.entrySet()) {
            Producto producto = entry.getKey();
            int cantidad = entry.getValue();

            if (producto.getStock() < cantidad) {
                return new ResultadoOperacion(false,
                        "Stock insuficiente para " + producto.getNombre()
                        + ". Disponible: " + producto.getStock());
            }
        }

        for (Map.Entry<Producto, Integer> entry : items.entrySet()) {
            catalogoService.reducirStock(entry.getKey().getCodigo(), entry.getValue());
        }

        double total = calcularTotal();
        int totalProductos = contarProductos();

        vaciar();

        return new ResultadoOperacion(true,
                String.format("✅ Compra procesada exitosamente!\n\n"
                        + "Productos: %d items\n"
                        + "Total pagado: S/.%.2f",
                        totalProductos, total));
    }

    private Producto buscarProductoEnCarrito(String codigo) {
        return items.keySet().stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public static class ResultadoOperacion {

        private final boolean exitoso;
        private final String mensaje;

        public ResultadoOperacion(boolean exitoso, String mensaje) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
        }

        public boolean isExitoso() {
            return exitoso;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    public static class ItemCarrito {

        private final Producto producto;
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

        public double getSubtotal() {
            return producto.getPrecio() * cantidad;
        }
    }
}
