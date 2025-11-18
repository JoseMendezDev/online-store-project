/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

    import domain.Producto;
    import service.CatalogoService;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.ArrayList;
/**
 *
 * @author USER
 */
public class CarritoService {

    private Map<Producto, Integer> items;

    public CarritoService() {
        this.items = new HashMap<>();
    }

    public boolean agregarProducto(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            return false;
        }

        if (producto.getStock() < cantidad) {
            return false;
        }

        int cantidadActual = items.getOrDefault(producto, 0);
        int nuevaCantidadTotal = cantidadActual + cantidad;

        if (producto.getStock() < nuevaCantidadTotal) {
            return false;
        }

        items.put(producto, nuevaCantidadTotal);
        return true;
    }

    public void removerProducto(Producto producto, int cantidad) {
        if (!items.containsKey(producto)) {
            return;
        }

        int cantidadActual = items.get(producto);

        if (cantidad >= cantidadActual || cantidad == 0) {
            items.remove(producto);
        } else {
            items.put(producto, cantidadActual - cantidad);
        }
    }

    public double calcularTotal() {
        double total = 0.0;
        for (Map.Entry<Producto, Integer> entry : items.entrySet()) {
            Producto p = entry.getKey();
            int cantidad = entry.getValue();
            total += p.getPrecio() * cantidad;
        }
        return total;
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

            Producto productoEnCatalogo = CatalogoService.buscarProductoPorHash(producto.getCodigo());

            if (productoEnCatalogo != null) {
                productoEnCatalogo.setStock(productoEnCatalogo.getStock() - cantidadComprada);
            }
        }

        vaciarCarrito();
        return true;
    }
}
