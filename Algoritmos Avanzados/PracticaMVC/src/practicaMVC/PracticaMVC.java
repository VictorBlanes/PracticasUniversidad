package practicaMVC;

import Controller.Controlador;


public class PracticaMVC {
    
    private Controlador controlador;
    private static final int BASE = 5;
    private static final int NUM_GRAPH = 5;
    private static final int LENGTH_GRAPH = 10;
    
    public static void main(String[] args) {
        (new PracticaMVC()).init();
    }
    
    public void init(){
        this.controlador = new Controlador(this, BASE, NUM_GRAPH, LENGTH_GRAPH);
    }

}
