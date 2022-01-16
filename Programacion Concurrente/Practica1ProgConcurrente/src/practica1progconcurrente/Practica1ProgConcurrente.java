/**
 * Practica1ProgConcurrente.
 * Practica 1 de programacion concurrente, simula el funcionamiento de un baño 
 * mixto en un despacho de abogados, hay 6 hombres y mujeres y estos van al baño
 * 2 veces, en el baño puede caber maximo 3 personas y no pueden haber hombres y
 * mujeres en el baño al mismo tiempo. Se tiene que simular el acceso al baño 
 * mediante semaforos.
 * 
 * @author Victor Manuel Blanes Castro
 */
package practica1progconcurrente;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Practica1ProgConcurrente implements Runnable {

    static final int THREADS = 12; //Num de hombres + mujeres
    static final int MAX_COUNT = 2; //Num de veces que van al baño
    static final int MAX_GENTE_BANYO = 3; //Aforo maximo del baño
    static volatile int enBaño = 0;
    
    //Si hay hombre o mujeres en el baño en el momento, Ninguno si no hay nadie
    static volatile HombreMujer estadoBaño = HombreMujer.Ninguno;
    

    static Semaphore mutex = new Semaphore(1);
    static Semaphore bañoVacio = new Semaphore(MAX_GENTE_BANYO);

    int id;
    HombreMujer hm;
    boolean permit;

    /**
     * Metodo constructor. Inicializa las variables para crear una objeto de
     * esta clase, usado inicializar los hilos. La varaible permit se usara mas
     * adelante para controlar que hombre y mujeres no esten juntos en el baño.
     *
     * @param id: Identificador del hilo
     * @param hm: Usado para distinguir si es un hombre o mujer.
     */
    public Practica1ProgConcurrente(int id, HombreMujer hm) {
        this.id = id;
        this.hm = hm;
        this.permit = true;
    }

    /**
     * Metodo run. Metodo que ejecutara cada hilo que se cree, entra y sale del
     * baño(Seccion critica) MAX_COUNT veces, se añaden esperas para impulsar
     * mas el intercalado entre hilos.
     */
    @Override
    public void run() {
        try {
            System.out.printf("Soy %s %d.\n", hm, id);
            for (int i = 0; i < MAX_COUNT; i++) {
                System.out.printf("%s %d trabaja [%d/%d].\n", hm, id, (i + 1), MAX_COUNT);
                entraBaño(hm);
                Thread.sleep(100); //En el baño
                salirBaño();
                Thread.sleep(100); //Trabajando

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Practica1ProgConcurrente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo entraBaño. Controla el acceso a la seccion critica, al entrar
     * aumenta enBaño y pone el estadoBaño a hombre o mujer.
     *
     * @param hm
     * @throws InterruptedException
     */
    private void entraBaño(HombreMujer hm) throws InterruptedException {
        /* Los semaforos solo controlan el aforo y la seccion critica, el bucle 
        controla que un hombre no acceda cuando hay mujeres. Si es hombre y 
        hay mujeres hara release de los dos semaforos y seguira dentro del bucle, 
        bloqueandose otra vez, dejando continuar a otro hilo.*/
        while (permit) {
            permit = false;
            bañoVacio.acquire();
            mutex.acquire();
            if (estadoBaño != HombreMujer.Ninguno && estadoBaño != hm) {
                bañoVacio.release();
                mutex.release();
                permit = true;
            }
        }
        estadoBaño = hm;
        enBaño++;
        System.out.printf("%s %d esta en el baño [%d/3].\n", hm, id, enBaño);
        mutex.release();
    }
    
    /**
     * Metodo salirBaño. Controla el acceso a la seccion critica, reduce enBaño
     * y, en el caso de que el baño este vacio, pone el estadoBaño a ninguno
     * para que puedan entrar hombre y mujeres.
     * 
     * @throws InterruptedException 
     */
    private void salirBaño() throws InterruptedException {
        mutex.acquire();
        enBaño--;
        System.out.printf("%s %d sale en el baño [%d/3].\n", hm, id, enBaño);
        if (enBaño == 0) {
            System.out.println("****BAÑO VACIO****");
            estadoBaño = HombreMujer.Ninguno;
        }
        permit = true;
        bañoVacio.release();
        mutex.release();
    }

    /**
     * Main.
     * Lanza un numero THREADS de hilos y espera a que terminen.
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[THREADS];
        int i;

        for (i = 0; i < THREADS; i++) {
            if (i % 2 == 0) {
                threads[i] = new Thread(new Practica1ProgConcurrente(i, HombreMujer.Hombre));
            } else {
                threads[i] = new Thread(new Practica1ProgConcurrente(i, HombreMujer.Mujer));
            }
            threads[i].start();
        }

        for (i = 0; i < THREADS; i++) {
            threads[i].join();
        }
    }

}

enum HombreMujer {
    Hombre, Mujer, Ninguno
}
