/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author USER
 */
public class Ecommerce {

    //Simulación de catálogo desordenado para probar el algoritmo de ordenamiento
    public static Producto[] Lista = {
        new Producto(103, "Laptop Gamer"),
        new Producto(105, "Teclado Mecánico"),
        new Producto(101, "Monitor"),
        new Producto(102, "Mouse Inalambrico"),
        new Producto(104, "Auriculares")
    };

    public static Producto[] catalogo = Arrays.copyOf(Lista, Lista.length);

    public static void resetCatalogo() {
        catalogo = Arrays.copyOf(Lista, Lista.length);
    }
    
    public static Producto[] getCatalogo(){
        return catalogo;
    }

    public static Producto buscarProductoPorId(int id) {
        for (Producto p : catalogo) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static void ordenarCatalogoPorId() {
        int n = catalogo.length;
        for (int i = 1; i < n; i++) {
            Producto key = catalogo[i];
            int j = i - 1;

            while (j >= 0 && catalogo[j].getId() > key.getId()) {
                catalogo[j + 1] = catalogo[j];
                j = j - 1;
            }
            catalogo[j + 1] = key;
        }
    }

    public static void ordenarCatalogoPorNombre() {
        Arrays.sort(catalogo, Comparator.comparing(Producto::getNombre));
    }

    public static void main(String[] args) {
        System.out.println("--- Catalogo antes de la ordenacion ---");
        for (Producto p : catalogo) {
            System.out.println(p);
        }

        ordenarCatalogoPorId();

        System.out.println("--- Catalogo despues de la ordenacion por ID ---");
        for (Producto p : catalogo) {
            System.out.println(p);
        }

        ordenarCatalogoPorNombre();
        System.out.println("--- Catalogo despues de la ordenacion por Nombre ---");
        for (Producto p : catalogo) {
            System.out.println(p);
        }

        System.out.println("\nBuscando producto con ID 104 en el catálogo ya ordenado...");
        Producto productoEncontrado = buscarProductoPorId(104);
        System.out.println("Producto encontrado: " + (productoEncontrado != null ? productoEncontrado.getNombre() : "No encontrado"));
    }
}
