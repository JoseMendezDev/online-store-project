/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * Clase que contiene todos los algoritmos de busqueda.
 */
public class Busqueda {
    
        public static Producto buscarLineal(Producto[] catalogo, int id) {
        for (Producto p : catalogo) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
