package Model;

import Controller.Controlador;
import Model.Piezas.Pieza;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */
public class Data {

    private static Controlador controlador;
    private int dimensionTablero;
    private ArrayList<Pieza> piezasEnTablero;
    double tiempoEjecucion;
    
    public Data(Controlador controlador, int dimensionTablero) {
        Data.controlador = controlador;
        this.dimensionTablero = dimensionTablero;
        this.piezasEnTablero = new ArrayList();
    }

    public int getDimensionTablero() {
        return dimensionTablero;
    }

    public ArrayList<Pieza> getPiezasEnTablero() {
        return piezasEnTablero;
    }

    public void newPiezasEnTablero() {
        this.piezasEnTablero = new ArrayList();
    }
    
    public void addPiezaEnTablero(Pieza pieza) {
        this.piezasEnTablero.add(pieza);
    }

    public void setDimensionTablero(int dimensionTablero) {
        this.dimensionTablero = dimensionTablero;
    }
    
    public void setTiempoEjecucion(double tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public double getTiempoEjecucion() {
        return tiempoEjecucion;
    }
    
    public boolean hayPiezasEnTablero(){
        return !piezasEnTablero.isEmpty();
    }
}
