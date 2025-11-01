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

    // EXCEPCIONES ESPECÍFICAS
    /**
     * Cuando un producto no se encuentra
     */
    class ProductoNoEncontradoException extends EcommerceException {

        public ProductoNoEncontradoException(String codigo) {
            super("Producto no encontrado: " + codigo);
        }
    }

    /**
     * Cuando hay problemas con el stock
     */
    class StockInsuficienteException extends EcommerceException {

        private final String codigoProducto;
        private final int stockDisponible;
        private final int stockSolicitado;

        public StockInsuficienteException(String codigoProducto, int stockDisponible, int stockSolicitado) {
            super(String.format("Stock insuficiente para producto %s. Disponible: %d, Solicitado: %d",
                    codigoProducto, stockDisponible, stockSolicitado));
            this.codigoProducto = codigoProducto;
            this.stockDisponible = stockDisponible;
            this.stockSolicitado = stockSolicitado;
        }

        public String getCodigoProducto() {
            return codigoProducto;
        }

        public int getStockDisponible() {
            return stockDisponible;
        }

        public int getStockSolicitado() {
            return stockSolicitado;
        }
    }

    /**
     * Cuando un código de producto ya existe
     */
    class ProductoDuplicadoException extends EcommerceException {

        public ProductoDuplicadoException(String codigo) {
            super("Ya existe un producto con el código: " + codigo);
        }
    }

    /**
     * Cuando hay errores de validación
     */
    class ValidacionException extends EcommerceException {

        public ValidacionException(String message) {
            super(message);
        }
    }

    /**
     * Cuando hay errores de persistencia
     */
    class PersistenciaException extends EcommerceException {

        public PersistenciaException(String message, Throwable cause) {
            super(message, cause);
        }
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
