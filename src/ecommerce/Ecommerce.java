/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author USER
 */
public class Ecommerce {

    //Simulación de catálogo desordenado para probar el algoritmo de ordenamiento
    public static ArrayList<Producto> CATALOGO_ORIGINAL = new ArrayList<>(Arrays.asList(
        new Producto(103, "Laptop Gamer", 3500.00),
        new Producto(105, "Teclado Mecanico", 149.99),
        new Producto(101, "Monitor",350.50),
        new Producto(102, "Mouse Inalambrico", 99.99),
        new Producto(104, "Auriculares", 129.00),
        new Producto(202, "Silla de Escritorio Ergonomica", 1200.00),
        new Producto(204, "Disco Duro Externo", 199.99),
        new Producto(201, "Webcam HD", 250.00),
        new Producto(203, "Micrófono USB", 125.50),
        new Producto(205, "Tarjeta Grafica RTX 4070", 3990.90),
        new Producto(305, "Fuente de Poder 850W", 150.00),
        new Producto(302, "Memoria RAM 16GB", 175.50),
        new Producto(303, "Procesador Intel i9", 780.00),
        new Producto(304, "SSD NVMe 1TB", 670.00),
        new Producto(301, "Refrigeracion Liquida", 250.00),
        new Producto(401, "Mousepad XL", 90.90),
        new Producto(404, "Adaptador HDMI", 14.99),
        new Producto(403, "Cable USB-C", 25.50),
        new Producto(405, "Estabilizador", 180.00),
        new Producto(402, "USB 32 GB", 39.99)
    ));

    public static ArrayList<Producto> catalogo = new ArrayList<>(CATALOGO_ORIGINAL);

    public static void resetCatalogo() {
        catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
    }
    
    public static void agregarProducto(Producto nuevoProducto){
        catalogo.add(nuevoProducto);
    }
    
    public static ArrayList<Producto> getCatalogo() {
        return catalogo;
    }

    public static void ordenarCatalogoPorPrecio() {
        OrdenacionInterna.ordenarPorPrecio(catalogo);
    }
    
    public static void ordenarCatalogoPorId() {
        OrdenacionInterna.ordenarPorInsercion(catalogo);
    }

    public static void ordenarCatalogoPorNombre() {
        OrdenacionInterna.ordenarPorNombre(catalogo);
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
    
    public static Producto buscarProductoPorIdBinaria(int id) {
        return Busqueda.buscarBinaria(catalogo, id);
    }
}