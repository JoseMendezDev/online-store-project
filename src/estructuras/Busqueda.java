/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import domain.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que contiene algoritmos de búsqueda.
 *
 */
public class Busqueda {

    /**
     * Búsqueda Lineal - O(n)
     *
     */
    public static Producto buscarLineal(ArrayList<Producto> catalogo, String codigo) {
        if (catalogo == null || codigo == null) {
            return null;
        }

        for (Producto p : catalogo) {
            if (codigo.equals(p.getCodigo())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Búsqueda binaria - O(log n) REQUISITO: El catálogo DEBE estar ordenado
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
     * Encuentra el índice del primero producto con precio >= al especificado
     * REQUISITO: productos DEBE estar ordenado por precio
     */
    public static int buscarPrimerMayorIgual(List<Producto> productosOrdenados, double precio) {
        if (productosOrdenados == null || productosOrdenados.isEmpty()) {
            return -1;
        }

        int izq = 0;
        int der = productosOrdenados.size() - 1;
        int resultado = -1;

        while (izq <= der) {
            int mid = izq + (der - izq) / 2;

            if (productosOrdenados.get(mid).getPrecio() >= precio) {
                resultado = mid;
                der = mid - 1; // Continuar buscando a la izquierda
            } else {
                izq = mid + 1;
            }
        }
        return resultado;
    }

    /**
     * Búsqueda en rango de precios - O(n) Para catálogos no ordenados
     */
    public static List<Producto> buscarEnRangoPrecio(List<Producto> productos,
            double precioMin, double precioMax) {
        List<Producto> resultados = new ArrayList<>();

        if (productos == null || precioMin < 0 || precioMax < precioMin) {
            return resultados;
        }

        for (Producto p : productos) {
            double precio = p.getPrecio();
            if (precio >= precioMin && precio <= precioMax) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    /**
     * Búsqueda optimizada en rango - O(k) donde k es el tamaño del resultado
     * REQUISITO: Productos deben estar ordenado por precio
     */
    public static List<Producto> buscarEnRango(List<Producto> productosOrdenados,
            double precioMin, double precioMax) {
        List<Producto> resultados = new ArrayList<>();

        if (productosOrdenados == null || precioMin < 0 || precioMax < precioMin) {
            return resultados;
        }

        int inicio = buscarPrimerMayorIgual(productosOrdenados, precioMin);

        if (inicio == -1) {
            return resultados; // No hay productos en el rango
        }

        for (int i = inicio; i < productosOrdenados.size(); i++) {
            double precio = productosOrdenados.get(i).getPrecio();
            if (precio <= precioMax) {
                resultados.add(productosOrdenados.get(i));
            } else {
                break; // Ya se pasó el límite superior
            }
        }
        return resultados;
    }
    
    /**
     * Búsqueda por categoría - O(n)
     * 
     */
    
    public static List<Producto> buscarPorCategoria(List<Producto> productos, String categoria) {
        List<Producto> resultados = new ArrayList<>();
        
        if (productos == null || categoria == null) {
            return resultados;
        }
        
        String categoriaLower = categoria.toLowerCase().trim();
        
        for (Producto p : productos) {
            if (p.getCategoria().toLowerCase().equals(categoriaLower)) {
                resultados.add(p);
            }
        }
        
        return resultados;
    }
}
