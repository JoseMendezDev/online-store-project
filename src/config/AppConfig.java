/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author USER
 */
public class AppConfig {
    
    // Base de datos / Archivos
    public static final String ARCHIVO_CATALOGO = "catalogo_productos.txt";
    
    // Paginación
    public static final int PRODUCTOS_POR_PAGINA = 30;
    
    // Hash Table
    public static final int TAMANO_TABLA_HASH = 10;
    
    // Ordenación Externa
    public static final int MEMORIA_MAXIMA_ORDENACION = 100;
    
    // UI
    public static final int ANCHO_VENTANA = 1100;
    public static final int ALTO_VENTANA = 690;
    
    // Autenticación
    public static final String USUARIO_ADMIN = "admin";
    public static final String HASH_PASSWORD_ADMIN = "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f";
    
    // Versión
    public static final String VERSION = "1.0";
    public static final String NOMBRE_APP = "E-commerce System";
    
    // Configuración del carrito
    public static final int CANTIDAD_MAXIMA_POR_PRODUCTO = 50;
    
    // Rutas de archivos (si se necesitan)
    public static final String RUTA_DATOS = "data/";
    public static final String RUTA_LOGS = "logs/";
    
    // Constructor privado para evitar instanciación
    private AppConfig() {
        throw new IllegalStateException("Clase de configuración - No instanciable");
    }
}
