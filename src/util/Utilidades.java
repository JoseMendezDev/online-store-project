/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.ArrayList;
import domain.Producto;

/**
* Intercambia dos elementos en un ArrayList.
*/
public class Utilidades {
    public static void intercambiar(ArrayList<Producto> A, int i, int j) {
        Producto temp = A.get(i);
        A.set(i, A.get(j));
        A.set(j, temp);
    }
}
