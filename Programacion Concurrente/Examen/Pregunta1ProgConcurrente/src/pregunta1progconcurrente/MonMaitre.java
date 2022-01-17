/**
 * Clase MonMaitre.
 * Clase encargada de controlar el acceso a la seccion critica, se trata de un
 * monitor con variables explicitas, se usa dos condition para bloquear a los
 * procesos (Un condition para fumadores y otro para no fumadores) y estructuras
 * array para controlar el estado de cada salon.
 *
 * @author Victor Manuel Blanes Castro (43232361C)
 */
package pregunta1progconcurrente;

import java.util.Arrays;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonMaitre {

    private EstadoSalon[] est;
    private int[] aforo;
    private final int CAP_MAXIMA;
    private final Lock lock = new ReentrantLock();
    private final Condition fumadores, noFumadores;

    /**
     * Metodo constructor. Inicializa las variables del monitor. Al principio
     * todos los salones estan libres.
     *
     * @param num_salones: Numero de salones en el restaurante.
     * @param num_sillas: Numero de sillas por salon.
     */
    public MonMaitre(int num_salones, int num_sillas) {
        this.est = new EstadoSalon[num_salones];
        this.aforo = new int[num_salones];
        this.CAP_MAXIMA = num_sillas;
        this.fumadores = lock.newCondition();
        this.noFumadores = lock.newCondition();
        Arrays.fill(aforo, num_sillas);
        Arrays.fill(est, EstadoSalon.LIBRE);
    }

    /**
     * Metodo entrarSalon. Controla el acceso a la seccion critica, comprueba si
     * hay un salon del mismo tipo que el cliente (Fumadores/No fumadores) y lo
     * asigna ahi reduciendo el aforo de ese salon en 1 y marcando el salon para
     * fumadores o no fumadores. Si no hay bloquea el hilo y espera a que uno
     * este libre. Cuando libera un hilo comienza la busqueda de salones desde
     * el principio. Se usa un while en vez de un if para poder cumplir IRR.
     *
     * @param id Identificador del hilo, se usa solo para prints
     * @param es Variable que indica si el hilo es fumador o no fumador.
     * @return int con el salon a donde se ha asignado el hilo
     * @throws InterruptedException
     */
    public int entrarSalon(String id, EstadoSalon es) throws InterruptedException {
        lock.lock();
        boolean encontrado = false;
        int i = 0;
        try {
            for (i = 0; i < aforo.length && !encontrado; i++) {
                if ((est[i] == es && aforo[i] != 0) || est[i] == EstadoSalon.LIBRE) {
                    encontrado = true;
                }
                while (i == aforo.length - 1 && !encontrado) {
                    if (es == EstadoSalon.FUMADORES) {
                        fumadores.await();
                    } else {
                        noFumadores.await();
                    }
                    i = 0;
                }
            }
            i--;
            System.out.printf("***** El Sr./Sra. %s tiene silla en el salon %d de %s.\n", id, i, es);
            aforo[i]--;
            est[i] = es;
            return i;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Metodo salirSalon. Controla el acceso a la seccion critica aumenta en 1
     * el aforo de ese salon y, en el caso de que el salon se quede vacio, se
     * marca como libre para fumadores y no fumadores y avisa a todos los hilos
     * bloqueados. Si no se queda vacio solamente avisa a los fumadores si es
     * fumador o los no fumadores si es no fumador.
     *
     * @param id: Identificador del hilo, se usa solo para prints.
     * @param es: Variable que indica si el hilo es fumador o no fumador.
     * @param salon: Indica cual fue el salon que se le asigno a este hilo.
     */
    public void salirSalon(String id, EstadoSalon es, int salon) {
        lock.lock();
        try {
            aforo[salon]++;
            System.out.printf("***** Se libera un sitio del salon %d de %s. Quedan %s comensales.\n", salon, es, CAP_MAXIMA - aforo[salon]);
            if (aforo[salon] == CAP_MAXIMA) {
                System.out.printf("***** El salon %d se ha quedado vacio.\n", salon);
                est[salon] = EstadoSalon.LIBRE;
                fumadores.signalAll();
                noFumadores.signalAll();
            } else {
                if (es == EstadoSalon.FUMADORES) {
                    fumadores.signal();
                } else {
                    noFumadores.signal();
                }
            }
            System.out.printf("Que tenga un buen dia Sr./Sra. %s.\n", id);
        } finally {
            lock.unlock();
        }
    }

}
