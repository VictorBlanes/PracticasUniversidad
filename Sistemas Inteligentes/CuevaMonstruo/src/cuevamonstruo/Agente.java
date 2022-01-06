package cuevamonstruo;

import IU.Tablero;
import Agente.BaseConocimientos;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor Manuel Blanes Castro
 */
public class Agente {

    private boolean[] percepciones = new boolean[4];
    private boolean[] vecCaracteristicas = new boolean[6];
    private boolean[] lastVecCaracteristicas = new boolean[6];
    private ConjuntoAcciones lastAccion;
    private int posx, posy;
    private BaseConocimientos bc = new BaseConocimientos();
    static Semaphore mutex = new Semaphore(0);
    static boolean auto = false;

    public Agente() {
        posx = 0;
        posy = 0;
    }

//  [Hedor, Brisa, Resplandor, Golpe]
    public boolean[] percibir(Tablero tbl) {
        int i = (Tablero.DIMENSION - 1) - posy;
        int j = posx;
        percepciones = tbl.setPercepciones(i, j);
        printPercepciones();
        reglasJuego();
        return percepciones;
    }

    /* actVecCaracteristicas
        A partir de las percepciones del agente actualiza el vector de caractersiticas.
     */
    public boolean[] reglasJuego() {
        if (percepciones[Tablero.HEDOR]) {
            bc.añadirRegla(posx, posy, bc.HEDOR, bc.SI, false);
            bc.añadirRegla(posx + 1, posy, bc.MONSTRUO, bc.PUEDE, true);
            bc.añadirRegla(posx - 1, posy, bc.MONSTRUO, bc.PUEDE, true);
            bc.añadirRegla(posx, posy + 1, bc.MONSTRUO, bc.PUEDE, true);
            bc.añadirRegla(posx, posy - 1, bc.MONSTRUO, bc.PUEDE, true);
        } else {
            bc.añadirRegla(posx, posy, bc.HEDOR, bc.NO, false);
            bc.añadirRegla(posx + 1, posy, bc.MONSTRUO, bc.NO, false);
            bc.añadirRegla(posx - 1, posy, bc.MONSTRUO, bc.NO, false);
            bc.añadirRegla(posx, posy + 1, bc.MONSTRUO, bc.NO, false);
            bc.añadirRegla(posx, posy - 1, bc.MONSTRUO, bc.NO, false);
        }
        if (percepciones[Tablero.BRISA]) {
            bc.añadirRegla(posx, posy, bc.BRISA, bc.SI, false);
            bc.añadirRegla(posx + 1, posy, bc.PRECIPICIO, bc.PUEDE, true);
            bc.añadirRegla(posx - 1, posy, bc.PRECIPICIO, bc.PUEDE, true);
            bc.añadirRegla(posx, posy + 1, bc.PRECIPICIO, bc.PUEDE, true);
            bc.añadirRegla(posx, posy - 1, bc.PRECIPICIO, bc.PUEDE, true);
        } else {
            bc.añadirRegla(posx, posy, bc.BRISA, bc.NO, false);
            bc.añadirRegla(posx + 1, posy, bc.PRECIPICIO, bc.NO, false);
            bc.añadirRegla(posx - 1, posy, bc.PRECIPICIO, bc.NO, false);
            bc.añadirRegla(posx, posy + 1, bc.PRECIPICIO, bc.NO, false);
            bc.añadirRegla(posx, posy - 1, bc.PRECIPICIO, bc.NO, false);
        }
        if (percepciones[Tablero.RESPLANDOR]) {
            bc.añadirRegla(posx, posy, bc.RESPLANDOR, bc.SI, false);
        } else {
            bc.añadirRegla(posx, posy, bc.RESPLANDOR, bc.NO, false);
        }
        bc.añadirRegla(posx, posy, bc.MONSTRUO, bc.NO, false);
        bc.añadirRegla(posx, posy, bc.PRECIPICIO, bc.NO, false);
        bc.consecuencias();
        return vecCaracteristicas;
    }

    /* efecAccion
        A partir del vector de caracteristicas y la accion anterior elige 
        que accion tomar.
     */
    public boolean efecAccion(ConjuntoAcciones returnAccion, Tablero tbl, int x, int y) {
        percibir(tbl);
        for (ConjuntoAcciones acc : ConjuntoAcciones.values()) {
            if (acc != returnAccion) {
                try {
                    if (auto) {
                        Thread.sleep(150);
                    } else {
                        mutex.acquire();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.printf("Pre %d / %d\n", x, y);
                System.out.println(acc);
                switch (acc) {
                    case NORTE:
                        if (bc.isOk(x, y + 1)) {
                            posx = x;
                            posy = y + 1;
                            System.out.printf("Post %d / %d\n", posx, posy);
                            tbl.moverPlayer(acc);
                            tbl.repaint();
                            efecAccion(acc.SUR, tbl, posx, posy);
                        } else if (!auto) {
                            mutex.release();
                        }
                        break;
                    case SUR:
                        if (bc.isOk(x, y - 1)) {
                            posx = x;
                            posy = y - 1;
                            System.out.printf("Post %d / %d\n", posx, posy);
                            tbl.moverPlayer(acc);
                            tbl.repaint();
                            efecAccion(acc.NORTE, tbl, posx, posy);
                        } else if (!auto) {
                            mutex.release();
                        }
                        break;
                    case ESTE:
                        if (bc.isOk(x + 1, y)) {
                            posx = x + 1;
                            posy = y;
                            System.out.printf("Post %d / %d\n", posx, posy);
                            tbl.moverPlayer(acc);
                            tbl.repaint();
                            efecAccion(acc.OESTE, tbl, posx, posy);
                        } else if (!auto) {
                            mutex.release();
                        }
                        break;
                    case OESTE:
                        if (bc.isOk(x - 1, y)) {
                            posx = x - 1;
                            posy = y;
                            System.out.printf("Post %d / %d\n", posx, posy);
                            tbl.moverPlayer(acc);
                            tbl.repaint();
                            efecAccion(acc.ESTE, tbl, posx, posy);
                        } else if (!auto) {
                            mutex.release();
                        }
                        break;
                }
            }
        }
        if (returnAccion != null) {

            tbl.moverPlayer(returnAccion);
            tbl.repaint();
            try {
                if (!auto) {
                    mutex.acquire();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("No hay mas acciones posibles.");
        }

        return true;
    }

    public void printPercepciones() {
        int i = 0;
        System.out.print("[");
        for (i = 0; i < percepciones.length - 1; i++) {
            System.out.print(percepciones[i] + ", ");
        }
        System.out.print(percepciones[i]);
        System.out.println("]");
    }

    public void printCaracteristicas(boolean[] vec) {
        int i = 0;
        System.out.print("[");
        for (i = 0; i < vec.length - 1; i++) {
            System.out.print(vec[i] + ", ");
        }
        System.out.print(vec[i]);
        System.out.println("]");
    }

    private boolean contains(boolean[] b, boolean mark) {
        for (int i = 0; i < b.length; i++) {
            if (b[i] == mark) {
                return true;
            }
        }
        return false;
    }

    public void nextAccion() {
        mutex.release();
    }

    public void startStop() {
        auto = !auto;
        if (mutex.hasQueuedThreads()) {
            mutex.release();
        }
    }
}
