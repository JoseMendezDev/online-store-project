/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

import java.util.*;

/**
 *
 * Clase que contiene todos los algoritmos de ordenación externa (fuera de
 * memoria).
 */
public class OrdenacionExterna {

    public static void ordenarPorFusionNatural(ArrayList<Producto> catalogo) {
        int n = catalogo.size();
        ArrayList<Producto> tempArray = new ArrayList<>(Collections.nCopies(n, null));

        boolean ordenado = false;
        while (!ordenado) {
            ordenado = true;
            int i = 0;
            while (i < n) {
                int j = i;
                while (j < n - 1 && catalogo.get(j).getCodigo().compareTo(catalogo.get(j + 1).getCodigo()) <= 0) {
                    j++;
                }
                if (j == n - 1) {
                    break;
                }
                int k = j + 1;
                while (k < n - 1 && catalogo.get(k).getCodigo().compareTo(catalogo.get(k + 1).getCodigo()) <=0 ) {
                    k++;
                }
                ordenado = false;

                int p1 = i, p2 = j + 1, p3 = i;
                while (p1 <= j && p2 <= k) {
                    if (catalogo.get(p1).getCodigo().compareTo(catalogo.get(p2).getCodigo()) <= 0) {
                        tempArray.set(p3++, catalogo.get(p1++));
                    } else {
                        tempArray.set(p3++, catalogo.get(p2++));
                    }
                }
                while (p1 <= j) {
                    tempArray.set(p3++, catalogo.get(p1++));
                }
                while (p2 <= k) {
                    tempArray.set(p3++, catalogo.get(p2++));
                }

                for (int l = i; l <= k; l++) {
                    catalogo.set(l, tempArray.get(l));
                }
                i = k + 1;
            }
        }
    }

    public static List<List<Integer>> crearCorridas(List<Integer> datos, int tamBloque) {
        List<List<Integer>> corridas = new ArrayList<>();
        for (int i = 0; i < datos.size(); i += tamBloque) {
            List<Integer> bloque = datos.subList(i, Math.min(i + tamBloque, datos.size()));
            List<Integer> ordenado = new ArrayList<>(bloque);
            Collections.sort(ordenado);
            corridas.add(ordenado);
        }
        return corridas;
    }

    /*    public static List<Integer> mezclaEquilibrada(List<List<Integer>> corridas) {
        Queue<List<Integer>> cola = new LinkedList<>(corridas);
        while (cola.size() > 1) {
            List<Integer> corrida1 = cola.poll();
            List<Integer> corrida2 = cola.poll();
            cola.offer(mezclarCorridas(corrida1, corrida2));
        }
        return cola.poll();
    }
    
     */
    public static List<Integer> metodoPolifasico(List<List<Integer>> corridas) {
        Queue<List<Integer>> a = new LinkedList<>();
        Queue<List<Integer>> b = new LinkedList<>();
        return null;
    }
}
