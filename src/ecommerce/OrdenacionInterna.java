/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * Clase que contiene todos los algoritmos de ordenación interna (en memoria).
 */
public class OrdenacionInterna {
    
    public static void ordenarPorPrecio(Producto[] catalogo){
        Arrays.sort(catalogo, Comparator.comparingDouble(Producto::getPrecio));
    }
    
    //Ordenamiento por insercion

    public static void ordenarPorInsercion(Producto[] catalogo) {
        int n = catalogo.length;
        for (int i = 1; i < n; i++) {
            Producto key = catalogo[i];
            int j = i - 1;

            while (j >= 0 && catalogo[j].getId() > key.getId()) {
                catalogo[j + 1] = catalogo[j];
                j = j - 1;
            }
            catalogo[j + 1] = key;
        }
    }

    //Ordena catálogo de productos por ID utilizando el algoritmo de ordenación Shell
    public static void ordenarPorShellSort(Producto[] catalogo) {
        int n = catalogo.length;
        int salto = n / 2;

        while (salto > 0) {
            for (int i = salto; i < n; i++) {
                Producto temp = catalogo[i];
                int j = i;
                while (j >= salto && catalogo[j - salto].getId() > temp.getId()) {
                    catalogo[j] = catalogo[j - salto];
                    j -= salto;
                }
                catalogo[j] = temp;
            }
            salto /= 2;
        }
    }

    public static void ordenarPorNombre(Producto[] catalogo) {
        Arrays.sort(catalogo, Comparator.comparing(Producto::getNombre));
    }
}
