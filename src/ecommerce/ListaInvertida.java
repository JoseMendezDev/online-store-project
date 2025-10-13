/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

import negocio.Producto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public class ListaInvertida {

    private static Map<String, List<String>> indiceInvertido;

    public static void inicializar(List<Producto> catalogo) {
        indiceInvertido = new HashMap<>();

        for (Producto p : catalogo) {
            indexarTexto(p.getNombre(), p.getCodigo());
            indexarTexto(p.getCategoria(), p.getCodigo());
        }
        System.out.println("✅ Lista Invertida inicializada. Total de tokens únicos: " + indiceInvertido.size());
    }

    private static void indexarTexto(String texto, String codigo) {

        String[] tokens = texto.toLowerCase()
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .split("\\s+");

        for (String token : tokens) {
            if (token.length() < 2) {
                continue;
            }
            indiceInvertido.computeIfAbsent(token, k -> new ArrayList<>()).add(codigo);
        }
    }

    public static List<String> buscarPorPalabra(String palabraClave) {
        if (indiceInvertido == null) {
            return new ArrayList<>();
        }

        String tokenBuscado = palabraClave.toLowerCase().trim();

        List<String> resultados = new ArrayList<>();

        if (indiceInvertido.containsKey(tokenBuscado)) {
            resultados.addAll(indiceInvertido.get(tokenBuscado));
            return resultados;
        }

        for (Map.Entry<String, List<String>> entry : indiceInvertido.entrySet()) {
            if (entry.getKey().contains(tokenBuscado)) {
                resultados.addAll(entry.getValue());
            }
        }

        return new ArrayList<>(new HashSet<>(resultados));
    }
}
