/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Cuando un producto no se encuentra
 *
 * @author USER
 */
public class ProductoNoEncontradoException extends EcommerceException {

    public ProductoNoEncontradoException(String codigo) {
        super("Producto no encontrado: " + codigo);
    }

}
