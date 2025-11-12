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
public class BusquedaBinaria {
     /*
     * Búsqueda binaria - O(log n) REQUISITO: El catálogo DEBE estar ordenado
     * por código
     */
    public static Producto buscarBinaria(ArrayList<Producto> catalogo, String codigo) {
        if (catalogo == null || codigo == null || catalogo.isEmpty()) {
            return null;
        }

        int izquierda = 0;
        int derecha = catalogo.size() - 1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            String codigoMedio = catalogo.get(medio).getCodigo();
            int comparacion = codigoMedio.compareTo(codigo);

            if (comparacion == 0) {
                return catalogo.get(medio);
            }

            if (comparacion < 0) {
                izquierda = medio + 1;
            } else {
                derecha = medio - 1;
            }
        }
        return null;
    }
}
