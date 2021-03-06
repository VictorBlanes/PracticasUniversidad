package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import practicaMVC.PerEsdeveniments;

/**
 *
 * @author vicbl
 */
public class Grafica extends JPanel implements PerEsdeveniments {

    private Vista vista;
    private final int PHEIGHT = 480;
    private final int PWIDTH = 940;
    private final int OFFSET = 30;

    public Grafica(Vista vista) {
        this.vista = vista;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        double[][] data = vista.getData();
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
            paintGraphs(g2d, data);
        }
    }

    private void paintGraphs(Graphics2D g2d, double[][] data) {
        Color[] palete = {Color.RED, Color.CYAN, Color.YELLOW, Color.GREEN, Color.PINK};
        boolean[] activated = vista.getSelected();
        boolean[] calcDone = vista.getCalcDone();
        double max = vista.getMax();
        int base = vista.getBase();
        g2d.drawString(String.format("%.2f", max), OFFSET - 27, OFFSET - 5);
        g2d.drawString(String.format("%.2f", (double) base * 10),
                OFFSET + (PWIDTH - OFFSET * 2) - 35, 15 + OFFSET + (PHEIGHT - OFFSET * 2));
        for (int i = 0; i < activated.length; i++) {
            if (activated[i] && calcDone[i]) {
                paintGraph(g2d, palete[i], data[i], max);
            }
        }
    }

    private void paintGraph(Graphics2D g2d, Color lineColor, double[] dataRow, double max) {
        double start, end;
        int graph_height = PHEIGHT - OFFSET * 2;
        g2d.setColor(lineColor);
        end = (dataRow[0] / max) * graph_height;
        g2d.drawLine(OFFSET, PHEIGHT - OFFSET,
                OFFSET + (PWIDTH - OFFSET * 2) / 10, PHEIGHT - (OFFSET + (int) end));
        start = end;
        for (int i = 0; i < dataRow.length - 1; i++) {
            end = (dataRow[i + 1] / max) * graph_height;
            g2d.fillOval((OFFSET + (i + 1) * (PWIDTH - OFFSET * 2) / 10) - 3, PHEIGHT - (OFFSET + (int) start) - 3, 5, 5);
            g2d.fillOval((OFFSET + (i + 2) * (PWIDTH - OFFSET * 2) / 10) - 3, PHEIGHT - (OFFSET + (int) end) - 3, 5, 5);
            g2d.drawLine(OFFSET + (i + 1) * (PWIDTH - OFFSET * 2) / 10, PHEIGHT - (OFFSET + (int) start),
                    OFFSET + (i + 2) * (PWIDTH - OFFSET * 2) / 10, PHEIGHT - (OFFSET + (int) end));
            start = end;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PWIDTH, PHEIGHT);
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Mostrar Resultados")) {
            repaint();
        }
    }

}
