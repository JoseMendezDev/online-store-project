/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import domain.Producto;
import estructuras.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Servicio centralizado para la gestión del catálogo de productos.
 *
 * @author USER
 */
public class CatalogoService {

    private CatalogoService() {
        inicializar();
    }

    public static CatalogoService getInstance() {
        if (instance == null) {
            synchronized (CatalogoService.class) {
                if (instance == null) {
                    instance = new CatalogoService();
                }
            }
        }
        return instance;
    }

    // CONFIGURACIÓN
    private static final int PRODUCTOS_POR_PAGINA = 30;
    private static final String ARCHIVO_DEFAULT = "catalogo_productos.txt";

    // ESTADO
    private final List<Producto> catalogo = new ArrayList<>();
    private final Map<String, Producto> indiceRapido = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private File archivoActual;
    private boolean modificado = false;

    // INICIALIZACIÓN
    private void inicializar() {
        lock.writeLock().lock();
        try {
            cargarCatalogoInicial();
            construirIndices();
            CatalogoHash.inicializar(new ArrayList<>(catalogo));
            ListaInvertida.inicializar(catalogo);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void cargarCatalogoInicial() {
        File archivo = new File(ARCHIVO_DEFAULT);

        if (archivo.exists() && archivo.length() > 0) {
            try {
                cargarDesdeArchivo(archivo);
                this.archivoActual = archivo;
                System.out.println("✅ Catálogo cargado desde: " + archivo.getAbsolutePath());
                return;
            } catch (IOException e) {
                System.err.println("⚠️ Error al cargar archivo: " + e.getMessage());
            }
        }

        // Fallback: catálogo por defecto
        cargarCatalogoDefecto();
        System.out.println("📦 Catálogo por defecto cargado");
    }

    private void cargarCatalogoDefecto() {
        catalogo.addAll(Arrays.asList(
                Producto.crear("523654", "Monitor Ultrawide", 350.50, 15, "Periféricos", 4.5),
                Producto.crear("265843", "Laptop Gamer", 1200.00, 5, "Portátiles", 4.3),
                Producto.crear("545154", "Auriculares Bluetooth", 99.99, 40, "Audio", 4.7),
                Producto.crear("523457", "Teclado Mecánico", 150.00, 25, "Periféricos", 4.8),
                Producto.crear("244846", "Mouse Inalámbrico", 75.25, 30, "Periféricos", 4.2),
                Producto.crear("695328", "Silla Ergonomica", 250.75, 10, "Mobiliario", 4.0),
                Producto.crear("662374", "Disco Duro Externo 1TB", 60.00, 50, "Almacenamiento", 4.6),
                Producto.crear("985263", "Webcam HD", 45.50, 35, "Periféricos", 4.4),
                Producto.crear("752236", "Micrófono USB", 85.00, 20, "Audio", 4.6),
                Producto.crear("412576", "Tarjeta Gráfica RTX 4070", 800.00, 8, "Componentes", 4.2)
        ));
    }

    private void construirIndices() {
        indiceRapido.clear();
        for (Producto p : catalogo) {
            indiceRapido.put(p.getCodigo(), p);
        }
    }

    // OPERACIONES DE LECTURA
    /**
     * Obtiene una copia del catálogo completo
     */
    public List<Producto> obtenerCatalogo() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(catalogo);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Obtiene productos de una página específica
     */
    public List<Producto> obtenerPagina(int numeroPagina) {
        lock.readLock().lock();
        try {
            if (catalogo.isEmpty() || numeroPagina < 1) {
                return Collections.emptyList();
            }

            int totalPaginas = calcularTotalPaginas();
            if (numeroPagina > totalPaginas) {
                return Collections.emptyList();
            }

            int inicio = (numeroPagina - 1) * PRODUCTOS_POR_PAGINA;
            int fin = Math.min(inicio + PRODUCTOS_POR_PAGINA, catalogo.size());

            return new ArrayList<>(catalogo.subList(inicio, fin));
        } finally {
            lock.readLock().unlock();
        }
    }

    public int calcularTotalPaginas() {
        lock.readLock().lock();
        try {
            return (int) Math.ceil((double) catalogo.size() / PRODUCTOS_POR_PAGINA);
        } finally {
            lock.readLock().unlock();
        }
    }

    public int obtenerTamanoCatalogo() {
        lock.readLock().lock();
        try {
            return catalogo.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    // BÚSQUEDAS
    public Optional<Producto> buscarPorCodigo(String codigo) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(indiceRapido.get(codigo));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        lock.readLock().lock();
        try {
            return catalogo.stream()
                    .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Producto> buscarPorRangoPrecio(double min, double max) {
        lock.readLock().lock();
        try {
            return catalogo.stream()
                    .filter(p -> p.getPrecio() >= min && p.getPrecio() <= max)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Producto> buscarPorPalabraClave(String palabraClave) {
        List<String> codigos = ListaInvertida.buscarPorPalabra(palabraClave);

        lock.readLock().lock();
        try {
            return codigos.stream()
                    .map(indiceRapido::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<String> obtenerCategoriasUnicas() {
        lock.readLock().lock();
        try {
            return catalogo.stream()
                    .map(Producto::getCategoria)
                    .collect(Collectors.toCollection(TreeSet::new)); // TreeSet para ordenar
        } finally {
            lock.readLock().unlock();
        }
    }

}
