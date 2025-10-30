/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package domain;

import estructuras.Busqueda;
import estructuras.CatalogoHash;
import estructuras.ListaInvertida;
import estructuras.OrdenacionExterna;
import estructuras.OrdenacionInterna;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Clase principal de gestión del catálogo de productos. Maneja la
 * inicialización, persistencia (lectura opcional), búsqueda, ordenación y
 * paginación del catálogo.
 */
public class Ecommerce {

    private static final String ARCHIVO = "catalogo_productos.txt";
    private static final int PRODUCTOS_POR_PAGINA = 30;
    private static ArrayList<Producto> catalogo;
    private static java.io.File archivoPersistenciaActual;

    //Simulación de catálogo desordenado para probar el algoritmo de ordenamiento
    public static ArrayList<Producto> CATALOGO_ORIGINAL = new ArrayList<>(Arrays.asList(
            // Codigo, Nombre, Precio, Stock, Categoria, Rating    
            new Producto("523654", "Monitor Ultrawide", 350.50, 15, "Periféricos", 4.5),
            new Producto("265843", "Laptop Gamer", 1200.00, 5, "Portátiles", 4.3),
            new Producto("545154", "Auriculares Bluetooth", 99.99, 40, "Audio", 4.7),
            new Producto("523457", "Teclado Mecánico", 150.00, 25, "Periféricos", 4.8),
            new Producto("244846", "Mouse Inalámbrico", 75.25, 30, "Periféricos", 4.2),
            new Producto("695328", "Silla Ergonomica", 250.75, 10, "Mobiliario", 4.0),
            new Producto("662374", "Disco Duro Externo 1TB", 60.00, 50, "Almacenamiento", 4.6),
            new Producto("985263", "Webcam HD", 45.50, 35, "Periféricos", 4.4),
            new Producto("752236", "Micrófono USB", 85.00, 20, "Audio", 4.6),
            new Producto("412576", "Tarjeta Gráfica RTX 4070", 800.00, 8, "Componentes", 4.2)
    ));

    static {
        boolean cargaExitosa = intentarCargarCatalogo();

        if (!cargaExitosa) {
            catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
            System.out.println("⚠️ Fallo en la carga de archivo o selección cancelada. Usando catálogo por defecto.");
        }

        CatalogoHash.inicializar(catalogo);

        ListaInvertida.inicializar(catalogo);
    }

    private static boolean intentarCargarCatalogo() {
        File archivoDefault = new File(ARCHIVO);

        if (archivoDefault.exists() && archivoDefault.length() > 0) {
            archivoPersistenciaActual = archivoDefault;
            return cargarCatalogoDesdeArchivoEncontrado(archivoPersistenciaActual);
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione el archivo de catálogo: " + ARCHIVO);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Texto (*.txt)", "txt"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();

            if (!archivoSeleccionado.getName().equals(ARCHIVO)) {
                JOptionPane.showMessageDialog(null,
                        "El archivo seleccionado debe llamarse '" + ARCHIVO + "'. Por favor, inténtelo de nuevo.",
                        "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            archivoPersistenciaActual = archivoSeleccionado;
            return cargarCatalogoDesdeArchivoEncontrado(archivoPersistenciaActual);

        } else {
            return false;
        }
    }

    private static boolean cargarCatalogoDesdeArchivoEncontrado(File archivo) {
        try {
            catalogo = cargarCatalogoDesdeArchivo(archivo);
            System.out.println("✅ Catálogo cargado EXITOSAMENTE desde la ruta: " + archivo.getAbsolutePath());
            return true;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("❌ ERROR: Fallo al leer o parsear el archivo. Mensaje: " + e.getMessage());
            return false;
        }
    }

    static {
        File archivoPersistencia = new File(ARCHIVO);
        try {
            if (archivoPersistencia.exists() && archivoPersistencia.length() > 0) {
                catalogo = cargarCatalogoDesdeArchivo(archivoPersistencia);
                System.out.println("✅ Catálogo cargado EXITOSAMENTE desde el archivo: " + ARCHIVO);
            } else {
                catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
                System.out.println("⚠️ Archivo de persistencia no encontrado. Usando catálogo por defecto.");
            }
        } catch (java.io.IOException | IllegalArgumentException e) {
            System.err.println("❌ ERROR: Fallo al leer o parsear el archivo. Usando catálogo por defecto. Mensaje: " + e.getMessage());
            catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
        } finally {
            CatalogoHash.inicializar(catalogo);
        }
    }

    public static ArrayList<Producto> cargarCatalogoDesdeArchivo(File archivo) throws IOException {
        ArrayList<Producto> productos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    productos.add(Producto.fromString(line));
                }
            }
        }
        return productos;
    }

    public static void guardarCatalogoEnArchivo() throws IOException {
        try (FileWriter writer = new FileWriter(ARCHIVO)) {
            for (Producto p : catalogo) {
                writer.write(p.toFileString());
                writer.write(System.lineSeparator());
            }
        }
    }

    public static void resetCatalogo() {
        if (archivoPersistenciaActual != null && archivoPersistenciaActual.exists()) {
            if (cargarCatalogoDesdeArchivoEncontrado(archivoPersistenciaActual)) {
                CatalogoHash.inicializar(catalogo);
                System.out.println("🔄 Catálogo reseteado en memoria: Datos recargados desde la ruta guardada.");
                return;
            }
        }

        catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
        CatalogoHash.inicializar(CATALOGO_ORIGINAL);
        System.out.println("⚠️ Fallback: Catálogo reseteado a valores de fábrica solo en memoria.");

        ListaInvertida.inicializar(catalogo);
        
    }

    public static ArrayList<Producto> getCatalogo() {
        return catalogo;
    }

    public static int getTotalPaginas() {
        if (catalogo == null || catalogo.isEmpty()) {
            return 1;
        }
        return (int) Math.ceil((double) catalogo.size() / PRODUCTOS_POR_PAGINA);
    }

    public static ArrayList<Producto> getPagina(int numeroPagina) {
        if (catalogo == null || catalogo.isEmpty()) {
            return new ArrayList<>();
        }

        int totalProductos = catalogo.size();
        int totalPaginas = getTotalPaginas();

        if (numeroPagina < 1 || numeroPagina > totalPaginas) {
            return new ArrayList<>();
        }

        int inicio = (numeroPagina - 1) * PRODUCTOS_POR_PAGINA;
        int fin = Math.min(inicio + PRODUCTOS_POR_PAGINA, totalProductos);

        List<Producto> sublista = catalogo.subList(inicio, fin);
        return new ArrayList<>(sublista);
    }

    public static void setCatalogo(ArrayList<Producto> nuevoCatalogo) {
        catalogo = nuevoCatalogo;
        CatalogoHash.inicializar(catalogo);
    }

    public static boolean agregarProducto(Producto nuevoProducto) {
        if (CatalogoHash.existeCodigo(nuevoProducto.getCodigo())) {
            return false;
        }
        catalogo.add(nuevoProducto);
        CatalogoHash.agregarProducto(nuevoProducto);
        return true;
    }

    public static ArrayList<String> getCategoriasUnicas() {
        Set<String> categorias = new HashSet<>();
        for (Producto p : catalogo) {
            categorias.add(p.getCategoria());
        }
        ArrayList<String> sortedCategories = new ArrayList<>(categorias);
        Collections.sort(sortedCategories);
        return sortedCategories;
    }

    public static ArrayList<Producto> buscarPorPalabraClave(String palabra) {
        List<String> codigosEncontrados = ListaInvertida.buscarPorPalabra(palabra);
        ArrayList<Producto> productos = new ArrayList<>();

        for (String codigo : codigosEncontrados) {
            Producto p = CatalogoHash.buscarProducto(codigo);
            if (p != null) {
                productos.add(p);
            }
        }
        return productos;
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
        return CatalogoHash.buscarProducto(codigo);
    }
}
