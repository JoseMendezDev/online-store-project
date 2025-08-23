/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package ecommerce;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class Ecommerce {

    public static ArrayList<Producto> catalogo = new ArrayList<>();

    public static Producto buscarProductoPorId(int id) {
        for (Producto p : catalogo) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    public static void mostrarCatalogo() {
        if (catalogo.isEmpty()) {
            System.out.println("El catalogo esta vacio.");
        } else {
            System.out.println("Catalogo de productos:");
            for (Producto p : catalogo) {
                System.out.println(p);
            }
        }
    }

public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        
        //usuario ingresa cuántos productos quiere añadir
        System.out.print("Cuantos productos deseas agregar al catalogo? ");
        int numProductos = scanner.nextInt();
        scanner.nextLine();
        
        for (int i = 0; i < numProductos; i++) {
            System.out.print("Ingrese el ID del producto " + (i + 1) + ": ");
            int id = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Ingrese el nombre del producto " + (i + 1) + ": ");
            String nombre = scanner.nextLine();
            catalogo.add(new Producto(id, nombre));
            System.out.println("Producto " + (i + 1) + " agregado con exito.");
        }

        do {
            System.out.println("\n===== MENU ECOMMERCE =====");
            System.out.println("1. Agregar producto");
            System.out.println("2. Buscar producto por ID");
            System.out.println("3. Mostrar catálogo");
            System.out.println("4. Ordenar catálogo por ID");
            System.out.println("5. Ordenar catálogo por Nombre");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el ID del producto: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer
                    System.out.print("Ingrese el nombre del producto: ");
                    String nombre = scanner.nextLine();
                    catalogo.add(new Producto(id, nombre));
                    System.out.println("Producto agregado con exito.");
                    break;
                case 2:
                    System.out.print("Ingrese el ID del producto a buscar: ");
                    int idBuscar = scanner.nextInt();
                    Producto producto = buscarProductoPorId(idBuscar);
                    if (producto != null) {
                        System.out.println("Producto encontrado: " + producto);
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                    break;
                case 3:
                    mostrarCatalogo();
                    break;
                case 4:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }

        } while (opcion != 4);

        scanner.close();
    }
}