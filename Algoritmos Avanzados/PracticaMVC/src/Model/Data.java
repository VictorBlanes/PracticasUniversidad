package Model;

import Controller.Complejidad;
import View.Grafica;

public class Data {

    private final double[][] timeValues;

    public Data(int num_graph, int length_graph) {
        timeValues = new double[num_graph][length_graph];
    }
    
    public void calcCostLog(int base, int step) {
        double ite = (Math.log10(base * (step + 1)) / Math.log10(2));
        timeValues[Complejidad.LOGN.ordinal()][step] = ite;
    }

    public void calcCostN(int base, int step) {
        double ite = base * (step + 1);
        timeValues[Complejidad.N.ordinal()][step] = ite;
    }

    public void calcCostNLog(int base, int step) {
        double ite = (base * (step + 1)) * (Math.log10(base * (step + 1)) / Math.log10(2));
        timeValues[Complejidad.NLOGN.ordinal()][step] = ite;
    }

    public void calcCostCuadratic(int base, int step) {
        double ite = (base * (step + 1)) * (base * (step + 1));
        timeValues[Complejidad.CUADRATIC.ordinal()][step] = ite;
    }

    public void calcCostNEXP(int base, int step) {
        double ite = Math.pow(2, base * (step + 1));
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
    
    public void dataToView(Grafica grafica){
        grafica.setData(timeValues);
    }
}
