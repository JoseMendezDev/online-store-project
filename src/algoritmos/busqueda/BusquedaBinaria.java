/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos.busqueda;

import domain.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class BusquedaBinaria {
     /*
     * Búsqueda binaria iterativa - O(log n) REQUISITO: El catálogo DEBE estar ordenado
     * por código
     */
    public static Producto buscarBinaria(ArrayList<Producto> catalogo, String codigo) {
        if (catalogo == null || codigo == null || catalogo.isEmpty()) {
            return null;
        }

        int izquierda = 0;
        int derecha = catalogo.size() - 1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            String codigoMedio = catalogo.get(medio).getCodigo();
            int comparacion = codigoMedio.compareTo(codigo);

            if (comparacion == 0) {
                return catalogo.get(medio);
            }

            if (comparacion < 0) {
                izquierda = medio + 1;
            } else {
                derecha = medio - 1;
            }
        }
        return null;
    }
    
    /**
     * Búsqueda binaria recursiva
     */
    public static Producto buscarRecursivo(List<Producto> productos, String codigo) {
        return buscarRecursivoHelper(productos, codigo, 0, productos.size() - 1);
    }
    
    private static Producto buscarRecursivoHelper(List<Producto> productos, 
                                                  String codigo, int inicio, int fin) {
        if (inicio > fin) return null;
        
        int medio = inicio + (fin - inicio) / 2;
        Producto productoMedio = productos.get(medio);
        int comparacion = productoMedio.getCodigo().compareTo(codigo);
        
        if (comparacion == 0) return productoMedio;
        if (comparacion > 0) return buscarRecursivoHelper(productos, codigo, inicio, medio - 1);
        return buscarRecursivoHelper(productos, codigo, medio + 1, fin);
    }
    
    /**
     * Búsqueda binaria iterativa
     */
    public static Producto buscarIterativo(List<Producto> productos, String codigo) {
        int inicio = 0;
        int fin = productos.size() - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            Producto productoMedio = productos.get(medio);
            int comparacion = productoMedio.getCodigo().compareTo(codigo);

            if (comparacion == 0) {
                return productoMedio;
            }
            if (comparacion > 0) {
                fin = medio - 1;
            } else {
                inicio = medio + 1;
            }
        }

        return null;
    }
}
