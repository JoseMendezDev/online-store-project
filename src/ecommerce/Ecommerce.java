/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class Ecommerce {

    /*
    public static ArrayList<Producto> catalogo = new ArrayList<>();

    public static void mostrarCatalogo() {
        if (catalogo.isEmpty()) {
            System.out.println("El catalogo esta vacio.");
        } else {
            System.out.println("Catalogo de productos:");
            for (Producto p : catalogo) {
                System.out.println(p);
            }
        }
    }
    
    //Ordena catálogo de productos por ID utilizando el algoritmo de ordenación Shell
    public static void ordenarCatalogoShellSort() {
        int n = catalogo.length;
        int salto = n / 2;

        while (salto > 0) {
            for (int i = salto; i < n; i++) {
                Producto temp = catalogo[i];
                int j = i;
                while (j >= salto && catalogo[j - salto].getId() > temp.getId()) {
                    catalogo[j] = catalogo[j - salto];
                    j -= salto;
                }
                catalogo[j] = temp;
            }
            salto /= 2;
        }
    }
     */
    //Simulación de catálogo desordenado para probar el algoritmo de ordenamiento
    public static Producto[] Lista = {
        new Producto(103, "Laptop Gamer"),
        new Producto(105, "Teclado Mecanico"),
        new Producto(101, "Monitor"),
        new Producto(102, "Mouse Inalambrico"),
        new Producto(104, "Auriculares"),
        new Producto(202, "Silla de Escritorio Ergonomica"),
        new Producto(204, "Disco Duro Externo"),
        new Producto(201, "Webcam HD"),
        new Producto(203, "Micrófono USB"),
        new Producto(205, "Tarjeta Grafica RTX 4070"),
        new Producto(305, "Fuente de Poder 850W"),
        new Producto(302, "Memoria RAM 16GB"),
        new Producto(303, "Procesador Intel i9"),
        new Producto(304, "SSD NVMe 1TB"),
        new Producto(301, "Refrigeracion Liquida"),
        new Producto(401, "Mousepad XL"),
        new Producto(404, "Adaptador HDMI"),
        new Producto(403, "Cable USB-C"),
        new Producto(405, "Estabilizador"),
        new Producto(402, "USB 32 GB")
    };

    public static Producto[] catalogo = Arrays.copyOf(Lista, Lista.length);

    public static void resetCatalogo() {
        catalogo = Arrays.copyOf(Lista, Lista.length);
    }

    public static Producto[] getCatalogo() {
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

    //Ordenamiento por insercion
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

//Ordenar el catálogo de productos por ID utilizando Fusión Natural (Natural Merge Sort).
    public static void ordenarCatalogoPorFusionNatural() {
        int n = catalogo.length;
        Producto[] tempArray = new Producto[n];

        boolean ordenado = false;
        while (!ordenado) {
            ordenado = true;
            int i = 0;

            while (i < n) {
                int j = i;
                while (j < n - 1 && catalogo[j].getId() <= catalogo[j + 1].getId()) {
                    j++;
                }
                if (j == n - 1) {
                    break;
                }

                int k = j + 1;
                while (k < n - 1 && catalogo[k].getId() <= catalogo[k + 1].getId()) {
                    k++;
                }

                ordenado = false;

                int p1 = i, p2 = j + 1, p3 = i;
                while (p1 <= j && p2 <= k) {
                    if (catalogo[p1].getId() <= catalogo[p2].getId()) {
                        tempArray[p3++] = catalogo[p1++];
                    } else {
                        tempArray[p3++] = catalogo[p2++];
                    }
                }
                while (p1 <= j) {
                    tempArray[p3++] = catalogo[p1++];
                }
                while (p2 <= k) {
                    tempArray[p3++] = catalogo[p2++];
                }

                for (int l = i; l <= k; l++) {
                    catalogo[l] = tempArray[l];
                }

                i = k + 1;
            }
        }
    }
}