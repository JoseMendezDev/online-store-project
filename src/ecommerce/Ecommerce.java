/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author USER
 */
public class Ecommerce {

    //Simulación de catálogo desordenado para probar el algoritmo de ordenamiento
    public static ArrayList<Producto> CATALOGO_ORIGINAL = new ArrayList<>(Arrays.asList(
        new Producto(523654, "Monitor Ultrawide", 350.50, 15, "Periféricos"), 
        new Producto(265843, "Laptop Gamer", 1200.00, 5, "Portátiles"),
        new Producto(545154, "Auriculares Bluetooth", 99.99, 40, "Audio"),
        new Producto(523457, "Teclado Mecánico", 150.00, 25, "Periféricos"),
        new Producto(244846, "Mouse Inalámbrico", 75.25, 30, "Periféricos"),
        new Producto(695328, "Silla Ergonomica", 250.75, 10, "Mobiliario"),
        new Producto(662374, "Disco Duro Externo 1TB", 60.00, 50, "Almacenamiento"),
        new Producto(985263, "Webcam HD", 45.50, 35, "Periféricos"),
        new Producto(752236, "Micrófono USB", 85.00, 20, "Audio"),
        new Producto(412576, "Tarjeta Gráfica RTX 4070", 800.00, 8, "Componentes")
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

    public static void ordenarCatalogoPorCodigo() {
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

    public static Producto buscarProductoPorCodigo(int codigo) {
        return Busqueda.buscarLineal(catalogo, codigo);
    }

    public static Producto buscarProductoPorCodigoBinaria(int codigo) {
        return Busqueda.buscarBinaria(catalogo, codigo);
    }
    
    public static Producto buscarProductoPorHash(int codigo) {
        return EstructuraHash.buscarProducto(codigo);
    }
}
