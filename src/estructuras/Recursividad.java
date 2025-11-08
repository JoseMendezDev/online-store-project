/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import domain.Producto;
import java.util.*;

/**
 * Implementación de recursividad.
 * @author USER
 */
public class Recursividad {
    public static Producto busquedaBinariaRecursiva(List<Producto> productos, String codigo){
        return busquedaBinariaRecursivaHelper(productos, codigo, 0, productos.size() - 1);
    }
    
    private static Producto busquedaBinariaRecursivaHelper(
        List<Producto> productos, String codigo, int inicio, int fin){
        
        // CASO BASE: No encontrado
        if (inicio > fin) {
            return null;
        }
        
        // Calcular punto medio
        int medio = inicio + (fin - inicio) / 2;
        Producto productoMedio = productos.get(medio);
        int comparacion = productoMedio.getCodigo().compareTo(codigo);
        
        // CASO BASE: Encontrado
        if (comparacion == 0){
            return productoMedio;
        }
        
        // CASO RECURSIVO: Buscar en mitad izquierda o derecha
        if (comparacion > 0){
            return busquedaBinariaRecursivaHelper(productos, codigo, inicio, medio - 1);
        } else {
            return busquedaBinariaRecursivaHelper(productos, codigo, medio + 1, fin);
        }
    }
}
