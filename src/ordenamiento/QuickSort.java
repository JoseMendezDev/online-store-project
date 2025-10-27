/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ordenamiento;

import java.util.ArrayList;
import negocio.Producto;

/**
 *
 * @author USER
 */
public class QuickSort {
    public static void ordenar(ArrayList<Producto> A) {

        ArrayList<Producto> listaOrdenada = quickSortRecursive(A);
        
        A.clear();
        A.addAll(listaOrdenada);
    }
    
    private static ArrayList<Producto> quickSortRecursive(ArrayList<Producto> A) {
        int n = A.size();

        if (n <= 1) {
            return A;
        }

        Producto P = A.get(0);

        ArrayList<Producto> L = new ArrayList<>(); // Menores que P
        ArrayList<Producto> E = new ArrayList<>(); // Iguales a P
        ArrayList<Producto> G = new ArrayList<>(); // Mayores que P
        
        for (Producto producto : A) {
            int comparacion = producto.getCodigo().compareTo(P.getCodigo());
            
            if (comparacion < 0) {
                L.add(producto);
            } else if (comparacion > 0) {
                G.add(producto);
            } else {
                E.add(producto);
            }
        }
//Ordenamiento de sublistas de pivote (derecha e izquierda)
        ArrayList<Producto> L_sorted = quickSortRecursive(L);
        ArrayList<Producto> G_sorted = quickSortRecursive(G);

        ArrayList<Producto> resultado = new ArrayList<>();
        resultado.addAll(L_sorted);
        resultado.addAll(E);
        resultado.addAll(G_sorted);
        
        return resultado;
    }  
}
