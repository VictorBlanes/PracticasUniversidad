package Controller;

import Model.Data;
import View.Grafica;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CostCalculator {

//    private final boolean[] calcDone = new boolean[5];
    private final int BASE = 5;
    private final int NUM_GRAPH = 5;
    private final int LENGTH_GRAPH = 10;
    private final Data dt = new Data(NUM_GRAPH, LENGTH_GRAPH, BASE, this);
    private Grafica graf;
    public CostCalculator(Grafica graf) {
        this.graf = graf;
    }

    public void calcCostsThread(Grafica grafica, boolean[] selected) {
        ArrayList<Thread> threads = new ArrayList<>();
        dt.setActive(true);
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                threads.add(new Thread(new Data(Complejidad.values()[i])));
                threads.get(threads.size() - 1).start();
            }
        }
    }

    public double getMax(boolean[] activated) {
        ArrayList<Double> maximos = new ArrayList<>();
        double res = 0.0;
        for (int i = 0; i < NUM_GRAPH; i++) {
            double value = dt.getTimeValuesXY(i, LENGTH_GRAPH - 1);
            if (activated[i]) {
                maximos.add(value);
            }
        }
        if (!maximos.isEmpty()) {
            int size = maximos.size();
            if (size % 2 == 0) {
                res = (maximos.get(size / 2) + maximos.get(size / 2 - 1)) / 2;
            } else {
                res = maximos.get(size / 2);
            }
        }
        return res;
    }

    private void randomFill() {
        Random rng = new Random();
        for (int i = 0; i < NUM_GRAPH; i++) {
            for (int j = 0; j < LENGTH_GRAPH; j++) {
                dt.setTimeValuesXY(i, j, rng.nextDouble(100));
            }
        }
    }

    public int getBASE() {
        return BASE;
    }

    public void dataToView(double[][] data, int graph_done) {
        graf.setData(data);
        graf.calcDone(graph_done);
    }

    public void stopCalc() {
        dt.setActive(false);
    }

}
