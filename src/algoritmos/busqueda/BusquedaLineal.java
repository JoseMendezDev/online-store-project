/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos.busqueda;

import domain.Producto;
import java.util.ArrayList;

/**
 *
 * @author USER
 */
public class BusquedaLineal {
     /*
     * Búsqueda Lineal - O(n)
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
}
