/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Cuando un código de producto ya existe
 *
 * @author USER
 */
class ProductoDuplicadoException extends EcommerceException {

    public ProductoDuplicadoException(String codigo) {
        super("Ya existe un producto con el código: " + codigo);
    }
}
