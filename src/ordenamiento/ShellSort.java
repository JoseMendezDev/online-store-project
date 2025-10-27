/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ordenamiento;

import utilidades.Utilidades;
import java.util.ArrayList;
import negocio.Producto;

/**
 * 
 * @author USER
 */
public class ShellSort {

    public static void ordenarPorRating(ArrayList<Producto> A) {
        int n = A.size();
        //Inicialización de la secuencia de brechas (h)
        int h = 1;
        while (h < n / 3) {
            h = 3 * h + 1;
        }
        //Ordenamiento por inserción para cada brecha
        while (h >= 1) {
            for (int i = h; i < n; i++) {

                int j = i;
                while (j >= h && A.get(j).getRating() < A.get(j - h).getRating()) {
                    // intercambiar A[j] con A[j-h]
                    Utilidades.intercambiar(A, j, j - h);

                    j = j - h;
                }
            }
            h = h / 3;
        }
    }
}
