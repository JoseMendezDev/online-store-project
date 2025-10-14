/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Clase que representa un cliente
 */
public class Cliente {

    private String id;
    private String nombre;
    private String email;
    private String direccion;
    private String telefono;
    private List<String> historialCompras;
    private LocalDateTime fechaRegistro;

    public Cliente(String id, String nombre, String email, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.historialCompras = new ArrayList<>();
        this.fechaRegistro = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public List<String> getHistorialCompras() {
        return historialCompras;
    }
}
