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

    public static void ordenarCatalogoPorId() {
        OrdenacionInterna.ordenarPorInsercion(catalogo);
    }

    public static void ordenarCatalogoPorNombre() {
        Arrays.sort(catalogo, Comparator.comparing(Producto::getNombre));
    }

    public static void ordenarCatalogoPorShellSort() {
        OrdenacionInterna.ordenarPorShellSort(catalogo);
    }

    public static void ordenarCatalogoPorFusionNatural() {
        OrdenacionExterna.ordenarPorFusionNatural(catalogo);
    }

    public static Producto buscarProductoPorId(int id) {
        return Busqueda.buscarLineal(catalogo, id);
    }
}