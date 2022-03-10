package IU;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author vicbl
 */
public class Grafica extends JPanel {

    private final int HEIGHT = 480;
    private final int WIDTH = 940;
    private final int OFFSET = 20;
    private double[][] test = new double[5][10];
    private boolean[] isDone = new boolean[5];
    private boolean[] activated = new boolean[5];
    private double max;

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
            g2d.drawLine(OFFSET + i * (WIDTH - OFFSET * 2) / 10, HEIGHT - OFFSET,
                    OFFSET + i * (WIDTH - OFFSET * 2) / 10, HEIGHT - 15);
            g2d.drawLine(OFFSET, OFFSET + i * (HEIGHT - OFFSET * 2) / 10,
                    15, OFFSET + i * (HEIGHT - OFFSET * 2) / 10);
        }
        paintGraphs(g2d);
    }

    private void paintGraphs(Graphics2D g2d) {
        Color[] palete = {Color.RED, Color.CYAN, Color.YELLOW, Color.GREEN, Color.PINK};
        max = getMax();
        for (int i = 0; i < activated.length; i++) {
            if (activated[i]) {
                paintGraph(g2d, palete[i], i);
            }
        }
    }

    private void paintGraph(Graphics2D g2d, Color lineColor, int dataPos) {
        double start, end;
        int graph_height = HEIGHT - OFFSET * 2;
        g2d.setColor(lineColor);
        end = (test[dataPos][0] / max) * graph_height;
        g2d.drawLine(OFFSET, HEIGHT - OFFSET,
                OFFSET + (WIDTH - OFFSET * 2) / 10, HEIGHT - (OFFSET + (int) end));
        start = end;
        for (int i = 0; i < test[dataPos].length - 1; i++) {
            end = (test[dataPos][i + 1] / max) * graph_height;
            g2d.fillOval((OFFSET + (i + 1) * (WIDTH - OFFSET * 2) / 10) - 3, HEIGHT - (OFFSET + (int) start) - 3, 5, 5);
            g2d.fillOval((OFFSET + (i + 2) * (WIDTH - OFFSET * 2) / 10) - 3, HEIGHT - (OFFSET + (int) end) - 3, 5, 5);
            g2d.drawLine(OFFSET + (i + 1) * (WIDTH - OFFSET * 2) / 10, HEIGHT - (OFFSET + (int) start),
                    OFFSET + (i + 2) * (WIDTH - OFFSET * 2) / 10, HEIGHT - (OFFSET + (int) end));
            start = end;

        }
    }

    private void randomFill() {
        Random rng = new Random();
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[i].length; j++) {
                test[i][j] = rng.nextDouble(100);
            }
        }
    }

    private double getMax() {
        int cont = 0;
        double res = 0.0;
        double max = Double.MIN_VALUE;
        ArrayList<Integer> test2 = new ArrayList();
        for (int i = 0; i < test.length; i++) {
            if (activated[i] && max < test[i][test[i].length - 1]) {
                max = test[i][test[i].length - 1];
                cont++;
                test2.add(i);
            }
        }
        if (!test2.isEmpty()) {
            if (test2.size() % 2 == 0) {
                int act = test2.get(test2.size() / 2);
                int preact = test2.get(test2.size() / 2 - 1);
                double a = test[act][test[act].length - 1];
                double b = test[preact][test[preact].length - 1];
                res = a / b;
            } else {
                res = test[test2.get(test2.size() / 2)][test[test2.get(test2.size() / 2)].length - 1];
            }
        }
        return res;
    }

    private void calcCost(Complejidad tipo) {
        Random rng = new Random();
        final int base = 5;
        switch (tipo) {
            case LOGN:
                if (!isDone[0]) {
                    for (int x = 0; x < test[0].length; x++) {
                        double ite = (Math.log10(base * (x + 1)) / Math.log10(2));
                        test[0][x] = ite;
                    }
                    isDone[0] = true;
                }
                break;
            case N:
                if (!isDone[1]) {
                    for (int x = 0; x < test[1].length; x++) {
                        double ite = base * (x + 1);
                        test[1][x] = ite;
                    }
                    isDone[1] = true;
                }
                break;
            case NLOGN:
                if (!isDone[2]) {
                    for (int x = 0; x < test[2].length; x++) {
                        double ite = (base * (x + 1)) * (Math.log10(base * (x + 1)) / Math.log10(2));
                        test[2][x] = ite;
                    }
                    isDone[2] = true;
                }
                break;
            case CUADRATIC:
                if (!isDone[3]) {
                    for (int x = 0; x < test[2].length; x++) {
                        double ite = (base * (x + 1)) * (base * (x + 1));
                        test[3][x] = ite;
                    }
                    isDone[3] = true;
                }
                break;
            case NEXP:
                if (!isDone[4]) {
                    for (int x = 0; x < test[2].length; x++) {
                        double ite = Math.pow(2, base * (x + 1));
                        test[4][x] = ite;
                    }
                    isDone[4] = true;
                }
                break;
        }
    }

    public void mostrarGraficos(boolean[] selected) {
        activated = selected;
        for (int i = 0; i < activated.length; i++) {
            if (activated[i] && !isDone[i]) {
                calcCost(Complejidad.values()[i]);
            }
        }
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}

enum Complejidad {
    LOGN, N, NLOGN, CUADRATIC, NEXP;
}
