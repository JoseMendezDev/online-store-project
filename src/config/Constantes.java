/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author USER
 */
public class Constantes {
    
    // Mensajes
    public static final String MSG_EXITO_AGREGAR = "Producto agregado exitosamente";
    public static final String MSG_ERROR_CODIGO_DUPLICADO = "Ya existe un producto con ese código";
    public static final String MSG_ERROR_STOCK = "Stock insuficiente";
    
    // Validaciones
    public static final int LONGITUD_CODIGO = 6;
    public static final double RATING_MIN = 0.0;
    public static final double RATING_MAX = 5.0;
    
    // Formatos
    public static final String FORMATO_PRECIO = "S/.%.2f";
    public static final String FORMATO_FECHA = "dd/MM/yyyy HH:mm";
    
    private Constantes() {
        // No instanciable
    }
    
}
