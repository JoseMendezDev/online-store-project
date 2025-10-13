/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import negocio.Producto;
import java.util.ArrayList;

/**
 *
 * @author USER
 */
public class Inventario {

    private ArrayList<Producto> productos;
    private ArrayList<Integer> stock;

    public Inventario() {
        this.productos = new ArrayList<>();
        this.stock = new ArrayList<>();
    }

    public Producto buscarProducto(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }
}
