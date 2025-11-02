/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Cuando hay errores de persistencia
 * @author USER
 */
class PersistenciaException extends EcommerceException {

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
