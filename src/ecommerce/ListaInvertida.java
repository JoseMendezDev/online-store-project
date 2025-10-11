/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public class ListaInvertida {

    // El índice: Palabra clave (String) -> Lista de códigos de producto (List<String>)
    private static Map<String, List<String>> indiceInvertido;

    /**
     * Inicializa o reconstruye el índice invertido a partir de una lista de
     * productos.
     *
     * @param catalogo La lista completa de productos.
     */
    public static void inicializar(List<Producto> catalogo) {
        indiceInvertido = new HashMap<>();

        for (Producto p : catalogo) {
            // Indexamos por Nombre y Categoría
            indexarTexto(p.getNombre(), p.getCodigo());
            indexarTexto(p.getCategoria(), p.getCodigo());
        }
        System.out.println("✅ Lista Invertida inicializada. Total de tokens únicos: " + indiceInvertido.size());
    }

    /**
     * Procesa una cadena de texto, la divide en tokens y los añade al índice.
     *
     * @param texto El texto a indexar (Nombre o Categoría).
     * @param codigo El código del producto asociado.
     */
    private static void indexarTexto(String texto, String codigo) {
        // Normalización: pasar a minúsculas, remover puntuación y dividir por espacios
        String[] tokens = texto.toLowerCase()
                .replaceAll("[^a-zA-Z0-9 ]", "") // Remueve caracteres no alfanuméricos
                .split("\\s+"); // Divide por uno o más espacios

        for (String token : tokens) {
            if (token.length() < 2) {
                continue; // Ignorar tokens muy cortos
            }
            // Añadir el código al listado del token, si no existe.
            indiceInvertido.computeIfAbsent(token, k -> new ArrayList<>()).add(codigo);
        }
    }

    /**
     * Busca productos cuyos Nombres o Categorías contengan la palabra clave.
     *
     * @param palabraClave La palabra a buscar.
     * @return Una lista de códigos de productos que contienen la palabra.
     */
    public static List<String> buscarPorPalabra(String palabraClave) {
        if (indiceInvertido == null) {
            return new ArrayList<>();
        }

        // Normalizar la búsqueda
        String tokenBuscado = palabraClave.toLowerCase().trim();

        // Buscar coincidencias exactas o parciales (sugerencia de autocompletado)
        List<String> resultados = new ArrayList<>();

        // 1. Coincidencia Exacta (primero y más eficiente)
        if (indiceInvertido.containsKey(tokenBuscado)) {
            resultados.addAll(indiceInvertido.get(tokenBuscado));
            return resultados;
        }

        // 2. Coincidencia Parcial (para búsquedas más flexibles)
        for (Map.Entry<String, List<String>> entry : indiceInvertido.entrySet()) {
            if (entry.getKey().contains(tokenBuscado)) {
                // Usamos un conjunto temporal para evitar códigos duplicados si indexamos Nombre y Categoría
                resultados.addAll(entry.getValue());
            }
        }

        // Eliminar duplicados si usamos búsqueda parcial y combinamos resultados
        return new ArrayList<>(new HashSet<>(resultados));
    }
}
