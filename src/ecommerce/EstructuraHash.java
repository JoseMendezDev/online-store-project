/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author USER
 */
public class EstructuraHash {

    private static HashMap<Integer, Producto> productoMap = new HashMap<>();

    public static void inicializar(ArrayList<Producto> catalogo) {
        productoMap.clear();
        for (Producto p : catalogo) {
            productoMap.put(p.getCodigo(), p);
        }
    }

    public static void agregarProducto(Producto producto) {
        productoMap.put(producto.getCodigo(), producto);
    }

    public static Producto buscarProducto(int codigo) {
        return productoMap.get(codigo);
    }
}
