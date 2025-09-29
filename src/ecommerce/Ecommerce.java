/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author USER
 */
public class Ecommerce {

    //Simulación de catálogo desordenado para probar el algoritmo de ordenamiento
    public static ArrayList<Producto> CATALOGO_ORIGINAL = new ArrayList<>(Arrays.asList(
        // Codigo, Nombre, Precio, Stock, Categoria    
        new Producto("523654", "Monitor Ultrawide", 350.50, 15, "Periféricos", 4.5), 
        new Producto("265843", "Laptop Gamer", 1200.00, 5, "Portátiles", 4.3),
        new Producto("545154", "Auriculares Bluetooth", 99.99, 40, "Audio", 4.7),
        new Producto("523457", "Teclado Mecánico", 150.00, 25, "Periféricos", 4.8),
        new Producto("244846", "Mouse Inalámbrico", 75.25, 30, "Periféricos", 4.2),
        new Producto("695328", "Silla Ergonomica", 250.75, 10, "Mobiliario", 4.0),
        new Producto("662374", "Disco Duro Externo 1TB", 60.00, 50, "Almacenamiento", 4.6),
        new Producto("985263", "Webcam HD", 45.50, 35, "Periféricos", 4.4),
        new Producto("752236", "Micrófono USB", 85.00, 20, "Audio",4.6),
        new Producto("412576", "Tarjeta Gráfica RTX 4070", 800.00, 8, "Componentes",4.2)
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

    public static boolean agregarProducto(Producto nuevoProducto) {
        if (EstructuraHash.existeCodigo(nuevoProducto.getCodigo())) {
            return false;
        }
        catalogo.add(nuevoProducto);
        EstructuraHash.agregarProducto(nuevoProducto);
        return true;
    }
    
    public static ArrayList<String> getCategoriasUnicas(){
        Set<String> categorias = new HashSet<>();
        for (Producto p : CATALOGO_ORIGINAL){
            categorias.add(p.getCategoria());
        }
        return new ArrayList<>(categorias);
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

    public static Producto buscarProductoPorCodigo(String codigo) {
        return Busqueda.buscarLineal(catalogo, codigo);
    }

    public static Producto buscarProductoPorCodigoBinaria(String codigo) {
        return Busqueda.buscarBinaria(catalogo, codigo);
    }
    
    public static Producto buscarProductoPorHash(String codigo) {
        return EstructuraHash.buscarProducto(codigo);
    }
}
