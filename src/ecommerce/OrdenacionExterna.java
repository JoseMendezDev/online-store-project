/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * Clase que contiene todos los algoritmos de ordenación externa (fuera de memoria).
 */
public class OrdenacionExterna {

    public static void ordenarPorFusionNatural(Producto[] catalogo) {
        int n = catalogo.length;
        Producto[] tempArray = new Producto[n];

        boolean ordenado = false;
        while (!ordenado) {
            ordenado = true;
            int i = 0;
            while (i < n) {
                int j = i;
                while (j < n - 1 && catalogo[j].getId() <= catalogo[j + 1].getId()) {
                    j++;
                }
                if (j == n - 1) {
                    break;
                }
                int k = j + 1;
                while (k < n - 1 && catalogo[k].getId() <= catalogo[k + 1].getId()) {
                    k++;
                }
                ordenado = false;
                int p1 = i, p2 = j + 1, p3 = i;
                while (p1 <= j && p2 <= k) {
                    if (catalogo[p1].getId() <= catalogo[p2].getId()) {
                        tempArray[p3++] = catalogo[p1++];
                    } else {
                        tempArray[p3++] = catalogo[p2++];
                    }
                }
                while (p1 <= j) {
                    tempArray[p3++] = catalogo[p1++];
                }
                while (p2 <= k) {
                    tempArray[p3++] = catalogo[p2++];
                }
                for (int l = i; l <= k; l++) {
                    catalogo[l] = tempArray[l];
                }
                i = k + 1;
            }
        }
    }
}
