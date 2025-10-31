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
}
