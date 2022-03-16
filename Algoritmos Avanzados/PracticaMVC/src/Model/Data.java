package Model;

import Controller.Complejidad;
import Controller.CostCalculator;
import View.Grafica;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data implements Runnable {

    private static double[][] timeValues;
    private static int BASE;
    private Complejidad comp;
    private static CostCalculator costCalc;
    private static boolean active;

    public Data(int num_graph, int length_graph, int base, CostCalculator costCalc) {
        timeValues = new double[num_graph][length_graph];
        this.BASE = base;
        this.costCalc = costCalc;
    }

    public Data(Complejidad comp) {
        this.comp = comp;
    }

    public void calcCostLog(int base, int step) throws InterruptedException {
        double ite = (Math.log10(base * (step + 1)) / Math.log10(2));
        Thread.sleep((long) ite);

        timeValues[Complejidad.LOGN.ordinal()][step] = ite;
    }

    public void calcCostN(int base, int step) throws InterruptedException {
        double ite = base * (step + 1);
        Thread.sleep((long) ite);
        timeValues[Complejidad.N.ordinal()][step] = ite;
    }

    public void calcCostNLog(int base, int step) throws InterruptedException {
        double ite = (base * (step + 1)) * (Math.log10(base * (step + 1)) / Math.log10(2));
        Thread.sleep((long) ite);
        timeValues[Complejidad.NLOGN.ordinal()][step] = ite;
    }

    public void calcCostCuadratic(int base, int step) throws InterruptedException {
        double ite = (base * (step + 1)) * (base * (step + 1));
        Thread.sleep((long) ite);
        timeValues[Complejidad.CUADRATIC.ordinal()][step] = ite;
    }

    public void calcCostNEXP(int base, int step) throws InterruptedException {
        double ite = Math.pow(2, base * (step + 1));
        double sec = ite / 1000;
        double minutes = sec / 60;
        double hours = minutes / 60;
        double days = hours / 24;
        System.out.printf("2^n con n = %-2d tardaria: %-30s %-30s %-30s %-30s\n", base * (step + 1), sec + " segundos", minutes + " minutos", hours + " horas", days + " dias");
        timeValues[Complejidad.NEXP.ordinal()][step] = ite;
    }

    public double getTimeValuesXY(int x, int y) {
        return timeValues[x][y];
    }

    public void setTimeValuesXY(int x, int y, double value) {
        timeValues[x][y] = value;
    }

    public double[][] getTimeValues() {
        return timeValues;
    }

    public void dataToView(Grafica grafica) {
        grafica.setData(timeValues);
    }

    @Override
    public void run() {
        try {
            switch (comp) {
                case LOGN -> {
                    for (int x = 0; x < timeValues[comp.ordinal()].length && active; x++) {
                        calcCostLog(BASE, x);
                    }
                    if (active) {
                        costCalc.dataToView(timeValues, comp.ordinal());
                    }
                }
                case N -> {
                    for (int x = 0; x < timeValues[comp.ordinal()].length && active; x++) {
                        calcCostN(BASE, x);
                    }
                    if (active) {
                        costCalc.dataToView(timeValues, comp.ordinal());
                    }
                }
                case NLOGN -> {
                    for (int x = 0; x < timeValues[comp.ordinal()].length && active; x++) {
                        calcCostNLog(BASE, x);
                    }
                    if (active) {
                        costCalc.dataToView(timeValues, comp.ordinal());
                    }
                }
                case CUADRATIC -> {
                    for (int x = 0; x < timeValues[comp.ordinal()].length && active; x++) {
                        calcCostCuadratic(BASE, x);
                    }
                    if (active) {
                        costCalc.dataToView(timeValues, comp.ordinal());
                    }
                }
                case NEXP -> {
                    for (int x = 0; x < timeValues[comp.ordinal()].length && active; x++) {
                        calcCostNEXP(BASE, x);
                    }
                    if (active) {
                        costCalc.dataToView(timeValues, comp.ordinal());
                    }
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setActive(boolean b) {
        active = b;
    }
}
