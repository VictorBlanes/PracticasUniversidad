package Model;

import Controller.Controlador;
import practicaKaratsuba.PerEsdeveniments;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */

public class Data implements PerEsdeveniments {

    private String algoritmo, numero1, numero2, resultado;
    private int umbral;
    private double tiempoEjecucion;
    private static double[][] tiemposEstudio;
    private static Controlador controlador;

    public Data(Controlador controlador) {
        Data.controlador = controlador;
        Data.tiemposEstudio = new double[2][700];
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public void setNumero1(String numero1) {
        this.numero1 = numero1;
    }

    public void setNumero2(String numero2) {
        this.numero2 = numero2;
    }

    public void setUmbral(int umbral) {
        this.umbral = umbral;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setTiempoEjecucion(double tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public static void setTiemposEstudio(int x, int y, double value) {
        Data.tiemposEstudio[x][y] = value;
    }

    public String getResultado() {
        return resultado;
    }

    public double getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public double[][] getTiemposEstudio() {
        return tiemposEstudio;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public String getNumero1() {
        return numero1;
    }

    public String getNumero2() {
        return numero2;
    }

    public int getUmbral() {
        return umbral;
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Actualizar Modelo")) {
            String[] res = s.split("\\|");
            this.algoritmo = res[1];
            this.umbral = Integer.parseInt(res[2]);
            this.numero1 = res[3];
            this.numero2 = res[4];
        }
    }
}
