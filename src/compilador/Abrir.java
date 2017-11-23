/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author AlejandroGS13
 */
public class Abrir {
    
    public void Abir(String url){
      String s = null;
                    //                                        + ";

                try {

                        // Determinar en qué SO estamos
                        String so = System.getProperty("os.name");

                        String comando;

                        // Comando para Linux
                        if (so.equals("Linux"))
                                comando = "nasm -f elf /home/corelli/Documentos/Assembly/Practica1/HolaMundo.asm ";
                        // Comando para Windows
                        else
                                comando = "cmd /c start "+url;

                        // Ejcutamos el comando
                        Process p = Runtime.getRuntime().exec(comando);

                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                                        p.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new InputStreamReader(
                                        p.getErrorStream()));

                        // Leemos la salida del comando
                        System.out.println("Salida:\n");
                        while ((s = stdInput.readLine()) != null) {
                                System.out.println(s);
                        }

                        // Leemos los errores si los hubiera
                        
                        while ((s = stdError.readLine()) != null) {
                                System.out.println(s);
                        }

                } catch (IOException e) {
                        System.out.println("Excepción: ");
                        e.printStackTrace();
                        System.exit(-1);
                }
                
               
        }
    }
