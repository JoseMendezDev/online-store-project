/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos.indexacion;

import domain.Producto;
import config.AppConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de Tabla Hash con Encadenamiento Separado
 * para búsqueda rápida de productos
 */
public class TablaHash {
    private static List<Producto>[] tabla;
    
    @SuppressWarnings("unchecked")
    public static void inicializar(ArrayList<Producto> catalogoInicial) {
        tabla = new List[AppConfig.TAMANO_TABLA_HASH];
        
        for (int i = 0; i < AppConfig.TAMANO_TABLA_HASH; i++) {
            tabla[i] = new ArrayList<>();
        }
        
        for (Producto p : catalogoInicial) {
            agregarProducto(p);
        }
    }
    
    /**
     * Función hash: extrae dígitos 4-5 del código y aplica módulo
     */
    private static int funcionHash(String codigo) {
        if (codigo == null || codigo.length() < 6) {
            return 0;
        }
        
        try {
            String substring = codigo.substring(4, 6);
            int valor = Integer.parseInt(substring);
            return valor % AppConfig.TAMANO_TABLA_HASH;
        } catch (NumberFormatException e) {
            return Math.abs(codigo.hashCode() % AppConfig.TAMANO_TABLA_HASH);
        }
    }
    
    /**
     * Agregar producto a la tabla hash
     */
    public static void agregarProducto(Producto producto) {
        if (tabla == null) {
            throw new IllegalStateException("Tabla hash no inicializada");
        }
        
        int indice = funcionHash(producto.getCodigo());
        tabla[indice].add(producto);
    }
    
    /**
     * Buscar producto por código - O(1) promedio
     */
    public static Producto buscarProducto(String codigo) {
        if (tabla == null) {
            return null;
        }
        
        int indice = funcionHash(codigo);
        
        for (Producto p : tabla[indice]) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        
        return null;
    }
    
    /**
     * Verificar si existe un código
     */
    public static boolean existeCodigo(String codigo) {
        return buscarProducto(codigo) != null;
    }
    
    /**
     * Obtener estadísticas de la tabla hash
     */
    public static String obtenerEstadisticas() {
        if (tabla == null) {
            return "Tabla no inicializada";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Estadísticas de Tabla Hash:\n");
        sb.append("Tamaño: ").append(AppConfig.TAMANO_TABLA_HASH).append("\n");
        
        int totalProductos = 0;
        int slotsUsados = 0;
        int maxColisiones = 0;
        
        for (int i = 0; i < tabla.length; i++) {
            int tamaño = tabla[i].size();
            if (tamaño > 0) {
                slotsUsados++;
                totalProductos += tamaño;
                maxColisiones = Math.max(maxColisiones, tamaño - 1);
            }
        }
        
        sb.append("Productos totales: ").append(totalProductos).append("\n");
        sb.append("Slots usados: ").append(slotsUsados).append("/").append(tabla.length).append("\n");
        sb.append("Factor de carga: ").append(String.format("%.2f", (double) totalProductos / tabla.length)).append("\n");
        sb.append("Máximas colisiones: ").append(maxColisiones).append("\n");
        
        return sb.toString();
    }
}
