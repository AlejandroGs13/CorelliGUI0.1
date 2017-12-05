/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author corelli
 */
public class Expresiones {

    private String completo;
    private String id;
    private String juez;
    private String letras;
    private String enviar;
    private String expImp;
    private String expImp2;
    private String opCompleto;
    private String aritmeticas;
    private String opJuez;
    private String opLetras;
    private String obtener;
    private String verdad;
    private String logicos;
    private String xveces;
    private String lenguaje;
    public Expresiones() {
        this.id = "([a-zA-z]+[0-9]*)";
        this.aritmeticas = "(\\*|\\/|\\%|\\+|\\-)";
        this.logicos = "(\\<|\\>|(\\=){2}|\\<\\=|\\>\\=|\\!\\=)";
        this.completo = "([\\s]*completo[\\s]+" + id + "([\\s]+\\=[\\s]+[0-9]+)*[\\s]*)";
        this.juez = "([\\s]*juez[\\s]+" + id + "([\\s]+\\=[\\s]+(true|false))*[\\s]*)";
        this.expImp = "((\"([.]*[^\"̣][\\s]*)*\"[\\s]*\\+[\\s]*)*(\"([.]*[^\"̣][\\s]*)*\"))";
        this.expImp2 = "((((\"([.]*[^\"̣][\\s]*)*\")|(" + id + "))[\\s]*\\+[\\s]*)*((\"([.]*[^\"̣][\\s]*)*\")|(" + id + ")))";
        this.letras = "([\\s]*letras[\\s]+" + id + "([\\s]+\\=[\\s]+" + expImp + ")*[\\s]*)";
        this.enviar = "([\\s]*enviar[\\s]+\\([\\s]+((" + expImp + "|"+id+"|[0-9]+)*|((" + expImp + "|"+id+"|[0-9]+)([\\s]+\\+[\\s]+(" + expImp + "|"+id+"|[0-9]+))*)|[0-9]+)[\\s]+\\)[\\s]*)";
        this.opCompleto = "[\\s]*" + id + "[\\s]+\\=[\\s]+((" + id + "|[0-9]+)[\\s]+" + aritmeticas + "[\\s]+)*(" + id + "|[0-9]+)[\\s]*";
        this.opJuez = "([\\s]*" + id + "[\\s]+\\=[\\s]+(false|true|" + id + ")[\\s]*)";
        this.opLetras = "([\\s]*" + id + "[\\s]+\\=[\\s]+(" + expImp2 + ")[\\s]*)";
        this.obtener = "([\\s]*obtener[\\s]+\\([\\s]+"+id+"[\\s]+\\)[\\s]*)";
        this.lenguaje = "[^a-zA-Z\\s\\(\\)\\/\\+\\*\\-\\\"\\%{\\}\\;\\<\\>\\=\\|\\'\\!0-9]+";
        this.verdad = "([\\s]*(verdad|durante)[\\s]+\\([\\s]+(((([0-9]+)|(" + id + "))[\\s]+(" + logicos + ")[\\s]+(([0-9]+)|(" + id + ")))|(" + id + "))[\\s]+\\)[\\s]+\\>\\>[\\s]*)";
        this.xveces = "([\\s]*xveces[\\s]+\\([\\s]+" + id + "[\\s]+\\=[\\s]+((" + id + ")|([0-9]+)|(("+id+"|[0-9]+)[\\s]+"+aritmeticas+"[\\s]+("+id+"|[0-9]+)))[\\s]+\\;[\\s]+(" + id + "|[0-9]+)[\\s]+" + logicos + "[\\s]+(" + id + "|[0-9]+)[\\s]+\\;[\\s]+" + id + "[\\s]+(\\+\\+|\\-\\-)[\\s]+\\)[\\s]+\\>\\>[\\s]*)";
    }

    public String getLenguaje() {
        return lenguaje;
    }
    
    public String getCompleto() {
        return completo;
    }

    public String getObtener() {
        return obtener;
    }
    
    public String getOpJuez() {
        return opJuez;
    }

    public String getJuez() {
        return juez;
    }

    public String getLetras() {
        return letras;
    }

    public String getEnviar() {
        return enviar;
    }

    public String getOpCompleto() {
        return opCompleto;
    }

    public String getOpLetras() {
        return opLetras;
    }

    public String getVerdad() {
        return verdad;
    }

    public String getXveces() {
        return xveces;
    }

}
