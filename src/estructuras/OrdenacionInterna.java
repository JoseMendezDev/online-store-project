/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import domain.Producto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * Clase que contiene todos los algoritmos de ordenación interna (en memoria).
 */
public class OrdenacionInterna {
    
    public static void ordenarPorPrecio(ArrayList<Producto> catalogo) {
        Collections.sort(catalogo, Comparator.comparingDouble(Producto::getPrecio));
    }

    public static void ordenarPorInsercion(ArrayList<Producto> catalogo) {
        int n = catalogo.size();
        for (int i = 1; i < n; i++) {
            Producto key = catalogo.get(i);
            int j = i - 1;
            while (j >= 0 && catalogo.get(j).getCodigo().compareTo(key.getCodigo()) > 0) {
                catalogo.set(j + 1, catalogo.get(j));
                j = j - 1;
            }
            catalogo.set(j + 1, key);
        }
    }

    //Ordena catálogo de productos por Codigo utilizando el algoritmo de ordenación Shell
    public static void ordenarPorShellSort(ArrayList<Producto> catalogo) {
        int n = catalogo.size();
        int salto = n / 2;

        while (salto > 0) {
            for (int i = salto; i < n; i++) {
                Producto temp = catalogo.get(i);
                int j = i;
                while (j >= salto && catalogo.get(j - salto).getCodigo().compareTo(temp.getCodigo()) > 0) {
                    catalogo.set(j, catalogo.get(j - salto));
                    j -= salto;
                }
                catalogo.set(j, temp);
            }
            salto /= 2;
        }
    }

    public static void ordenarPorNombre(ArrayList<Producto> catalogo) {
        Collections.sort(catalogo, Comparator.comparing(Producto::getNombre));
    }
    
    
}
