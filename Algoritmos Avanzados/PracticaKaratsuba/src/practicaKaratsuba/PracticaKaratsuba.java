package practicaKaratsuba;

import Controller.Controlador;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */
public class PracticaKaratsuba {

    private Controlador controlador;

    public static void main(String[] args) {
        (new PracticaKaratsuba()).init();
    }

    public void init() {
        this.controlador = new Controlador(this);
        controlador.notificar("Ejecutar Estudio");
    }
}
