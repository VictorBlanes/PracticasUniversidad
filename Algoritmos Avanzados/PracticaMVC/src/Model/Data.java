package Model;

import Controller.Controlador;
import practicaMVC.PerEsdeveniments;

public class Data implements PerEsdeveniments {

    private static Controlador controlador;
    private int base;
    private int num_graph;
    private int length_graph;
    private double[][] timeValues;
    private boolean[] calcDone;

    public Data(Controlador controlador, int num_graph, int length_graph, int base) {
        Data.controlador = controlador;
        this.base = base;
        this.length_graph = length_graph;
        this.num_graph = num_graph;
        this.timeValues = new double[num_graph][length_graph];
        this.calcDone = new boolean[num_graph];
    }

    public void setTimeValues(int funcion, int step, double value) {
        timeValues[funcion][step] = value;
    }

    public double getTimeValuesValue(int x, int y) {
        return timeValues[x][y];
    }

    public double[][] getTimeValues() {
        return timeValues;
    }

    public int getBase() {
        return base;
    }

    public boolean[] getCalcDone() {
        return calcDone;
    }

    public void setCalcDone(int ind) {
        this.calcDone[ind] = true;
    }

    public int getNum_graph() {
        return num_graph;
    }

    public int getLength_graph() {
        return length_graph;
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Limpiar Data")) {
            this.timeValues = new double[num_graph][length_graph];
            this.calcDone = new boolean[num_graph];
        }
    }
}
