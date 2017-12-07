/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author AlejandroGs13
 */
public class Metodos {

    private ArrayList<String> asmVariables = new ArrayList<>();
    private ArrayList<String> asmControl = new ArrayList<>();
    private ArrayList<String> Code = new ArrayList<>();
    private ArrayList<String> Data = new ArrayList<>();
    private ArrayList<String> Errores = new ArrayList<>();
    private ArrayList<String> Parentesis = new ArrayList<>();
    private ArrayList<String> vName = new ArrayList<>();
    private ArrayList<String> vTipo = new ArrayList<>();
    private ArrayList<String> vValor = new ArrayList<>();
    private Expresiones expresiones = new Expresiones();

    ArrayList<String> Reservadas() {
        ArrayList<String> Reser = new ArrayList<>();
        Archivos archivos = new Archivos();
        String Reserva = archivos.Read();
        String[] R = Reserva.split("\n");
        Reser.addAll(Arrays.asList(R));
        return Reser;
    }

    boolean vDeclarada(String token) {
        return vName.contains(token);
    }

    public void validateCompleto(String Linea, int conLine, String token, String exp) {
        if (estructura(exp, Linea)) {
            StringTokenizer tokenizer = new StringTokenizer(Linea);
            if (tokenizer.countTokens() == 4) {
                tokenizer.nextToken();
                String name = tokenizer.nextToken();
                if (vName.contains(name)) {
                    //AddError("Variable " + name + " ya declarada, Error liena:" + conLine);
                    AddError("Error Semantico: Variable ya declarada '" + name + "' : Linea:" + conLine);
                } else if (Reservadas().contains(name)) {
                    //AddError("Variable " + name + " Es reservada para el sistema, Error liena:" + conLine);
                    AddError("Error Semantico: Variable reservada para el sistema '" + name + "' : Linea:" + conLine);
                } else {
                    tokenizer.nextToken();
                    String valor = tokenizer.nextToken();
                    addVar(name, token, valor);
                }
            } else {
                tokenizer.nextToken();
                String name = tokenizer.nextToken();
                if (vName.contains(name)) {
                    AddError("Error Semantico: Variable ya declarada '" + name + "' : Linea:" + conLine);
                    //AddError("Variable " + name + " ya declarada, Error liena:" + conLine);
                } else if (Reservadas().contains(name)) {
                    AddError("Error Semantico: Variable reservada para el sistema '" + name + "' : Linea:" + conLine);
                    //AddError("Variable " + name + " Es reservada para el sistema, Error liena:" + conLine);
                } else {
                    addVar(name, token, "0");
                }
            }
        } else {
            //AddError("Error en la declaracion de la variable " + token + " Linea:" + conLine);
            AddError("Error Sintactico: Declaracion de la variable : Linea:" + conLine);
        }
    }

    public void validateString(String token, String linea, int cLinea) {
        if (estructura(expresiones.getLetras(), linea)) {
            StringTokenizer tokenizer = new StringTokenizer(linea);
            if (tokenizer.countTokens() == 2) {
                tokenizer.nextToken();
                String name = tokenizer.nextToken();
                if (vName.contains(name)) {
                    AddError("Error Semantico: Variable ya declarada '" + name + "' : Linea:" + cLinea);
                } else if (Reservadas().contains(name)) {
                    AddError("Error Semantico: Variable reservada para el sistema '" + name + "' : Linea:" + cLinea);

                } else {
                    addVar(name, "letras", "null");
                }
            } else {
                tokenizer.nextToken();
                String name = tokenizer.nextToken();
                if (vName.contains(name)) {
                    AddError("Error Semantico: Variable ya declarada '" + name + "' : Linea:" + cLinea);
                } else if (Reservadas().contains(name)) {
                    AddError("Error Semantico: Variable reservada para el sistema '" + name + "' : Linea:" + cLinea);

                } else {
                    String[] contenido = linea.split("\"");
                    String conten = "";
                    for (int i = 1; i < contenido.length; i = i + 2) {
                        int con = contenido[i].length();
                        conten = conten + contenido[i];
                    }
                    addVar(name, "letras", conten);
                }
            }
        } else {
            AddError("Error Sintactico: Declaracion de la variable : Linea:" + cLinea);
        }
    }

    public void addVar(String vName, String vTipo, String vValor) {
        this.vName.add(vName);
        this.vTipo.add(vTipo);
        this.vValor.add(vValor);
    }

    public void AddError(String e) {
        Errores.add(e);
    }

    public void showErros() {
        Errores.forEach((Errore) -> {
            System.out.println(Errore);
        });
    }

    public String enviarErros() {
        String error = "";
        for (String Errore : Errores) {
            error = error + Errore + "\n";
        }
        return error;
    }

    public boolean comReser(String Token) {
        return Reservadas().contains(Token);
    }

    public void delErro(String error) {
        Errores.remove(error);
    }

    public boolean estructura(String exp, String Linea) {
        Pattern patron = Pattern.compile(exp);
        Matcher mcher = patron.matcher(Linea);
        return mcher.matches();
    }

    public void validateEnviar(String Linea, int cLinea) {
        if (estructura(expresiones.getEnviar(), Linea)) {
            if (-1 == Linea.indexOf("\"")) {
                StringTokenizer tokenizer = new StringTokenizer(Linea);
                if (tokenizer.countTokens() > 3) {
                    if (tokenizer.countTokens() == 4) {
                        tokenizer.nextToken();
                        tokenizer.nextToken();
                        String elemento = tokenizer.nextToken();
                        if (!elemento.matches("[0-9]+")) {
                            if (!vName.contains(elemento)) {
                                AddError("Error Semantico: Variable no declarada '" + elemento + "' : Linea:" + cLinea);

                            }
                        }
                    }
                    if (tokenizer.countTokens() > 4) {
                        String[] variables = Linea.split("\\(");
                        String[] variables2 = variables[1].split("\\)");
                        String[] variable = variables2[0].split("\\+");
                        String var = "";
                        for (String string : variable) {
                            var = var + string;
                        }
                        ArrayList<String> vari = new ArrayList<>();
                        StringTokenizer tokenizer1 = new StringTokenizer(var);
                        while (tokenizer1.hasMoreTokens()) {
                            String name = tokenizer1.nextToken();
                            vari.add(name);
                        }
                        boolean error = false;
                        for (String string : vari) {
                            if (!vName.contains(string)) {
                                AddError("Error Semantico: Variable no declarada '" + string + "' : Linea:" + cLinea);
                                error = true;
                            }
                        }
                        if (!error) {
                            for (int i = 1; i < vari.size(); i++) {
                                if (!vTipo.get(vName.indexOf(vari.get(i))).equals(vTipo.get(vName.indexOf(vari.get(i - 1))))) {
                                    AddError("Error Semantico: Error de tipos de variable: Linea:" + cLinea);
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                String[] msjaux = Linea.split("\\(");
                String[] msjaux2 = msjaux[1].split("\\)");
                String[] msj = msjaux2[0].split("\\+");

                for (String string : msj) {
                    if (!string.contains("\"")) {
                        StringTokenizer st = new StringTokenizer(string);
                        String token = st.nextToken();
                        if (!vName.contains(token)) {
                            AddError("Error Semantico: Variable no declarada '" + string + "' : Linea:" + cLinea);
                        }
                    }
                }
            }
        } else {
            AddError("Error Sintactico: Enviar : Linea:" + cLinea);
        }
    }

    /*
    public void validateEnviar(String Linea, int cLinea) {
        if (estructura(expresiones.getEnviar(), Linea)) {
            
        }else {
            AddError("Error Sintactico: Enviar : Linea:" + cLinea);
        }*/
    public void impVaribles() {
        for (int i = 0; i < vName.size(); i++) {
            System.out.println(vTipo.get(i) + " " + vName.get(i) + " = " + vValor.get(i));
        }
    }

    public void validateOp(String token, String Linea, int cLinea) {
        if (vTipo.get(vName.indexOf(token)).equals("completo")) {
            if (estructura(expresiones.getOpCompleto(), Linea)) {
                String[] separa = Linea.split("\\=");
                ArrayList<String> vari = new ArrayList<>();
                StringTokenizer tokenizer1 = new StringTokenizer(separa[1]);
                boolean error = false;
                while (tokenizer1.hasMoreTokens()) {
                    String var = tokenizer1.nextToken();
                    if (!var.matches("[\\*\\/\\%\\+\\-]")) {
                        vari.add(var);
                    }
                }
                for (String string : vari) {
                    if (!string.matches("[0-9]+")) {
                        if (!vName.contains(string)) {
                            AddError("Error Semantico: Variable no declarada '" + string + "' : Linea:" + cLinea);
                            error = true;
                        }
                    }
                }
                if (!error) {
                    vari.stream().filter((string) -> (!string.matches("[0-9]+"))).filter((string) -> (!vTipo.get(vName.indexOf(string)).equals("completo"))).forEachOrdered((string) -> {
                        AddError("Error Semantico: " + string + "- no es del tipo completo: Linea: " + cLinea);
                    });
                }
            } else {
                AddError("Error Sintactico: Operacion completo : Linea:" + cLinea);
            }
        }
        if (vTipo.get(vName.indexOf(token)).equals("juez")) {
            if (estructura(expresiones.getOpJuez(), Linea)) {
                StringTokenizer tokenizer = new StringTokenizer(Linea);
                if (tokenizer.countTokens() == 3) {
                    tokenizer.nextToken();
                    tokenizer.nextToken();
                    String tipo = tokenizer.nextToken();
                    if (!tipo.equals("false") && !tipo.equals("true")) {
                        if (vName.contains(tipo)) {
                            if (!vTipo.get(vName.indexOf(tipo)).equals("juez")) {
                                AddError("Error Semantico: " + tipo + "- no es del tipo completo: Linea: " + cLinea);
                            }
                        } else {
                            AddError("Error Semantico: Variable no declarada '" + tipo + "' : Linea:" + cLinea);
                        }
                    }
                }
            } else {
                AddError("Error Sintactico: Operacion Juez : Linea:" + cLinea);
            }
        }
        if (vTipo.get(vName.indexOf(token)).equals("letras")) {
            if (estructura(expresiones.getOpLetras(), Linea)) {
                String[] separa = Linea.split("\\=");
                String[] separa2 = separa[1].split("\\+");
                for (String string : separa2) {
                    Pattern pattern = Pattern.compile("[\"]");
                    Matcher matcher = pattern.matcher(string);
                    boolean resultado = matcher.find();
                    if (!resultado) {
                        StringTokenizer tokenizer = new StringTokenizer(string);
                        if (tokenizer.hasMoreTokens()) {
                            String variable = tokenizer.nextToken();
                            if (!vName.contains(variable)) {
                                AddError("Error Semantico: Variable no declarada '" + variable + "' : Linea:" + cLinea);
                            } else {
                                if (!vTipo.get(vName.indexOf(variable)).equals("letras")) {
                                    AddError("Error Semantico: " + variable + "- no es del tipo letas: Linea: " + cLinea);
                                }
                            }
                        }
                    }
                }
            } else {
                AddError("Error Sintactico: Operacion letras : Linea:" + cLinea);
            }
        }
    }

    public void validateVerdad(String liena, int cLinea) {
        if (estructura(expresiones.getVerdad(), liena)) {
            boolean errortotal = false;
            StringTokenizer tokenizer = new StringTokenizer(liena);
            if (tokenizer.countTokens() > 5) {
                tokenizer.nextToken();
                tokenizer.nextToken();
                String variable1 = tokenizer.nextToken();
                tokenizer.nextToken();
                boolean error = false;
                String variable2 = tokenizer.nextToken();
                if (!vName.contains(variable1) && !variable1.matches("[0-9]+")) {
                    AddError("Error Semantico: Variable no declarada '" + variable1 + "' : Linea:" + cLinea);
                    errortotal = true;
                    error = true;
                }
                if (!vName.contains(variable2) && !variable2.matches("[0-9]+")) {
                    AddError("Error Semantico: Variable no declarada '" + variable2 + "' : Linea:" + cLinea);
                    error = true;
                    errortotal = true;
                }
                if (!error) {
                    if (!variable1.matches("[0-9]+")) {
                        if (!vTipo.get(vName.indexOf(variable1)).equals("completo") && !variable1.matches("[0-9]+")) {
                            AddError("Error Semantico: " + variable1 + "- no es del tipo completo: Linea: " + cLinea);
                            errortotal = true;
                        }
                    }
                    if (!variable2.matches("[0-9]+")) {
                        if (!vTipo.get(vName.indexOf(variable2)).equals("completo") && !variable2.matches("[0-9]+")) {
                            AddError("Error Semantico: " + variable2 + "- no es del tipo completo: Linea: " + cLinea);
                            errortotal = true;
                        }
                    }
                }
            } else {
                tokenizer.nextToken();
                tokenizer.nextToken();
                String variable1 = tokenizer.nextToken();
                if (!vTipo.get(vName.indexOf(variable1)).equals("completo")) {
                    AddError("Error Semantico: " + variable1 + "- no es del tipo completo: Linea: " + cLinea);
                    errortotal = true;
                }
            }
            if (!errortotal) {
                parentesis(cLinea, 0);
            }
        } else {
            AddError("Error Sintactico: Verdad : Linea:" + cLinea);
        }
    }

    public void validateXveces(String linea, int cLinea) {
        if (estructura(expresiones.getXveces(), linea)) {
            boolean errotTotal = false;
            StringTokenizer tokenizer = new StringTokenizer(linea);
            tokenizer.nextToken();
            tokenizer.nextToken();
            String var1 = tokenizer.nextToken();
            tokenizer.nextToken();
            String var2 = tokenizer.nextToken();
            tokenizer.nextToken();
            String var3 = tokenizer.nextToken();
            tokenizer.nextToken();
            String var4 = tokenizer.nextToken();
            tokenizer.nextToken();
            String var5 = tokenizer.nextToken();
            if (validateVariable("completo", cLinea, var1) && validateVariable("completo", cLinea, var2)
                    && validateVariable("completo", cLinea, var3) && validateVariable("completo", cLinea, var4)
                    && validateVariable("completo", cLinea, var5)) {
                parentesis(cLinea, 0);
            }
        } else {
            AddError("Error Sintactico: Operacion xveces : Linea:" + cLinea);
        }
    }

    private boolean validateVariable(String tipo, int cLinea, String var) {
        if (!var.matches("[0-9]+")) {
            if (validatePertenece(var, cLinea)) {
                if (!vTipo.get(vName.indexOf(var)).equals(tipo)) {
                    AddError("Variable -" + var + "- no es del tipo " + tipo + ": linea - " + cLinea);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validatePertenece(String token, int cLinea) {
        if (!vName.contains(token)) {
            AddError("Error Semantico: Variable no declarada '" + token + "' : Linea:" + cLinea);
            return false;
        }
        return true;
    }

    public void parentesis(int cLinea, int opc) {
        if (opc == 0) {
            Parentesis.add(String.valueOf(cLinea));
        }
        if (opc == 1) {
            try {
                Parentesis.remove(Parentesis.size() - 1);
            } catch (Exception ex) {
                AddError("Error Semantico: << sobrante: Linea" + cLinea);
            }
        }
    }

    public void validateDurante(String liena, int cLinea) {
        if (estructura(expresiones.getVerdad(), liena)) {
            boolean errortotal = false;
            StringTokenizer tokenizer = new StringTokenizer(liena);
            if (tokenizer.countTokens() > 5) {
                tokenizer.nextToken();
                tokenizer.nextToken();
                String variable1 = tokenizer.nextToken();
                tokenizer.nextToken();
                boolean error = false;
                String variable2 = tokenizer.nextToken();
                if (!variable1.matches("[0-9]+") || !variable2.matches("[0-9]+")) {
                    if (!vName.contains(variable1) && !variable1.matches("[0-9]+")) {
                        AddError("Error Semantico: Variable no declarada '" + variable1 + "' : Linea:" + cLinea);
                        errortotal = true;
                        error = true;
                    }
                    if (!vName.contains(variable2) && !variable2.matches("[0-9]+")) {
                        AddError("Error Semantico: Variable no declarada '" + variable2 + "' : Linea:" + cLinea);
                        error = true;
                        errortotal = true;
                    }
                    if (!error) {
                        if (!variable1.matches("[0-9]+")) {
                            if (!vTipo.get(vName.indexOf(variable1)).equals("completo") && !variable1.matches("[0-9]+")) {
                                //AddError("Variable:" + variable1 + " no es tipo numerico, linea:" + cLinea);
                                AddError("Error Semantico: " + variable1 + "- no es del tipo completo: Linea: " + cLinea);
                                errortotal = true;
                            }
                        }
                        if (!variable2.matches("[0-9]+")) {
                            if (!vTipo.get(vName.indexOf(variable2)).equals("completo") && !variable2.matches("[0-9]+")) {
                                //AddError("Variable:" + variable2 + " no es tipo numerico, linea:" + cLinea);
                                AddError("Error Semantico: " + variable2 + "- no es del tipo completo: Linea: " + cLinea);
                                errortotal = true;
                            }
                        }
                    }
                } else {
                    AddError("Error semantico: No puede haber dos constantes en la condicion del while: Linea:" + cLinea);
                    errortotal = true;
                }
            } else {
                tokenizer.nextToken();
                tokenizer.nextToken();
                String variable1 = tokenizer.nextToken();
                if (!vTipo.get(vName.indexOf(variable1)).equals("completo")) {
                    AddError("Error Semantico: " + variable1 + "- no es del tipo completo: Linea: " + cLinea);;
                    errortotal = true;
                }
            }
            if (!errortotal) {
                parentesis(cLinea, 0);
            }
        } else {
            AddError("Error Sintactico: While: Linea:" + cLinea);
        }
    }

    public void validateObtener(String linea, int cLinea) {
        if (estructura(expresiones.getObtener(), linea)) {
            StringTokenizer tokenizer = new StringTokenizer(linea);
            tokenizer.nextToken();
            tokenizer.nextToken();
            String variable = tokenizer.nextToken();
            if (!vName.contains(variable)) {
                AddError("Error Semantico: Variable no declarada '" + variable + "' : Linea:" + cLinea);

            }
        } else {
            AddError("Error Sintactico: Obtener: Linea:" + cLinea);
        }
    }

    public void impriParentesis() {

        Parentesis.forEach((Errore) -> {
            AddError("Error Semantico: Falta cierre ' << ': Linea" + Errore);
        });
    }

    public boolean Basio() {
        return Errores.isEmpty();
    }

    public void addData(String linea) {
        Data.add(linea);
    }

    public void addCode(String linea) {
        Code.add(linea);
    }

    ArrayList<String> code() {
        return Code;
    }

    ArrayList<String> data() {
        return Data;
    }

    public void Paser() {
        Data.forEach((string) -> {
            Code.add(string);
        });
    }

    public void addImprimir(String Linea, int nvar, int cLine) {
        boolean entro = false;
        boolean entro2 = false;
        String vv = "";
        String[] msjaux = Linea.split("\\(");
        String[] msjaux2 = msjaux[1].split("\\)");
        String[] msj = msjaux2[0].split("\\+");
        String contenido = "";
        for (String msj1 : msj) {
            StringTokenizer st = new StringTokenizer(msj1);
            String token = st.nextToken();
            if (msj1.contains("\"")) {
                entro = true;
                String[] texto = msj1.split("\"");
                contenido = contenido + texto[1];
            } else if (!asmVariables.contains(token) && token.equals("[0-9]+")) {
                entro = true;
                contenido = contenido + vValor.get(vName.indexOf(token));
            } else {
                vv = token;
                entro2 = true;
            }
        }
        if (entro) {
            Code.add("; Imprimir  ");
            Code.add("mov Dx, Offset var" + nvar);
            Code.add("mov Ah, 9");
            Code.add("Int 21h");
            Data.add("var" + nvar + " DB '" + contenido + "',10,13,'$'");
            if (entro2) {
                String Tipo = vTipo.get(vName.indexOf(vv));
                if (Tipo.equals("completo")) {
                    Code.add("; Imprimir  ");
                    Code.add("mov bl," + vv);
                    Code.add("mov dl,bl");
                    Code.add("add dl,30h");
                    Code.add("mov ah, 02h");
                    Code.add("int 21h");
                }
                if (Tipo.equals("letras")) {
                    Code.add("; Imprimir  ");
                    Code.add("mov Dx, Offset " + vv);
                    Code.add("mov Ah, 9");
                    Code.add("Int 21h");
                }
            }

        }
        StringTokenizer tokenizer = new StringTokenizer(Linea);
        if (tokenizer.countTokens() == 4) {
            if (entro2) {
                Code.add("; Imprime variable ");
                Code.add("mov bl," + vv);
                Code.add("mov dl,bl");
                Code.add("add dl,30h");
                Code.add("mov ah, 02h");
                Code.add("int 21h");
                Code.add(";Salto de linea");
                Code.add("mov Dx, Offset salto");
                Code.add("mov ah,9");
                Code.add("int 21h");
            }
        }
    }

    public void addObtener(String linea, int nvar, int cLine) {
        StringTokenizer tokenizer = new StringTokenizer(linea);
        tokenizer.nextToken();
        tokenizer.nextToken();
        String var = tokenizer.nextToken();
        Code.add("; Leer");
        Code.add("mov ah,01h");
        Code.add("int 21h");
        Code.add("sub al, 30h");
        Code.add("mov " + var + ", al");
        Code.add(";Salto de linea");
        Code.add("mov Dx, Offset salto");
        Code.add("mov ah,9");
        Code.add("int 21h");

    }

    public void addOperaciones(String linea, int nvar, int cLine) {
        StringTokenizer tokenizer = new StringTokenizer(linea);
        String var = tokenizer.nextToken();
        tokenizer.nextToken();
        String var1 = tokenizer.nextToken();
        String op = tokenizer.nextToken();
        String var2 = tokenizer.nextToken();
        if (op.equals("+")) {
            Code.add(";Operacion suma ");
            Code.add("mov bl, 0");
            Code.add("add bl, " + var1);
            Code.add("add bl, " + var2);
            Code.add("mov " + var + ", bl");
        }
        if (op.equals("-")) {
            Code.add("; Operacion resta");
            Code.add("mov bl, 0");
            Code.add("add bl, " + var1);
            Code.add("sub bl, " + var2);
            Code.add("mov " + var + ", bl");
        }
    }

    public void AddAsmVar(String Liena) {
        StringTokenizer tokenizer = new StringTokenizer(Liena);
        tokenizer.nextToken();
        String var = tokenizer.nextToken();
        asmVariables.add(var);
        String valor = vValor.get(vName.indexOf(var));
        String Tipo = vTipo.get(vName.indexOf(var));
        if (Tipo.equals("completo")) {
            Data.add(var + " DB " + valor);
        }
        if (Tipo.equals("letras")) {
            Data.add(var + " DB '" + valor + "' ,10,13,'$'");
        }

    }

    public void AddXveces(String liena, int nvar) {
        StringTokenizer tokenizer = new StringTokenizer(liena);
        String veces = "";
        for (int i = 0; i < 9; i++) {
            veces = tokenizer.nextToken();
        }
        String xv = "For" + nvar;
        Code.add("mov Cx, " + veces);
        Code.add(xv + ":");
        asmControl.add(xv);
    }

    public void AddDurante(String linea, int nvar) {
        StringTokenizer tokenizer = new StringTokenizer(linea);
        tokenizer.nextToken();
        tokenizer.nextToken();
        String var1 = tokenizer.nextToken();
        String op = tokenizer.nextToken();
        String var2 = tokenizer.nextToken();
        String wh = "while" + nvar;
        String control = "FinWhile" + nvar+","+wh+","+var1+","+op+","+var2+","+nvar;
        Code.add(";Inicio del While,");
        Code.add(wh + ":");
        asmControl.add(control);
    }

    public void AddVerdad(String liena, int nvar) {
        StringTokenizer tokenizer = new StringTokenizer(liena);
        tokenizer.nextToken();
        tokenizer.nextToken();
        String var1 = tokenizer.nextToken();
        String op = tokenizer.nextToken();
        String var2 = tokenizer.nextToken();

        if (op.equals("<")) {
            Code.add(";Inicio del if,");
            String control = "If" + nvar + ",Igual" + nvar + ",FinVerdad" + nvar;
            asmControl.add(control);
            Code.add("mov al," + var1);
            Code.add("cmp al," + var2);
            Code.add("jc If" + nvar);
            Code.add("jz Igual" + nvar);
            Code.add("jmp FinVerdad" + nvar);
            Code.add("If" + nvar + ":");
        }
        if (op.equals(">")) {
            Code.add(";Inicio del if,");
            String control = "If" + nvar + ",Igual" + nvar + ",FinVerdad" + nvar;
            asmControl.add(control);
            Code.add("mov al," + var2);
            Code.add("cmp al," + var1);
            Code.add("jc If" + nvar);
            Code.add("jz Igual" + nvar);
            Code.add("jmp FinVerdad" + nvar);
            Code.add("If" + nvar + ":");
        }
        if (op.equals("==")) {
            Code.add(";Inicio del if,");
            String control = "Igual" + nvar + ",FinVerdad" + nvar;
            asmControl.add(control);
            Code.add("mov al," + var1);
            Code.add("cmp al," + var2);
            Code.add("jc If" + nvar);
            Code.add("jz Igual" + nvar);
            Code.add("jmp FinVerdad" + nvar);
            Code.add("If" + nvar + ":");
            Code.add("jmp FinVerdad" + nvar);
            Code.add("Igual" + nvar + ":");

        }

    }

    public void AddCerrar() {
        String ultimo = asmControl.get(asmControl.size() - 1);
        //System.out.println(ultimo);
        if (ultimo.startsWith("For")) {
            Code.add("; Cierre del for " + ultimo);
            Code.add("loop " + ultimo);
        }
        if (ultimo.startsWith("If")) {
            //String control = "If" + nvar+",Igual"+nvar+",Continua"+nvar;
            String[] string = ultimo.split(",");
            Code.add("jmp " + string[2]);
            Code.add(string[1] + ":");
            Code.add("jmp " + string[2]);
            Code.add(string[2] + ":");
        }

        if (ultimo.startsWith("Igual")) {
            String[] string = ultimo.split(",");
            Code.add("; Cierre del if " + ultimo);
            Code.add("jmp " + string[1]);
            Code.add(string[1] + ":");
        }

        if (ultimo.startsWith("FinWhile")) {
            //String control = "FinWhile" + nvar+","+wh+","+var1+","+op+","+var2,nvar;
            String []string = ultimo.split(",");
            Code.add("; Cierre del "+string[0]);
            if (string[3].equals("<")) {
                Code.add("mov al," + string[2]);
                Code.add("cmp al," + string[4]);
                Code.add("jc If" + string[5]);
                Code.add("jz Igual" + string[5]);
                Code.add("jmp " + string[0]);
                Code.add("If" + string[5] + ":");
                Code.add("jmp " + string[1]);
                Code.add("Igual" + string[5] + ":");
                Code.add("jmp " + string[0]);
                Code.add(string[0] + ":");
            }
            if (string[3].equals(">")) {
                Code.add("mov al," + string[4]);
                Code.add("cmp al," + string[2]);
                Code.add("jc If" + string[5]);
                Code.add("jz Igual" + string[5]);
                Code.add("jmp " + string[0]);
                Code.add("If" + string[5] + ":");
                Code.add("jmp " + string[1]);
                Code.add("Igual" + string[5] + ":");
                Code.add("jmp " + string[0]);
                Code.add(string[0] + ":");
            }
            if (string[3].equals("==")) {
                Code.add("mov al," + string[4]);
                Code.add("cmp al," + string[2]);
                Code.add("jc If" + string[5]);
                Code.add("jz Igual" + string[5]);
                Code.add("jmp " + string[0]);
                Code.add("If" + string[5] + ":");
                Code.add("jmp " + string[0]);
                Code.add("Igual" + string[5] + ":");
                Code.add("jmp " + string[1]);
                Code.add(string[0] + ":");
            }
        }
        asmControl.remove(asmControl.size()-1);
    }

    public void vaciarErrores() {
        Errores.clear();
    }

    public void vaciarCode() {
        Code.clear();
    }

    public void vaciarData() {
        Data.clear();
    }

    public void vacirVariables() {
        vName.clear();
        vTipo.clear();
        vValor.clear();
        asmVariables.clear();
    }
}
