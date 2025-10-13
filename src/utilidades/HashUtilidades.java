/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author USER
 */
public class HashUtilidades {

    public static String generarHash(String password) {
        try {
            //uso de algoritmo de hash SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash: " + e.getMessage());
        }
    }
    
    /*
    public static void main(String[] args) {
        String passwordDePrueba = "password123";
        String hashGenerado = generarHash(passwordDePrueba);
        
        System.out.println("------------------------------------------");
        System.out.println("Contraseña: " + passwordDePrueba);
        System.out.println("HASH GENERADO");
        System.out.println(hashGenerado);
        System.out.println("------------------------------------------");
    }
    */
}
