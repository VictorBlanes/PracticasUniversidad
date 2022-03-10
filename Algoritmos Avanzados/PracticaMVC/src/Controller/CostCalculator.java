package Controller;

import Model.Data;
import java.util.Random;

public class CostCalculator {

    private final boolean[] calcDone = new boolean[5];
    private final int BASE = 5;
    private final int NUM_GRAPH = 5;
    private final int LENGTH_GRAPH = 10;
    private final Data dt;

    public CostCalculator() {
        dt = new Data(NUM_GRAPH, LENGTH_GRAPH);
    }

    public void calcCosts(boolean[] selected) {
        for (int i = 0; i < selected.length; i++) {
            if(!calcDone[i]){
                calcCost(Complejidad.values()[i]);
            }
        }
        
    }

    private double getMax(boolean[] activated) {
        int cont = 0;
        double res = 0.0;
        double[] max_graphs = {Double.MIN_VALUE, Double.MIN_VALUE};
        for (int i = 0; i < NUM_GRAPH; i++) {
            double value = dt.getTimeValuesXY(i, LENGTH_GRAPH - 1);
            if (activated[i]) {
                cont++;
                if (max_graphs[1] < value) {
                    max_graphs[0] = max_graphs[1];
                    max_graphs[1] = value;
                }
            }
        }
        if (cont != 0) {
            if (cont % 2 == 0) {
                res = (max_graphs[1] + max_graphs[0]) / 2;
            } else {
                res = max_graphs[1];
            }
        }
        return res;
    }

    private void calcCost(Complejidad tipo) {
        switch (tipo) {
            case LOGN -> {
                if (!calcDone[Complejidad.LOGN.ordinal()]) {
                    for (int x = 0; x < LENGTH_GRAPH; x++) {
                        dt.calcCostLog(BASE, x);
                    }
                    calcDone[Complejidad.LOGN.ordinal()] = true;
                }
            }
            case N -> {
                if (!calcDone[Complejidad.N.ordinal()]) {
                    for (int x = 0; x < LENGTH_GRAPH; x++) {
                        dt.calcCostN(BASE, x);
                    }
                    calcDone[Complejidad.N.ordinal()] = true;
                }
            }
            case NLOGN -> {
                if (!calcDone[Complejidad.NLOGN.ordinal()]) {
                    for (int x = 0; x < LENGTH_GRAPH; x++) {
                        dt.calcCostNLog(BASE, x);
                    }
                    calcDone[Complejidad.NLOGN.ordinal()] = true;
                }
            }
            case CUADRATIC -> {
                if (!calcDone[Complejidad.CUADRATIC.ordinal()]) {
                    for (int x = 0; x < LENGTH_GRAPH; x++) {
                        dt.calcCostCuadratic(BASE, x);
                    }
                    calcDone[Complejidad.CUADRATIC.ordinal()] = true;
                }
            }
            case NEXP -> {
                if (!calcDone[Complejidad.NEXP.ordinal()]) {
                    for (int x = 0; x < LENGTH_GRAPH; x++) {
                        dt.calcCostNEXP(BASE, x);
                    }
                    calcDone[Complejidad.NEXP.ordinal()] = true;
                }
            }

        }
    }

    private void randomFill() {
        Random rng = new Random();
        for (int i = 0; i < NUM_GRAPH; i++) {
            for (int j = 0; j < LENGTH_GRAPH; j++) {
                dt.setTimeValuesXY(i, j, rng.nextDouble(100));
            }
        }
    }
}
