package View;

import Controller.Controlador;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */

public class Grafica extends JPanel {

    private Controlador controlador;
    private final int PHEIGHT = 480;
    private final int PWIDTH = 940;
    private final int OFFSET = 30;
    private int umbral = 320;
    private double[][] data = null;

    public Grafica(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, PWIDTH, PHEIGHT);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(OFFSET, OFFSET, OFFSET, PHEIGHT - OFFSET);
        g2d.drawLine(OFFSET, PHEIGHT - OFFSET, PWIDTH - OFFSET, PHEIGHT - OFFSET);
        for (int i = 0; i <= 10; i++) {
            int longWidth = OFFSET + i * (PWIDTH - OFFSET * 2) / 10;
            int longHeight = OFFSET + i * (PHEIGHT - OFFSET * 2) / 10;
            g2d.drawLine(longWidth, PHEIGHT - OFFSET, longWidth, PHEIGHT - 20);
            g2d.drawLine(OFFSET, longHeight, 20, longHeight);
        }
        g2d.drawString("0.00", OFFSET - 27, 15 + OFFSET + (PHEIGHT - OFFSET * 2));
        if (data != null) {
            paintGraphs(g2d);
        }
    }

    private void paintGraphs(Graphics2D g2d) {
        Color[] palete = {Color.CYAN, Color.YELLOW};
        int end = data[0].length - 1;
        double max = Math.max(data[0][end], data[1][end]);
        int base = 70;
        g2d.drawString(String.format("%.2f", max), OFFSET - 27, OFFSET - 5);
        g2d.drawString(String.format("%.2f", (double) base * 10),
                OFFSET + (PWIDTH - OFFSET * 2) - 35, 15 + OFFSET + (PHEIGHT - OFFSET * 2));
        g2d.setColor( Color.RED);
        g2d.drawLine(OFFSET + (umbral + 1) * (PWIDTH - OFFSET * 2) / data[0].length, OFFSET,
                    OFFSET + (umbral + 1) * (PWIDTH - OFFSET * 2) / data[0].length, PHEIGHT - OFFSET);
        for (int i = 0; i < data.length; i++) {
            paintGraph(g2d, palete[i], i, max);
        }
    }

    private void paintGraph(Graphics2D g2d, Color lineColor, int ind, double max) {
        double start, end;
        int graph_height = PHEIGHT - OFFSET * 2;
        g2d.setColor(lineColor);
        end = (data[ind][0] / max) * graph_height;
        g2d.drawLine(OFFSET, PHEIGHT - OFFSET,
                OFFSET + (PWIDTH - OFFSET * 2) / 10, PHEIGHT - (OFFSET + (int) end));
        start = end;
        for (int i = 0; i < data[ind].length - 1; i++) {
            end = (data[ind][i + 1] / max) * graph_height;
            g2d.drawLine(OFFSET + (i + 1) * (PWIDTH - OFFSET * 2) / data[ind].length, PHEIGHT - (OFFSET + (int) start),
                    OFFSET + (i + 2) * (PWIDTH - OFFSET * 2) / data[ind].length, PHEIGHT - (OFFSET + (int) end));
            start = end;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PWIDTH, PHEIGHT);
    }

    public void setData(double[][] data, int umbral) {
        this.data = data;
        this.umbral = umbral;
        repaint();
    }

}
