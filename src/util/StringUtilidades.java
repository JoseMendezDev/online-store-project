/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author USER
 */
public class StringUtilidades {
    
    public static String repetir(String str, int count) {
        if (count <= 0) return "";

        StringBuilder sb = new StringBuilder(count * str.length());
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    public static String truncar(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    public static boolean esVacio(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static String capitalizar(String str) {
        if (esVacio(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    } 
}
