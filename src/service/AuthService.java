/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import domain.Usuario;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author USER
 */
public class AuthService {
    private static AuthService instance;
    private Map<String, Usuario> usuarios;
    private Usuario usuarioActual;
    
    private AuthService() {
        this.usuarios = new HashMap<>();
        inicializarUsuarios();
    }
    
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }
    
    private void inicializarUsuarios() {
        usuarios.put("admin", new Usuario("admin", hashPassword("password123"), "Administrador"));
    }
    
    public ResultadoAuth login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return new ResultadoAuth(false, "Usuario y contraseña son requeridos", null);
        }
        
        Usuario usuario = usuarios.get(username);
        
        if (usuario == null) {
            return new ResultadoAuth(false, "Usuario no encontrado", null);
        }
        
        if (!usuario.getPassword().equals(hashPassword(password))) {
            return new ResultadoAuth(false, "Contraseña incorrecta", null);
        }
        
        this.usuarioActual = usuario;
        return new ResultadoAuth(true, "Bienvenido, " + usuario.getNombre(), usuario);
    }
    
    public ResultadoAuth cambiarPassword(String passwordActual, String passwordNueva) {
        if (usuarioActual == null) {
            return new ResultadoAuth(false, "No hay sesión activa", null);
        }
        
        if (!usuarioActual.getPassword().equals(hashPassword(passwordActual))) {
            return new ResultadoAuth(false, "Contraseña actual incorrecta", null);
        }
        
        if (passwordNueva == null || passwordNueva.length() < 6) {
            return new ResultadoAuth(false, "La nueva contraseña debe tener al menos 6 caracteres", null);
        }
        
        usuarioActual.setPassword(hashPassword(passwordNueva));
        usuarios.put(usuarioActual.getUsername(), usuarioActual);
        
        return new ResultadoAuth(true, "Contraseña actualizada correctamente", usuarioActual);
    }
    
    public void logout() {
        this.usuarioActual = null;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public boolean estaAutenticado() {
        return usuarioActual != null;
    }
    
    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }
    
    public static class ResultadoAuth {
        private final boolean exitoso;
        private final String mensaje;
        private final Usuario usuario;
        
        public ResultadoAuth(boolean exitoso, String mensaje, Usuario usuario) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.usuario = usuario;
        }
        
        public boolean isExitoso() { return exitoso; }
        public String getMensaje() { return mensaje; }
        public Usuario getUsuario() { return usuario; }
    }
}
