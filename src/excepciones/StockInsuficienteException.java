/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Cuando hay problemas con el stock
 * @author USER
 */
public class StockInsuficienteException extends EcommerceException {

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
