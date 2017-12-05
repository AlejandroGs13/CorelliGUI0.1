/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.StringTokenizer;

/**
 *
 * @author AlejandroGs13
 */
public class Corelli {

    /*
    Actividad principal del compilador Main
     */
      Casos casos = new Casos();
      String errores = "";
    public void  compilador(String url) {
        Abrir abrir = new Abrir();
        //<editor-fold desc="Declaracion de variables "Globales" necesarias para el programa">
        Archivos archivos = new Archivos();//Declaramos la clase archivo 
        String Codigo = "";
        Codigo = archivos.ReadFIle(url);//Creamos un String y con el metodo REadFile de la clase archivo pasamos todo el codigo del programa
        String Lineas[] = Codigo.split("\n");//Partimos todo el codigo en lineas
      
        int cLinea = 1;//Variable utilizada para contar las lienas 
        //</editor-fold>
        //<editor-fold desc="Recorrido del Codigo">    
        for (String linea : Lineas) {
            StringTokenizer tokenizer = new StringTokenizer(linea);
            //<editor-fold desc="Recorremos los tokens de la linea">
            if (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();//Pasamos el token a un String 
                casos.Casos(token, tokenizer, linea, cLinea, false);
            }
            cLinea++;
            //</editor-fold>
        }
        casos.imprimirerrores();
        System.out.println(url);
        errores = casos.enviarErrores();
        
        //casos.imprimir();
        //</editor-fold>
        if (casos.basio()) {
            archivos.MakeAsm(url);
            casos.AddCode(".MODEL SMALL");
            casos.AddCode(".CODE");
            casos.AddCode("Empieza:");
            casos.AddCode("mov Ax, @Data");
            casos.AddCode("mov Ds, Ax");
            cLinea = 1;//Variable utilizada para contar las lienas 
            for (String Linea : Lineas) {
                StringTokenizer tokenizer = new StringTokenizer(Linea);
                if (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    casos.Casos(token, tokenizer, Linea, cLinea, true);
                }
            cLinea++;
            }
            casos.AddCode("mov AX, 4C00h");
            casos.AddCode("int 21h");
            casos.AddCode(".DATA");
            casos.Paser();
            casos.AddCode(".STACK");
            casos.AddCode("END Empieza");

            if (url.endsWith(".cain")) {
            String []temp = url.split("\\.");
            url = temp[0]+"assembly.asm";
            }
            for (String string : casos.Code()) {
                archivos.writerFile(string,url,true);
            }
             abrir.Abir(url);
        }
       
        casos.setter();
    }
    
    public String EnviarErrores(){
       return  errores;
    }

}
