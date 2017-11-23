/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corelli;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

/**
 *
 * @author AlejandroGS13
 */
public class LineNumberPane extends JPanel {

        private JTextArea ta;

        public LineNumberPane(JTextArea ta) {
            this.ta = ta;
            ta.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    revalidate();
                    repaint();
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    revalidate();
                    repaint();
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    revalidate();
                    repaint();
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            int lineCount = ta.getLineCount();
            Insets insets = getInsets();
            int min = fm.stringWidth("000");
            int width = Math.max(min, fm.stringWidth(Integer.toString(lineCount))) + insets.left + insets.right;
            int height = fm.getHeight() * lineCount;
            return new Dimension(width, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            FontMetrics fm = ta.getFontMetrics(ta.getFont());
            Insets insets = getInsets();

            Rectangle clip = g.getClipBounds();
            int rowStartOffset = ta.viewToModel(new Point(0, clip.y));
            int endOffset = ta.viewToModel(new Point(0, clip.y + clip.height));

            Element root = ta.getDocument().getDefaultRootElement();
            while (rowStartOffset <= endOffset) {
                try {
                    int index = root.getElementIndex(rowStartOffset);
                    Element line = root.getElement(index);

                    String lineNumber = "";
                    if (line.getStartOffset() == rowStartOffset) {
                        lineNumber = String.valueOf(index + 1);
                    }

                    int stringWidth = fm.stringWidth(lineNumber);
                    int x = insets.left;
                    Rectangle r = ta.modelToView(rowStartOffset);
                    int y = r.y + r.height;
                    g.drawString(lineNumber, x, y - fm.getDescent());

                    //  Move to the next row
                    rowStartOffset = Utilities.getRowEnd(ta, rowStartOffset) + 1;
                } catch (Exception e) {
                    break;
                }
            }
        }

    }
