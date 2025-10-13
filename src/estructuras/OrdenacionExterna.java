/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import negocio.Producto;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Clase que contiene todos los algoritmos de ordenación externa (fuera de
 * memoria).
 */
public class OrdenacionExterna {

    private static final int MEMORIA_MAXIMA = 100;

    public static void ordenarPorFusionNatural(ArrayList<Producto> catalogo) {
        int n = catalogo.size();
        ArrayList<Producto> tempArray = new ArrayList<>(Collections.nCopies(n, null));

        boolean ordenado = false;
        while (!ordenado) {
            ordenado = true;
            int i = 0;
            while (i < n) {
                int j = i;
                while (j < n - 1 && catalogo.get(j).getCodigo().compareTo(catalogo.get(j + 1).getCodigo()) <= 0) {
                    j++;
                }
                if (j == n - 1) {
                    break;
                }
                int k = j + 1;
                while (k < n - 1 && catalogo.get(k).getCodigo().compareTo(catalogo.get(k + 1).getCodigo()) <= 0) {
                    k++;
                }
                ordenado = false;

                int p1 = i, p2 = j + 1, p3 = i;
                while (p1 <= j && p2 <= k) {
                    if (catalogo.get(p1).getCodigo().compareTo(catalogo.get(p2).getCodigo()) <= 0) {
                        tempArray.set(p3++, catalogo.get(p1++));
                    } else {
                        tempArray.set(p3++, catalogo.get(p2++));
                    }
                }
                while (p1 <= j) {
                    tempArray.set(p3++, catalogo.get(p1++));
                }
                while (p2 <= k) {
                    tempArray.set(p3++, catalogo.get(p2++));
                }

                for (int l = i; l <= k; l++) {
                    catalogo.set(l, tempArray.get(l));
                }
                i = k + 1;
            }
        }
    }

    public static void ordenar(File inputFile, File outputFile) throws IOException {

        File f1 = new File("temp_ext_1.txt");
        File f2 = new File("temp_ext_2.txt");

        //Resetear el archivo de salida
        if (outputFile.exists()) {
            outputFile.delete();
        }

        //Distribución y ordenación inicial de series
        distribuir(inputFile, f1, f2);

        //Fusión iterativa hasta que quede un solo archivo ordenado
        boolean ordenado = false;
        File source1 = f1;
        File source2 = f2;
        File destination = outputFile;

        while (!ordenado) {
            ordenado = fusionar(source1, source2, destination);

            if (!ordenado) {
                //El archivo de salida se convierte en la nueva entrada.
                source1.delete();
                source2.delete();
                distribuir(destination, source1, source2);

                // Borrar el archivo de destino(será reescrito)
                if (destination.exists() && !destination.equals(outputFile)) {
                    destination.delete();
                }
            }
        }
        f1.delete();
        f2.delete();
    }   
         
    private static void distribuir(File entrada, File salida1, File salida2) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(entrada)); 
             BufferedWriter writer1 = new BufferedWriter(new FileWriter(salida1)); 
             BufferedWriter writer2 = new BufferedWriter(new FileWriter(salida2))) {

            String linea;
            boolean usarWriter1 = true;

            while ((linea = reader.readLine()) != null) {
                List<String> serie = new ArrayList<>();
                serie.add(linea);

                for (int i = 0; i < MEMORIA_MAXIMA - 1 && (linea = reader.readLine()) != null; i++) {
                    if (!linea.trim().isEmpty()) {
                        serie.add(linea);
                    }
                }

                Collections.sort(serie, (s1, s2) -> 
                    Producto.getCodigoFromLine(s1).compareTo(Producto.getCodigoFromLine(s2)));
                

                BufferedWriter writer = usarWriter1 ? writer1 : writer2;
                for (String s : serie) {
                    writer.write(s);
                    writer.newLine();
                }
                usarWriter1 = !usarWriter1;
            }
        }
    }

    private static boolean fusionar(File entrada1, File entrada2, File salida) throws IOException {
        long linesInOutput = 0;
        int seriesFusionadas = 0;

        try (BufferedReader reader1 = new BufferedReader(new FileReader(entrada1)); 
             BufferedReader reader2 = new BufferedReader(new FileReader(entrada2)); 
             BufferedWriter writer = new BufferedWriter(new FileWriter(salida))) {

            String linea1 = reader1.readLine();
            String linea2 = reader2.readLine();

            if (linea1 == null && linea2 != null) {
                copyRemaining(reader2, writer, linea2);
                return true;
            } else if (linea2 == null && linea1 != null) {
                copyRemaining(reader1, writer, linea1);
                return true;
            }

            while (linea1 != null || linea2 != null) {
                String codigo1 = (linea1 != null) ? Producto.getCodigoFromLine(linea1) : null;
                String codigo2 = (linea2 != null) ? Producto.getCodigoFromLine(linea2) : null;
                
                while (linea1 != null && linea2 != null) {
                    if (codigo1.compareTo(codigo2) <= 0) {
                        writer.write(linea1);
                        writer.newLine();
                        linesInOutput++;
                        linea1 = reader1.readLine();
                        if (linea1 != null) {
                            if (Producto.getCodigoFromLine(linea1).compareTo(codigo1) < 0) break; 
                            codigo1 = Producto.getCodigoFromLine(linea1);
                        }
                    } else {
                        writer.write(linea2);
                        writer.newLine();
                        linesInOutput++;
                        linea2 = reader2.readLine();
                        if (linea2 != null) {
                            if (Producto.getCodigoFromLine(linea2).compareTo(codigo2) < 0) break;
                            codigo2 = Producto.getCodigoFromLine(linea2);
                        }
                    }
                }

                linea1 = copyRemainingSeries(reader1, writer, linea1, codigo1);
                linea2 = copyRemainingSeries(reader2, writer, linea2, codigo2);

                seriesFusionadas++;
            }
        }
        return seriesFusionadas <= 1;
    }

    private static void copyRemaining(BufferedReader reader, BufferedWriter writer, String currentLine) throws IOException {
        writer.write(currentLine);
        writer.newLine();
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.newLine();
        }
    }
    
    private static String copyRemainingSeries(BufferedReader reader, BufferedWriter writer, String currentLine, String lastCode) throws IOException {
        while (currentLine != null) {
            String nextCode = Producto.getCodigoFromLine(currentLine);
            if (nextCode.compareTo(lastCode) < 0) {
                return currentLine; 
            }
            writer.write(currentLine);
            writer.newLine();
            lastCode = nextCode;
            currentLine = reader.readLine();
        }
        return null;
    }
}
