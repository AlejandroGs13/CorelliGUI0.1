/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corelli;

import compilador.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * @author alejandrogs
 */
public class main extends JFrame implements ActionListener {

    Corelli corelli = new Corelli();
    Choosefile choosefile = new Choosefile();
    Archivos archivos = new Archivos();
    String url = "None";
    /**
     * **********************************
     */
    JButton btnRun, btnSave, btnOpen;
    Container contenido;
    JTextArea taCode, taDebug;
    JScrollPane scroll;
    boolean primera = true;
    /**
     * **********************************
     */
    public main() {
        super("Corelli");
        this.setSize(720, 680);
        this.setResizable(false);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
        Contain();
        repaint();
    }

    private void Contain() {
        contenido = getContentPane();
        contenido.setLayout(null);
        contenido.setVisible(true);
        Border border = BorderFactory.createLineBorder(Color.BLACK);

        taDebug = new JTextArea();
        taDebug.setEditable(false);
        JScrollPane scrollDebug = new JScrollPane(taDebug);
        scrollDebug.setSize(680, 150);
        scrollDebug.setLocation(20, 400);
        contenido.add(scrollDebug, BorderLayout.CENTER);
        /**
         * *****************************
         */
        btnRun = new JButton("Ejecutar");
        btnRun.setSize(120, 40);
        btnRun.setLocation(310, 600);
        btnRun.setVisible(true);
        btnRun.addActionListener(this);
        contenido.add(btnRun);
        /**
         * ********************************
         */
        /**
         * *****************************
         */
        btnSave = new JButton("Guardar");
        btnSave.setSize(120, 40);
        btnSave.setLocation(180, 600);
        btnSave.setVisible(true);
        btnSave.addActionListener(this);
        contenido.add(btnSave);
        /**
         * *****************************
         */
        btnOpen = new JButton("Abrir");
        btnOpen.setSize(120, 40);
        btnOpen.setLocation(440, 600);
        btnOpen.setVisible(true);
        btnOpen.addActionListener(this);
        contenido.add(btnOpen);
        /**
         * ********************************
         */
        //////////////////////////////////
        taCode = new JTextArea();
        taCode.setWrapStyleWord(true);
        taCode.setLineWrap(true);
        Font font = new Font("Dialog", Font.ITALIC, 14);
        taCode.setFont(font);
        JScrollPane sp = new JScrollPane(taCode);
        sp.setSize(675, 350);
        sp.setLocation(22, 20);
        sp.setRowHeaderView(new LineNumberPane(taCode));
        contenido.add(sp);
        ////////////////////////////////
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSave) {
            if (url.equals("None")) {
                url = choosefile.save(taCode);
                if (!url.equals("None")) {
                    archivos.writerFile(taCode.getText(), url + ".cain", false);
                }

            } else {
                archivos.writerFile(taCode.getText(), url, false);
            }
        }
        if (ae.getSource() == btnRun) {
            if (url.equals("None")) {
                url = choosefile.save(taCode);
                if (!url.equals("None")) {
                    taDebug.setText("");
                    archivos.writerFile(taCode.getText(), url + ".cain", false);
                    corelli.compilador(url);
                    System.out.println("111:" + corelli.EnviarErrores() + "22");
                    taDebug.setText(corelli.EnviarErrores());
                    primera = false;
                }

            } else {
                
                String temp = url;
                if (!temp.endsWith(".cain")) {
                    temp = temp + ".cain";
                }
                taDebug.setText("");
                archivos.writerFile(taCode.getText(), temp, false);
                corelli.compilador(temp);
                taDebug.setText(corelli.EnviarErrores());
                primera = false;
            }
        }
        if (ae.getSource() == btnOpen) {
            url = choosefile.open(taCode);
        }
    }

    public static void main(String[] args) {
        main main = new main();
    }

}
