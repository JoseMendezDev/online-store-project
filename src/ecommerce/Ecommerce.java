/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author USER
 */
public class Ecommerce
{

    private static final String ARCHIVO = "catalogo_productos.txt";

    //Simulación de catálogo desordenado para probar el algoritmo de ordenamiento
    public static ArrayList<Producto> CATALOGO_ORIGINAL = new ArrayList<>(Arrays.asList(
            // Codigo, Nombre, Precio, Stock, Categoria    
            new Producto("523654", "Monitor Ultrawide", 350.50, 15, "Periféricos", 4.5),
            new Producto("265843", "Laptop Gamer", 1200.00, 5, "Portátiles", 4.3),
            new Producto("545154", "Auriculares Bluetooth", 99.99, 40, "Audio", 4.7),
            new Producto("523457", "Teclado Mecánico", 150.00, 25, "Periféricos", 4.8),
            new Producto("244846", "Mouse Inalámbrico", 75.25, 30, "Periféricos", 4.2),
            new Producto("695328", "Silla Ergonomica", 250.75, 10, "Mobiliario", 4.0),
            new Producto("662374", "Disco Duro Externo 1TB", 60.00, 50, "Almacenamiento", 4.6),
            new Producto("985263", "Webcam HD", 45.50, 35, "Periféricos", 4.4),
            new Producto("752236", "Micrófono USB", 85.00, 20, "Audio", 4.6),
            new Producto("412576", "Tarjeta Gráfica RTX 4070", 800.00, 8, "Componentes", 4.2)
    ));

    private static ArrayList<Producto> catalogo;

    private static final int PRODUCTOS_POR_PAGINA = 31;

    static
    {
        File archivoPersistencia = new File(ARCHIVO);
        try
        {
            if (archivoPersistencia.exists() && archivoPersistencia.length() > 0)
            {
                catalogo = cargarCatalogoDesdeArchivo(archivoPersistencia);
                System.out.println("Catálogo cargado desde " + ARCHIVO);
            } else
            {
                catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
                guardarCatalogoEnArchivo();
                System.out.println("Usando catálogo por defecto y guardando archivo.");
            }
        } catch (IOException e)
        {
            System.err.println("Error al manejar el archivo de catálogo. Usando default. " + e.getMessage());
            catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
        }
        EstructuraHash.inicializar(catalogo);
    }

    public static ArrayList<Producto> cargarCatalogoDesdeArchivo(File archivo) throws IOException
    {
        ArrayList<Producto> productos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (!line.trim().isEmpty())
                {
                    productos.add(Producto.fromString(line));
                }
            }
        }
        return productos;
    }

    public static void guardarCatalogoEnArchivo() throws IOException
    {
        try (FileWriter writer = new FileWriter(ARCHIVO))
        {
            for (Producto p : catalogo)
            {
                writer.write(p.toFileString());
                writer.write(System.lineSeparator());
            }
        }
    }

    public static void resetCatalogo()
    {
        catalogo = new ArrayList<>(CATALOGO_ORIGINAL);
        EstructuraHash.inicializar(CATALOGO_ORIGINAL);
        try
        {
            guardarCatalogoEnArchivo();
        } catch (IOException e)
        {
            System.err.println("Error al guardar el catálogo reseteado: " + e.getMessage());
        }
    }

    public static int getTotalPaginas()
    {
        if (catalogo == null || catalogo.isEmpty())
        {
            return 1;
        }
        return (int) Math.ceil((double) catalogo.size() / PRODUCTOS_POR_PAGINA);
    }

    public static ArrayList<Producto> getCatalogo()
    {
        return catalogo;
    }

    public static ArrayList<Producto> getPagina(int numeroPagina)
    {
        if (catalogo == null || catalogo.isEmpty())
        {
            return new ArrayList<>();
        }

        int totalProductos = catalogo.size();
        int totalPaginas = getTotalPaginas();

        if (numeroPagina < 1 || numeroPagina > totalPaginas)
        {
            return new ArrayList<>();
        }

        int inicio = (numeroPagina - 1) * PRODUCTOS_POR_PAGINA;
        int fin = Math.min(inicio + PRODUCTOS_POR_PAGINA, totalProductos);

        List<Producto> sublista = catalogo.subList(inicio, fin);
        return new ArrayList<>(sublista);
    }

    public static void setCatalogo(ArrayList<Producto> nuevoCatalogo)
    {
        catalogo = nuevoCatalogo;
        EstructuraHash.inicializar(catalogo);
        try
        {
            guardarCatalogoEnArchivo();
        } catch (IOException e)
        {
            System.err.println("Error al guardar el catálogo después de la ordenación: " + e.getMessage());
        }
    }

    public static boolean agregarProducto(Producto nuevoProducto)
    {
        if (EstructuraHash.existeCodigo(nuevoProducto.getCodigo()))
        {
            return false;
        }
        catalogo.add(nuevoProducto);
        EstructuraHash.agregarProducto(nuevoProducto);

        try
        {
            guardarCatalogoEnArchivo();
        } catch (IOException e)
        {
            System.err.println("Error al guardar el catálogo después de agregar un producto: " + e.getMessage());
        }
        return true;
    }

    public static ArrayList<String> getCategoriasUnicas()
    {
        Set<String> categorias = new HashSet<>();
        for (Producto p : catalogo)
        {
            categorias.add(p.getCategoria());
        }
        return new ArrayList<>(categorias);
    }

    public static void ordenarCatalogoPorPrecio()
    {
        OrdenacionInterna.ordenarPorPrecio(catalogo);
    }

    public static void ordenarCatalogoPorCodigo()
    {
        OrdenacionInterna.ordenarPorInsercion(catalogo);
    }

    public static void ordenarCatalogoPorNombre()
    {
        OrdenacionInterna.ordenarPorNombre(catalogo);
    }

    public static void ordenarCatalogoPorShellSort()
    {
        OrdenacionInterna.ordenarPorShellSort(catalogo);
    }

    public static void ordenarCatalogoPorFusionNatural()
    {
        OrdenacionExterna.ordenarPorFusionNatural(catalogo);
    }

    public static Producto buscarProductoPorCodigo(String codigo)
    {
        return Busqueda.buscarLineal(catalogo, codigo);
    }

    public static Producto buscarProductoPorCodigoBinaria(String codigo)
    {
        return Busqueda.buscarBinaria(catalogo, codigo);
    }

    public static Producto buscarProductoPorHash(String codigo)
    {
        return EstructuraHash.buscarProducto(codigo);
    }
}
