package practicaBacktracking;

import Controller.Controlador;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */

public class PracticaBacktracking {

    private Controlador controlador;

    public static void main(String[] args) {
        (new PracticaBacktracking()).init();
    }

    public void init() {
        this.controlador = new Controlador(this,8);
        controlador.notificar("Ejecutar Estudio");
    }
}
