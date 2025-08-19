/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author USER
 */
public class Producto {
    private int id;
    private String nombre;
    
    public Producto(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    
    @Override
    public String toString(){
        return "ID: " + id + ", Nombre: " + nombre;
    }
}


