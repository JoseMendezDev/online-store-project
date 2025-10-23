/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ordenamiento;

import utilidades.Utilidades;
import java.util.ArrayList;
import negocio.Producto;

/**
 * Ordena el ArrayList de Productos usando ShellSort.
 *
 * @author USER
 */
public class ShellSort {

    public static void ordenar(ArrayList<Producto> A) {
        int n = A.size();

        int h = 1;
        while (h < n / 3) {
            h = 3 * h + 1;
        }

        while (h >= 1) {
            for (int i = h; i < n; i++) {

                int j = i;

                while (j >= h && A.get(j).getCodigo().compareTo(A.get(j - h).getCodigo()) < 0) {

                    Utilidades.intercambiar(A, j, j - h);

                    j = j - h;
                }
            }
            h = h / 3;
        }
    }
}
