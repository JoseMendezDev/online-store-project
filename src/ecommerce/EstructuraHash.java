/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

import negocio.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class EstructuraHash
{
//Encadenamiento Separado (Separate Chaining)
    private static final int TAMANO_TABLA = 10;
    private static List<Producto>[] tabla;

    public static void inicializar(ArrayList<Producto> catalogoInicial)
    {
        tabla = new List[TAMANO_TABLA];
        for (int i = 0; i < TAMANO_TABLA; i++)
        {
            tabla[i] = new ArrayList<>();
        }

        for (Producto p : catalogoInicial)
        {
            agregarProducto(p);
        }
    }

    private static int funcionHash(String codigo)
    {
        if (codigo == null || codigo.length() < 6)
        {
            return 0;
        }
        try
        {
            String subString = codigo.substring(4, 6);
            int valor = Integer.parseInt(subString);

            return valor % TAMANO_TABLA;
        } catch (NumberFormatException e)
        {
            return codigo.hashCode() % TAMANO_TABLA;
        }
    }

    public static void agregarProducto(Producto producto)
    {
        int indice = funcionHash(producto.getCodigo());

        tabla[indice].add(producto);
    }

    public static Producto buscarProducto(String codigo)
    {
        int indice = funcionHash(codigo);

        for (Producto p : tabla[indice])
        {
            if (p.getCodigo().equals(codigo))
            {
                return p;
            }
        }
        return null;
    }

    public static boolean existeCodigo(String codigo)
    {
        return buscarProducto(codigo) != null;
    }
}
