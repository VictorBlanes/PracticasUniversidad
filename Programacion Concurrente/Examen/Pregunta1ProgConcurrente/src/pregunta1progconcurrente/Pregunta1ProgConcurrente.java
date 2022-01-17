/**
 * Pregunta 1 Examen programacion concurrente (2021/2022).
 * Simula el funcionamiento de la asignacion de mesas en un restaurante. Dicho
 * restaurante tiene NUM_SALONES salones y en cada salon hay NUM_SILLAS sillas.
 * A este restaurante pueden venir gente fumadora y no fumadora y se les tiene
 * que asignar una mesa de un salon tienendo en cuenta que debe haber espacio
 * suficiente en el salon y no se puede asignar a un no fumador una mesa en un
 * salon usado por fumadores.
 *
 * @author Victor Manuel Blanes Castro (43232361C)
 */
package pregunta1progconcurrente;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Pregunta1ProgConcurrente implements Runnable {

    private static final int NUM_SALONES = 3;
    private static final int NUM_SILLAS = 3;
    private static final int NUM_THREADS = 12;
    private static final int NUM_LOOPS = 1;
    static MonMaitre monitor = new MonMaitre(NUM_SALONES, NUM_SILLAS);

    String id;
    EstadoSalon es;

    /**
     * Metodo constructor. Inicializa las variables para crear un objeto de esta
     * clase, usado para inicalizar los hilos que hayan de los clientes del
     * restaurante.
     *
     * @param id: Identificador del hilo
     * @param es: Usado para distinguir fumador de no fumador.
     */
    public Pregunta1ProgConcurrente(String id, EstadoSalon es) {
        this.id = id;
        this.es = es;
    }

    /**
     * Metodo run. Metodo que ejecutara cada hilo que se cree. Simula el 
     * comportamiento de un cliente el cual entra al salon, come y se va una vez.
     * Se añaden esperar para impulsar el intercalado entre hilos.
     */
    @Override
    public void run() {
        int salon;
        for (int i = 0; i < NUM_LOOPS; i++) {
            try {
                System.out.printf("Hola, mi nombre es %s, me gustaria cenar y soy %s.\n", id, es);
                salon = monitor.entrarSalon(id, es);
                System.out.printf("%s dice: Me gusta mucho el salon %d.\n", id, salon);
                Thread.sleep(100);
                System.out.printf("%s dice: Ya he cenado, la cuenta porfavor.\n", id, salon);
                monitor.salirSalon(id, es, salon);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pregunta1ProgConcurrente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Metodo main. Lanza los threads y espera a que terminen.
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("SIMULACION DEL RESTAURANTE DE LOS SOLITARIOS");
        Thread[] threads = new Thread[NUM_THREADS];
        int i;
        for (i = 0; i < NUM_THREADS; i++) {
            if (i % 2 == 0) {
                threads[i] = new Thread(new Pregunta1ProgConcurrente("Cliente" + i, EstadoSalon.FUMADORES));
            } else {
                threads[i] = new Thread(new Pregunta1ProgConcurrente("Cliente" + i, EstadoSalon.NO_FUMADORES));
            }
            threads[i].start();
        }

        for (i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }
        System.out.println("FIN DE LA SIMULACION");
    }

}
