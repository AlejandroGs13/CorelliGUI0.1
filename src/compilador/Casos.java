/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author AlejandroGs13
 */
public class Casos {

    private boolean control;
    private boolean control2;
    private int nvar;
    private boolean startCode;//variable para ver si es el inicio del codigo.
    private Metodos metodos = new Metodos();//Clase de los metodos principales del programa.
    private boolean firtsToken;//variable para comprobar si es el primer token usado.
    private boolean EndCode;//variable para comprobar si es el primer token usado.
    private Expresiones expresiones = new Expresiones();

    /*
    Constructor de la clase:
    El constructor inicia con la variable firsToken verdadedo y la cariable de codigo inicializado en falso.
     */
    public Casos() {
        this.startCode = false;
        this.firtsToken = true;
        this.control = false;
        this.control2 = false;
        this.nvar = 0;
        this.EndCode = false;
    }

    /*
    Metodo principal de la clase casos:
    En este metodo recibe un token de la clase main par evaluarlo
     */
    public void Casos(String token, StringTokenizer tokenizer, String linea, int cLine, boolean segunda) {
        boolean bien = true;
        if (!token.equals("letras") && !token.equals("enviar")) {
            StringTokenizer tokenizer1 = new StringTokenizer(linea);
            while (tokenizer1.hasMoreTokens()) {
                bien = NoLenguaje(tokenizer1.nextToken(), cLine);
            }
        }
        StringTokenizer tokenletras = new StringTokenizer(linea);
        if (tokenletras.hasMoreElements()) {
            tokenletras.nextToken();
            if (tokenletras.hasMoreElements()) {
                String imp = tokenletras.nextToken();
                bien = NoLenguaje(imp, cLine);
            }
        }
        if (bien) {
            switch (token) {
                case "--->":
                    if (startCode) {
                        //metodos.AddError("Solo se peude indicar el inicio del codigo una vez");
                        metodos.AddError("Error Semantico : Solo se peude delcarar una vez el inicio de codigo ' ---> ': Linea:" + cLine);
                    } else {
                        startCode = true;
                    }
                    break;
                case "<---":
                    if (EndCode) {
                        //metodos.AddError("Solo se peude indicar el fin del codigo una vez");
                        metodos.AddError("Error Semantico : Solo se peude delcarar una vez el fin de codigo ' <--- ': Linea:" + cLine);
                    } else {
                        EndCode = true;
                    }
                    break;
                case "obtener":
                    if (!startCode || EndCode) {
                        //metodos.AddError("Codigo fuera del programa en la linea:" + cLine);
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {
                        metodos.validateObtener(linea, cLine);
                    }
                    break;
                case "enviar":
                    if (!startCode || EndCode) {
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {
                        metodos.validateEnviar(linea, cLine);
                    }

                    if (segunda) {
                        metodos.addImprimir(linea, nvar);
                        nvar++;
                    }
                    break;
                case "verdad":
                    if (!startCode || EndCode) {
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {
                        metodos.validateVerdad(linea, cLine);
                    }
                    break;
                case "durante":
                    if (!startCode || EndCode) {
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {
                        metodos.validateDurante(linea, cLine);
                    }
                    break;
                case "xveces":
                    if (!startCode || EndCode) {
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {
                        metodos.validateXveces(linea, cLine);
                    }
                    break;
                case "completo":
                    if (!startCode || EndCode) {
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {
                        metodos.validateCompleto(linea, cLine, token, expresiones.getCompleto());
                    }
                    break;
                case "juez":
                    if (!startCode || EndCode) {
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {

                        metodos.validateCompleto(linea, cLine, token, expresiones.getJuez());
                    }
                    break;
                case "letras":
                    if (!startCode || EndCode) {
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {
                        metodos.validateString(token, linea, cLine);
                    }
                    break;
                case "<<":
                    if (!startCode || EndCode) {
                        metodos.AddError("Error Semantico : Codigo fuera del programa : Linea:" + cLine);
                    } else {
                        StringTokenizer tokenizer1 = new StringTokenizer(linea);
                        if (tokenizer1.countTokens() > 1) {
                            metodos.AddError("Error Sintactico  : Linea:" + cLine);
                        } else {
                            metodos.parentesis(cLine, 1);
                        }
                    }
                    break;

                default:
                    if (!metodos.vDeclarada(token) && !token.matches("[0-9]+")) {
                        metodos.AddError("Error Semantico: Variable no declarada '" + token + "' : Linea:" + cLine);
                    } else {
                        if (token.matches("[0-9]+")) {
                            metodos.AddError("Error Sintactico : ' " + token + " ' : Linea:" + cLine);
                        } else {
                            metodos.validateOp(token, linea, cLine);
                        }

                    }
            }
        }
        controlFIn();
        controlInicio();
        firtsToken = false;
    }

    public void imprimirerrores() {

        metodos.impriParentesis();
        metodos.showErros();
    }

    private void controlInicio() {
        if (!startCode && !control) {
            //metodos.AddError("No indico el incio del codigo");
            metodos.AddError("Error Semantico : No Indico Inicio del codigo ' ---> ' ");
            control = true;
        }
        if (startCode) {
            metodos.delErro("Error Semantico : No Indico Inicio del codigo ' ---> ' ");
        }
    }

    private void controlFIn() {
        if (!EndCode && !control2) {
            metodos.AddError("Error Semantico : No Indico Fin del codigo ' <--- ' ");
            control2 = true;
        }
        if (EndCode) {
            metodos.delErro("Error Semantico : No Indico Fin del codigo ' <--- ' ");
        }
    }

    public boolean NoLenguaje(String palabra, int contadorr) {
        int contador = 0;
        String palabra2 = palabra;
        boolean valido = true;
        String ex = expresiones.getLenguaje();
        Pattern p = Pattern.compile(ex);
        Matcher m = p.matcher(palabra);
        StringBuffer sb = new StringBuffer();
        boolean resultado = m.find();
        boolean ilegal = false;
        while (resultado) {
            ilegal = true;
            m.appendReplacement(sb, "*");
            contador = m.end();
            resultado = m.find();
        }
        m.appendTail(sb);
        palabra = sb.toString();
        if (ilegal) {
            //metodos.AddError("Error caracter fuera del lengaje:" + palabra + " liena:" + contadorr);
            metodos.AddError("Error Lexico: Caracter fuera del lenguaje '" + palabra + "':Linea:" + contador);
            valido = false;
        }
        return valido;
    }

    public void imprimir() {
        metodos.impVaribles();
    }

    public boolean basio() {
        return metodos.Basio();
    }
    
    public ArrayList<String> Code(){
        return metodos.code();
    }
    
    public ArrayList<String> Data(){
        return metodos.data();
    }
    
    public void AddCode(String linea){
    metodos.addCode(linea);
    }
    
    public void AddData(String linea){
    metodos.addData(linea);
    }
    
    public String enviarErrores(){
        return metodos.enviarErros();
    }
    public void Paser(){
    metodos.Paser();
    }
    
    
   
    
    
    public void setter(){
        this.startCode = false;
        this.firtsToken = true;
        this.control = false;
        this.control2 = false;
        this.nvar = 0;
        this.EndCode = false;
        metodos.vaciarErrores();
        metodos.vaciarCode();
        metodos.vaciarData();
    }
}
