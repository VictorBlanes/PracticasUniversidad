package Controller;

import Model.Data;
import View.Vista;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import practicaMVC.PerEsdeveniments;
import practicaMVC.PracticaMVC;

public class Controlador implements Runnable, PerEsdeveniments {

    private static PracticaMVC main;
    private static Vista vista;
    private static Data modelo;
    private static boolean active;
    private static int activeThreads;
    private double maximo;
    private int zoom;
    private Complejidad complejidad;
    private final static int MAX_WAIT = 10000;

    public Controlador(PracticaMVC main, int base, int num_graph, int length_graph) {
        Controlador.main = main;
        Controlador.vista = new Vista(this);
        Controlador.modelo = new Data(this, num_graph, length_graph, base);
    }

    public Controlador(Complejidad complejidad) {
        this.complejidad = complejidad;
    }

    public void calcCostsThread() {
        if (!active) {
            active = true;
            ArrayList<Thread> threads = new ArrayList<>();
            for (int i = 0; i < modelo.getNum_graph(); i++) {
                activeThreads++;
                threads.add(new Thread(new Controlador(Complejidad.values()[i])));
                threads.get(threads.size() - 1).start();
            }
        }
    }

    public void calcCost(Complejidad type, int base, int step) throws InterruptedException {
        double ite;
        switch (complejidad) {
            case LOGN -> {
                ite = (Math.log10(base * (step + 1)) / Math.log10(2));
            }
            case N -> {
                ite = base * (step + 1);
            }
            case NLOGN -> {
                ite = (base * (step + 1)) * (Math.log10(base * (step + 1)) / Math.log10(2));
            }
            case CUADRATIC -> {
                ite = (base * (step + 1)) * (base * (step + 1));
            }
            case NEXP -> {
                ite = Math.pow(2, base * (step + 1));
            }
            default ->
                ite = 0.0;
        }
        if (ite > MAX_WAIT) {
            double sec = ite / 1000;
            double minutes = sec / 60;
            double hours = minutes / 60;
            double days = hours / 24;
            System.out.printf("%s con n = %-2d tardaria: %-30s %-30s %-30s %-30s\n",
                    complejidad, base * (step + 1), sec + " segundos", minutes + " minutos", hours + " horas", days + " dias");
        } else {
            Thread.sleep((long) ite);
        }
        modelo.setTimeValues(type.ordinal(), step, ite);
    }

    public double getMax() {
        ArrayList<Double> maximos = new ArrayList<>();
        double res = 0.0;
        int num_graph = modelo.getNum_graph();
        int length_graph = modelo.getLength_graph();
        boolean[] activated = vista.getSelected();
        boolean[] calcDone = modelo.getCalcDone();
        for (int i = 0; i < num_graph; i++) {
            double value = modelo.getTimeValuesValue(i, length_graph - 1);
            if (activated[i] && calcDone[i]) {
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
        if (zoom > 0) {
            res = res * Math.pow(1.2, zoom);
        } else if (zoom < 0) {
            res = res * Math.pow(0.8, Math.abs(zoom));
        }
        maximo = res;
        return maximo;
    }

    public int getBase() {
        return modelo.getBase();
    }

    public double[][] getData() {
        return modelo.getTimeValues();
    }

    public int getNum_graph() {
        return modelo.getNum_graph();
    }

    public boolean[] getCalcDone() {
        return modelo.getCalcDone();
    }

    @Override
    public void run() {
        int base = modelo.getBase();
        int dataLength = modelo.getTimeValues()[0].length;
        boolean selected = vista.getSelected()[complejidad.ordinal()];
        if (selected) {
            try {
                for (int x = 0; x < dataLength && active; x++) {
                    calcCost(complejidad, base, x);
                }
                modelo.setCalcDone(complejidad.ordinal());
                if (active) {
                    notificar("Mostrar Resultados");
                }
                activeThreads--;

            } catch (InterruptedException ex) {
                Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            activeThreads--;
        }
        if (activeThreads == 0) {
            active = false;
        }
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Calcular Graficos")) {
            calcCostsThread();
        } else if (s.startsWith("Detener Graficos")) {
            active = false;
        } else if (s.startsWith("Mostrar Resultados")) {
            vista.notificar(s);
        } else if (s.startsWith("Limpiar Data")) {
            zoom = 0;
            modelo.notificar(s);
        } else if (s.startsWith("Subir Maximo")) {
            if (!active && maximo != 0.0) {
                zoom++;
            }
        } else if (s.startsWith("Bajar Maximo")) {
            if (!active && maximo != 0.0) {
                zoom--;
            }
        }
    }
}
