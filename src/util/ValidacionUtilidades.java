/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import config.Constantes;

/**
 *
 * @author USER
 */
public class ValidacionUtilidades {
    public static boolean validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        String codigoLimpio = codigo.trim();
        return codigoLimpio.length() == Constantes.LONGITUD_CODIGO && 
               codigoLimpio.matches("\\d+");
    }
    
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public static boolean validarRating(double rating) {
        return rating >= Constantes.RATING_MIN && rating <= Constantes.RATING_MAX;
    }
    
    public static boolean validarPrecio(double precio) {
        return precio >= 0 && !Double.isNaN(precio) && !Double.isInfinite(precio);
    }
    
    public static boolean validarStock(int stock) {
        return stock >= 0;
    }
}
