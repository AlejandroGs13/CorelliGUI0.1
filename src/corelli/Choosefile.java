/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corelli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author alejandrogs
 */
public class Choosefile {

    public String open(JTextArea jTextArea) {
        String path = "";
        //empezamos implementando la clase JFileChooser para abrir archivos
        JFileChooser JFC = new JFileChooser();
        //filtro que muestra solo los archivos con extension *.edu
        JFC.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.CAIN", "cain", "CAIN"));//filtro para ver solo archivos .edu
        //se comprueba si se ha dado al boton aceptar
        int abrir = JFC.showDialog(null, "Abrir");
        if (abrir == JFileChooser.APPROVE_OPTION) {
            FileReader FR = null;
            BufferedReader BR = null;

            try {
                //abro el fichero y creo un BufferedReader para hacer
                //una lectura comoda (tener el metodo readLine();)
                File archivo = JFC.getSelectedFile();//abre un archivo .lengf

                //evitar abrir archivo con otra extension que no sea *.LFP
                String PATH = JFC.getSelectedFile().getAbsolutePath();
                if (PATH.endsWith(".cain") || PATH.endsWith(".CAIN")) {

                    FR = new FileReader(archivo);
                    BR = new BufferedReader(FR);

                    //leyendo el archivo
                    String linea;//variable para leer linea por linea el archivo de entrada
                    if (path.compareTo(archivo.getAbsolutePath()) == 0) {
                        JOptionPane.showMessageDialog(null, "Archivo Abierto", "Oops! Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        path = archivo.getAbsolutePath();
                        jTextArea.setText(null);//limpiamos el textArea antes de sobreescribir
                        try {
                            while ((linea = BR.readLine()) != null) { //cuando termina el texto del archivo?
                                jTextArea.append(linea + "\n");//utilizamos append para escribir en el textArea
                            }
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Archivo no soportado", "Oops! Error", JOptionPane.ERROR_MESSAGE);

                }

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                //Logger.getLogger(fileChooser.class.getName()).log(Level.SEVERE, null, ex);
                //cerramos el fichero, para asegurar que se cierra tanto
                // si todo va bien si salta una excepcion
            } finally {
                try {
                    if (null != FR) {
                        FR.close();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    //Logger.getLogger(fileChooser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
        return "None";
        }
        return path;
    }

    public String save(JTextArea jTextArea) {
        String texto = jTextArea.getText();//variable para comparacion
        String PATH = "";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.CAIN", "cain", "CAIN"));//filtro para ver solo archivos .edu
        int seleccion = fileChooser.showSaveDialog(null);
        boolean bSalir;
        try {
            bSalir = true;
            if (seleccion == JFileChooser.APPROVE_OPTION) {//comprueba si ha presionado el boton de aceptar
                File fichero = fileChooser.getSelectedFile();

                PATH = fichero.getAbsolutePath();//obtenemos el path del archivo a guarda
                File tem = new File(PATH + ".cain");
                if (!tem.exists()) {

                    fichero = new File(PATH + ".cain");
                    fichero.createNewFile();
                    JOptionPane.showMessageDialog(null, "Archivo Creado", "Corelli", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    bSalir = JOptionPane.showConfirmDialog(null, "El archivo ya existe dessea sobre escribir", "Corelli", JOptionPane.OK_OPTION) != JOptionPane.YES_OPTION;

                    if (bSalir == false) {

                        fichero = new File(PATH + ".cain");
                        fichero.delete();

                        fichero.createNewFile();
                        JOptionPane.showMessageDialog(null, "Archivo Remplazado", "Corelli", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        return "None";
                    }
                }

            }else {
                        return "None";
                    }
        } catch (Exception e) {//por alguna excepcion salta un mensaje de error
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo!", "Oops! Error", JOptionPane.ERROR_MESSAGE);
        }
        return PATH;
    }

}
