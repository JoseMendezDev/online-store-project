/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 *
 * @author USER
 */
public class EcommerceException extends Exception {

    public EcommerceException(String message) {
        super(message);
    }

    public EcommerceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Cuando el carrito está vacío
     */
    class CarritoVacioException extends EcommerceException {

        public CarritoVacioException() {
            super("El carrito de compras está vacío");
        }
    }
}
