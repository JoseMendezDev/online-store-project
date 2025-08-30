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
        new Producto(105, "Teclado Mecánico"),
        new Producto(101, "Monitor"),
        new Producto(102, "Mouse Inalambrico"),
        new Producto(104, "Auriculares")
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

    public static void main(String[] args) {

        /*
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Cuantos productos deseas agregar al catalogo? ");
        int numProductos = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < numProductos; i++) {
            System.out.print("Ingrese el ID del producto " + (i + 1) + ": ");
            int id = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Ingrese el nombre del producto " + (i + 1) + ": ");
            String nombre = scanner.nextLine();
            catalogo.add(new Producto(id, nombre));
            System.out.println("Producto " + (i + 1) + " agregado con exito.");
        }
         */
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

        ordenarCatalogoPorFusionNatural();
        System.out.println("--- Catalogo despues de la ordenacion por Fusion Natural ---");
        for (Producto p : catalogo) {
            System.out.println(p);
        }

        System.out.println("\nBuscando producto con ID 104 en el catálogo ya ordenado...");
        Producto productoEncontrado = buscarProductoPorId(104);
        System.out.println("Producto encontrado: " + (productoEncontrado != null ? productoEncontrado.getNombre() : "No encontrado"));
    }        
}
