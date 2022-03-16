package View;

import Controller.CostCalculator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author vicbl
 */
public class Grafica extends JPanel {

    //TODO: Obtener la posicion del raton relativa el JPanel en cualquier momento.
    private CostCalculator cc = new CostCalculator(this);
    private final int HEIGHT = 480;
    private final int WIDTH = 940;
    private final int OFFSET = 30;
    private boolean[] activated = new boolean[5];
    private boolean[] calcDone = new boolean[5];
    private double[][] data = null;

    public Grafica() {
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(OFFSET, OFFSET, OFFSET, HEIGHT - OFFSET);
        g2d.drawLine(OFFSET, HEIGHT - OFFSET, WIDTH - OFFSET, HEIGHT - OFFSET);
        for (int i = 0; i <= 10; i++) {
            int longWidth = OFFSET + i * (WIDTH - OFFSET * 2) / 10;
            int longHeight = OFFSET + i * (HEIGHT - OFFSET * 2) / 10;
            g2d.drawLine(longWidth, HEIGHT - OFFSET, longWidth, HEIGHT - 20);
            g2d.drawLine(OFFSET, longHeight, 20, longHeight);
        }
        g2d.drawString("0.00", OFFSET - 27, 15 + OFFSET + (HEIGHT - OFFSET * 2));
        if (data != null) {
            paintGraphs(g2d);
        }
    }

    private void paintGraphs(Graphics2D g2d) {
        Color[] palete = {Color.RED, Color.CYAN, Color.YELLOW, Color.GREEN, Color.PINK};
        double max = cc.getMax(activated);
        int base = cc.getBASE();
        g2d.drawString(String.format("%.2f", max), OFFSET - 27, OFFSET - 5);
        g2d.drawString(String.format("%.2f", (double) base * 10),
                OFFSET + (WIDTH - OFFSET * 2) - 35, 15 + OFFSET + (HEIGHT - OFFSET * 2));
        for (int i = 0; i < activated.length; i++) {
            if (activated[i] && calcDone[i]) {
                paintGraph(g2d, palete[i], i, max);
            }
        }
    }

    private void paintGraph(Graphics2D g2d, Color lineColor, int ind, double max) {
        double start, end;
        int graph_height = HEIGHT - OFFSET * 2;
        g2d.setColor(lineColor);
        end = (data[ind][0] / max) * graph_height;
        g2d.drawLine(OFFSET, HEIGHT - OFFSET,
                OFFSET + (WIDTH - OFFSET * 2) / 10, HEIGHT - (OFFSET + (int) end));
        start = end;
        for (int i = 0; i < data[ind].length - 1; i++) {
            end = (data[ind][i + 1] / max) * graph_height;
            g2d.fillOval((OFFSET + (i + 1) * (WIDTH - OFFSET * 2) / 10) - 3, HEIGHT - (OFFSET + (int) start) - 3, 5, 5);
            g2d.fillOval((OFFSET + (i + 2) * (WIDTH - OFFSET * 2) / 10) - 3, HEIGHT - (OFFSET + (int) end) - 3, 5, 5);
            g2d.drawLine(OFFSET + (i + 1) * (WIDTH - OFFSET * 2) / 10, HEIGHT - (OFFSET + (int) start),
                    OFFSET + (i + 2) * (WIDTH - OFFSET * 2) / 10, HEIGHT - (OFFSET + (int) end));
            start = end;
        }
    }

    public void calcGraficos(boolean[] selected) {
        activated = selected;
        calcDone = new boolean[5];
        cc.calcCostsThread(this, selected); 
    }
    public void stopCalc(){
        cc.stopCalc();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public void setData(double[][] data) {
        this.data = data;
        repaint();
    }
    
    public void calcDone(int index){
        calcDone[index] = true;
    }

}
