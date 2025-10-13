/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import negocio.Producto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import negocio.Producto;

/**
 *
 * @author USER
 */
public class EstructuraHash
{
    private Map<String, Producto> porCodigo;
    private Map<String, List<Producto>> porCategoria;
    private Map<Double, List<Producto>> porPrecio;
    
    public EstructuraHash() {
        porCodigo = new HashMap<>();
        porCategoria = new HashMap<>();
        porPrecio = new HashMap<>();
    }
    
    // Agregar producto a índices
    public void agregar(Producto producto) {
        // Índice por ID
        porCodigo.put(producto.getCodigo(), producto);
        
        // Índice por categoría
        porCategoria.computeIfAbsent(producto.getCategoria(), k -> new ArrayList<>())
                   .add(producto);
        
        // Índice por precio (redondeado)
        double precioRedondeado = Math.round(producto.getPrecio() * 100.0) / 100.0;
        porPrecio.computeIfAbsent(precioRedondeado, k -> new ArrayList<>())
                .add(producto);
    }
    
    public Producto buscarPorId(String id) {
        return porCodigo.get(id);
    }
    
    public List<Producto> buscarPorCategoria(String categoria) {
        return porCategoria.getOrDefault(categoria, new ArrayList<>());
    }
    
    public List<Producto> buscarPorPrecio(double precio) {
        double precioRedondeado = Math.round(precio * 100.0) / 100.0;
        return porPrecio.getOrDefault(precioRedondeado, new ArrayList<>());
    }

}
