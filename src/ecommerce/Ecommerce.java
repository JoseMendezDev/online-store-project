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
        new Producto(103, "Monitor Ultrawide", 350.50, 15, "Periféricos"), 
        new Producto(101, "Laptop Gamer", 1200.00, 5, "Portátiles"),
        new Producto(105, "Auriculares Bluetooth", 99.99, 40, "Audio"),
        new Producto(102, "Teclado Mecánico", 150.00, 25, "Periféricos"),
        new Producto(104, "Mouse Inalámbrico", 75.25, 30, "Periféricos"),
        new Producto(201, "Silla Ergonomica", 250.75, 10, "Mobiliario"),
        new Producto(202, "Disco Duro Externo 1TB", 60.00, 50, "Almacenamiento"),
        new Producto(203, "Webcam HD", 45.50, 35, "Periféricos"),
        new Producto(204, "Micrófono USB", 85.00, 20, "Audio"),
        new Producto(205, "Tarjeta Gráfica RTX 4070", 800.00, 8, "Componentes")
    ));

    public static ArrayList<Producto> catalogo = new ArrayList<>(CATALOGO_ORIGINAL);

    static {
        EstructuraHash.inicializar(CATALOGO_ORIGINAL);
    }

    public static void resetCatalogo() {
        catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
        EstructuraHash.inicializar(CATALOGO_ORIGINAL);
    }

    public static ArrayList<Producto> getCatalogo() {
        return catalogo;
    }

    public static void agregarProducto(Producto nuevoProducto) {
        catalogo.add(nuevoProducto);
        EstructuraHash.agregarProducto(nuevoProducto);
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
    
    public static Producto buscarProductoPorHash(int id) {
        return EstructuraHash.buscarProducto(id);
    }
}
