/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

/**
 *
 * @author USER
 */
public class Ecommerce {

    public static Producto[] catalogo = {
        new Producto(101, "Laptop Gamer"),
        new Producto(102, "Teclado Mecánico"),
        new Producto(103, "Monitor"),
        new Producto(104, "Mouse Inalambrico"),
        new Producto(105, "Auriculares"),};

    public static Producto buscarProductoPorId(int id) {
        for (Producto p : catalogo) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("Buscando producto con ID 103...");
        Producto productoEncontrado = buscarProductoPorId(103);

        if (productoEncontrado != null) {
            System.out.println("Producto encontrado: " + productoEncontrado.toString());
        } else {
            System.out.println("Producto no encontrado.");
        }

        System.out.println("\nBuscando producto con ID 999...");
        Producto productoNoEncontrado = buscarProductoPorId(999);

        if (productoNoEncontrado != null) {
            System.out.println("Producto encontrado: " + productoNoEncontrado.toString());
        } else {
            System.out.println("Producto no encontrado.");
        }
    }
}
