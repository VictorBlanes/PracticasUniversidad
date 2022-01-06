package practica1progconcurrente;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Practica1ProgConcurrente implements Runnable {

    static final int THREADS = 12;
    static final int MAX_COUNT = 2;
    static final int MAX_GENTE_BANYO = 3;
    static volatile int aforo = 0;
    static volatile HombreMujer estadoBaño = HombreMujer.Ninguno;

    static Semaphore mutex = new Semaphore(1);
    static Semaphore bañoVacio = new Semaphore(MAX_GENTE_BANYO);

    int id;
    HombreMujer hm;
    boolean permit;

    public Practica1ProgConcurrente(int id, HombreMujer hm) {
        this.id = id;
        this.hm = hm;
        this.permit = true;
    }

    @Override
    public void run() {
        if (hm == HombreMujer.Hombre) {
            try {
                runHombre();
            } catch (InterruptedException ex) {
                Logger.getLogger(Practica1ProgConcurrente.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                runMujer();
            } catch (InterruptedException ex) {
                Logger.getLogger(Practica1ProgConcurrente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void runHombre() throws InterruptedException {
        System.out.printf("Soy %s %d.\n", hm, id);
        for (int i = 0; i < MAX_COUNT; i++) {
            System.out.printf("%s %d trabaja [%d/%d].\n", hm, id, (i + 1), MAX_COUNT);
            entra_baño(hm);
            Thread.sleep(100); //En el baño
            salir_baño();
            Thread.sleep(100); //Trabajando

        }
    }

    private void runMujer() throws InterruptedException {
        System.out.printf("Soy %s %d.\n", hm, id);
        for (int i = 0; i < MAX_COUNT; i++) {
            System.out.printf("%s %d trabaja [%d/%d].\n", hm, id, (i + 1), MAX_COUNT);
            entra_baño(hm);
            Thread.sleep(100); //En el baño
            salir_baño();
            Thread.sleep(100); //Trabajando

        }
    }

    private void entra_baño(HombreMujer hm) throws InterruptedException {
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
        aforo++;
        System.out.printf("%s %d esta en el baño [%d/3].\n", hm, id, aforo);
        mutex.release();
    }

    private void salir_baño() throws InterruptedException {
        mutex.acquire();
        aforo--;
        System.out.printf("%s %d sale en el baño [%d/3].\n", hm, id, aforo);
        if (aforo == 0) {
            System.out.println("****BAÑO VACIO****");
            estadoBaño = HombreMujer.Ninguno;
        }
        permit = true;
        bañoVacio.release();
        mutex.release();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[THREADS];
        int i;

        for (i = 0; i < THREADS; i++) {
            if (i < THREADS / 2) {
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