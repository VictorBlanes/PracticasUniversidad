package Agente;

import IU.Tablero;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor Manuel Blanes Castro
 */
public class Agente {

    private boolean[] percepciones = new boolean[4];
    private int posx, posy;
    private BaseConocimientos bc = new BaseConocimientos();
    static Semaphore mutex = new Semaphore(0); //Barrera
    static boolean auto = false; //Control de movimiento automatico del robot
    private int num_flechas;

    public Agente() {
        posx = 0;
        posy = 0;
    }

    /* percibir
        Llama a percibir de la clase Tablero que es donde se ponen las percepciones
        Cambia de coordenadas Agente a coordendas Tablero (Ya que 0,0 esta en
        lugares distintos y las coordenadas van diferentes)
        Inicio tambien el ciclo que actualizara todas las reglas.
     */
    public boolean[] percibir(Tablero tbl) {
        int i = (Tablero.DIMENSION - 1) - posy;
        int j = posx;
        percepciones = tbl.setPercepciones(i, j);
        reglasJuegoActualizacion();
        return percepciones;
    }

    /* reglasJuegoActualizacion
        A partir de las percepciones añade todas las nuevas reglas posibles y
        actualiza antiguas, si es ncesario.
     */
    public void reglasJuegoActualizacion() {
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
    }

    /* efecAccion
        Proceso recursivo, el metodo ira elegiendo el movimiento mas apropiado 
        hasta que encuentre el tesoro, si no hay tesoro el agente ira dando vueltas
        infinitamente
     */
    public boolean efecAccion(Tablero tbl, int x, int y) {
        boolean res = false;
        percibir(tbl);
        if (percepciones[2]) {
            return true;
        }
        int[] costes = bc.getCostes(x, y);
        for (int i = 0; i < costes.length; i++) {
            int iacc = chooseLowest(costes);
            if (iacc < Integer.MAX_VALUE) {
                ConjuntoAcciones acc = ConjuntoAcciones.values()[iacc];
                try {
                    if (auto) {
                        Thread.sleep(150);
                    } else {
                        mutex.acquire();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
                }
                int[] resMon = checkDisparo(x, y);
                if (resMon[0] != -1) {
                    percibir(tbl);
                    int posx = (Tablero.DIMENSION - 1) - resMon[1];
                    int posy = resMon[0];
                    tbl.setEspecificoEstadoCasilla(posx, posy, 3);
                    tbl.repaint();
                    i--;
                } else {
                    switch (acc) {
                        case NORTE:
                            bc.addCostes(x, y + 1);
                            costes[iacc] = Integer.MAX_VALUE;
                            if (bc.isOk(x, y + 1)) {
                                posx = x;
                                posy = y + 1;
                                tbl.moverPlayer(acc);
                                tbl.repaint();
                                res = efecAccion(tbl, posx, posy);
                                if (res) {
                                    return true;
                                }
                            } else if (!auto) {
                                mutex.release();
                            }
                            break;
                        case SUR:
                            bc.addCostes(x, y - 1);
                            costes[iacc] = Integer.MAX_VALUE;
                            if (bc.isOk(x, y - 1)) {
                                posx = x;
                                posy = y - 1;
                                tbl.moverPlayer(acc);
                                tbl.repaint();
                                res = efecAccion(tbl, posx, posy);
                                if (res) {
                                    return true;
                                }
                            } else if (!auto) {
                                mutex.release();
                            }
                            break;
                        case ESTE:
                            bc.addCostes(x + 1, y);
                            costes[iacc] = Integer.MAX_VALUE;
                            if (bc.isOk(x + 1, y)) {
                                posx = x + 1;
                                posy = y;
                                tbl.moverPlayer(acc);
                                tbl.repaint();
                                res = efecAccion(tbl, posx, posy);
                                if (res) {
                                    return true;
                                }
                            } else if (!auto) {
                                mutex.release();
                            }
                            break;
                        case OESTE:
                            bc.addCostes(x - 1, y);
                            costes[iacc] = Integer.MAX_VALUE;
                            if (bc.isOk(x - 1, y)) {
                                posx = x - 1;
                                posy = y;
                                tbl.moverPlayer(acc);
                                tbl.repaint();
                                res = efecAccion(tbl, posx, posy);
                                if (res) {
                                    return true;
                                }
                            } else if (!auto) {
                                mutex.release();
                            }
                            break;
                    }
                }
            }
        }
        return false;
    }

    /* printPercepciones
        Muestra por consola las percepciones mas recientes.
     */
    public void printPercepciones() {
        int i = 0;
        System.out.print("[");
        for (i = 0; i < percepciones.length - 1; i++) {
            System.out.print(percepciones[i] + ", ");
        }
        System.out.print(percepciones[i]);
        System.out.println("]");
    }

    /* nextAccion
        Da un permiso al mutex(Que usamos como barrera) que controla la ejecucion
        del robot cuando este esta en modo Step.
     */
    public void nextAccion() {
        mutex.release();
    }

    /* startStop
        Inicio o para la ejecucion automatica del robot. (Modo Auto)
     */
    private int[] checkDisparo(int x, int y) {
        return bc.matarMonstruo(x, y);
    }

    public void startStop() {
        auto = !auto;
        if (mutex.hasQueuedThreads()) {
            mutex.release();
        }
    }

    /* chooseLowest
        Dada un array devuelve el indice donde se encuentra el valor mas bajo de
        dicho array.
     */
    private int chooseLowest(int[] arr) {
        int i = 0, res = -1, lowest = Integer.MAX_VALUE;
        for (i = 0; i < arr.length; i++) {
            if (arr[i] < lowest) {
                res = i;
                lowest = arr[i];
            }
        }
        return res;
    }

    /* resizeBc
        Llama al metodo para cambiar el tamaño de la base de Conocimientos y pone
        al robot en 0,0
     */
    public void resizeBc(int Dim) {
        bc.resizeBc(Dim);
        posx = 0;
        posy = 0;
    }

    public void setFlechas(int flechas) {
        this.num_flechas = flechas;
    }
}
