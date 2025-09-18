/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

import java.util.ArrayList;

/**
 *
 * Clase que contiene todos los algoritmos de busqueda.
 */
public class Busqueda {

    public static Producto buscarLineal(ArrayList<Producto> catalogo, int id) {
        for (Producto p : catalogo) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

public static Producto buscarBinaria(ArrayList<Producto> catalogo, int id) {
        int izquierda = 0;
        int derecha = catalogo.size() - 1;
        
        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            
            if (catalogo.get(medio).getId() == id) {
                return catalogo.get(medio);
            }
            
            if (catalogo.get(medio).getId() < id) {
                izquierda = medio + 1;
            } 
            else {
                derecha = medio - 1;
            }
        }
        return null;
    }
}
