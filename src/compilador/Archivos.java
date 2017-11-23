/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author David Alejandro Clase para leer los archivos: Archivo: Contiene el
 * codigo fuente Reservado: Contiene las palabras reservadas para el sistema
 */
public class Archivos {

    public String ReadFIle(String url) {
        String conten = "";
        if (!url.endsWith(".cain")) {
            url = url + ".cain";
        }
        try {
            FileReader lector = new FileReader(url);
            BufferedReader contenido = new BufferedReader(lector);

            String linea;
            while ((linea = contenido.readLine()) != null) {
                conten = conten + linea + "\n";
            }

        } catch (IOException exception) {
            System.out.println(exception);
        }
        return conten;
    }

    public String Read() {
        String conten = "";
        try {

            FileReader lector = new FileReader("src/compilador/Reservado");
            BufferedReader contenido = new BufferedReader(lector);

            String linea;
            while ((linea = contenido.readLine()) != null) {
                conten = conten + linea + "\n";
            }

        } catch (IOException exception) {
            System.out.println(exception);
        }
        return conten;
    }

    public String ReadError() {
        String conten = "";
        try {

            FileReader lector = new FileReader("src/compilador/Errores");
            BufferedReader contenido = new BufferedReader(lector);

            String linea;
            while ((linea = contenido.readLine()) != null) {
                conten = conten + linea + "\n";
            }

        } catch (IOException exception) {
            System.out.println(exception);
        }
        return conten;
    }

    public void writerFile(String ss, String url,boolean editable) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(url, editable);
            pw = new PrintWriter(fichero);
            pw.println(ss);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                System.out.println("Al escribir en el archivo");
            }
        }
    }

 

    public void MakeAsm(String url) {
        if (url.endsWith(".cain")) {
            String []temp = url.split("\\.");
            url = temp[0]+"assembly.asm";
        }
        File fichero;
        try {
            System.out.println("etraa:"+url);
            fichero = new File(url);
            if (!fichero.exists()) {
                fichero = new File(url);
                fichero.createNewFile();
            } else {
                fichero = new File(url);
                fichero.delete();
                fichero.createNewFile();
            }
        } catch (Exception ex) {
            System.out.println("Error al crear archivo");
        }
    }
}
