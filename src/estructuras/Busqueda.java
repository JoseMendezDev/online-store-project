/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import negocio.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Clase que contiene todos los algoritmos de busqueda.
 */
public class Busqueda {

    public static Producto buscarLineal(ArrayList<Producto> catalogo, String codigo)
    {
        for (Producto p : catalogo)
        {
            if (p.getCodigo() == codigo)
            {
                return p;
            }
        }
        return null;
    }

    public static Producto buscarBinaria(ArrayList<Producto> catalogo, String codigo)
    {
        int izquierda = 0;
        int derecha = catalogo.size() - 1;

        while (izquierda <= derecha)
        {
            int medio = izquierda + (derecha - izquierda) / 2;
            int comparacion = catalogo.get(medio).getCodigo().compareTo(codigo);

            if (comparacion == 0)
            {
                return catalogo.get(medio);
            }

            if (comparacion < 0)
            {
                izquierda = medio + 1;
            } else
            {
                derecha = medio - 1;
            }
        }
        return null;
    }
    
    public static int buscarPrimerMayorIgual(List<Producto> productosOrdenados, double precio)
    {
        int izq = 0;
        int der = productosOrdenados.size() - 1;
        int resultado = -1;

        while (izq <= der)
        {
            int mid = izq + (der - izq) / 2;

            if (productosOrdenados.get(mid).getPrecio() >= precio)
            {
                resultado = mid;
                der = mid - 1; // Seguir buscando a la izquierda
            } else
            {
                izq = mid + 1;
            }
        }
        return resultado;
    }

    public static List<Producto> buscarEnRangoPrecio(List<Producto> productos,
            double precioMin, double precioMax)
    {
        List<Producto> resultados = new ArrayList<>();

        for (Producto p : productos)
        {
            if (p.getPrecio() >= precioMin && p.getPrecio() <= precioMax)
            {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public static List<Producto> buscarEnRango(List<Producto> productosOrdenados,
            double precioMin, double precioMax)
    {
        int inicio = buscarPrimerMayorIgual(productosOrdenados, precioMin);

        if (inicio == -1)
        {
            return new ArrayList<>();
        }

        List<Producto> resultados = new ArrayList<>();
        for (int i = inicio; i < productosOrdenados.size(); i++)
        {
            double precio = productosOrdenados.get(i).getPrecio();
            if (precio <= precioMax)
            {
                resultados.add(productosOrdenados.get(i));
            } else
            {
                break;
            }
        }

        return resultados;
    }
}
