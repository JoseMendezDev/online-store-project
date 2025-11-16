/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import domain.Producto;
import config.AppConfig;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author USER
 */
public class ProductoRepository {
    private final File archivo;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public ProductoRepository() {
        this.archivo = new File(AppConfig.ARCHIVO_CATALOGO);
    }
    
    public ProductoRepository(File archivoPersonalizado) {
        this.archivo = archivoPersonalizado;
    }
    
    /**
     * Carga todos los productos del archivo
     */
    public List<Producto> cargarTodos() throws IOException {
        lock.readLock().lock();
        try {
            if (!archivo.exists() || archivo.length() == 0) {
                return new ArrayList<>();
            }
            
            List<Producto> productos = new ArrayList<>();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                int numeroLinea = 0;
                
                while ((linea = reader.readLine()) != null) {
                    numeroLinea++;
                    linea = linea.trim();
                    
                    if (linea.isEmpty()) continue;
                    
                    try {
                        Producto producto = Producto.fromString(linea);
                        productos.add(producto);
                    } catch (IllegalArgumentException e) {
                        System.err.printf("⚠️ Error línea %d: %s%n", numeroLinea, e.getMessage());
                    }
                }
            }
            
            return productos;
            
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Guarda todos los productos en el archivo
     */
    public void guardarTodos(List<Producto> productos) throws IOException {
        lock.writeLock().lock();
        try {
            // Crear directorios si no existen
            File directorio = archivo.getParentFile();
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                for (Producto producto : productos) {
                    writer.write(producto.toFileString());
                    writer.newLine();
                }
            }
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Verifica si el archivo existe y tiene contenido
     */
    public boolean existeArchivo() {
        return archivo.exists() && archivo.length() > 0;
    }
    
    /**
     * Obtiene la ruta del archivo
     */
    public String getRutaArchivo() {
        return archivo.getAbsolutePath();
    }
}
