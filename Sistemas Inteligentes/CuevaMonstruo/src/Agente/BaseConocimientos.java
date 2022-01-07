package Agente;

import IU.Tablero;
import java.util.Stack;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */
public class BaseConocimientos {

    //Estados de una casilla
    public static final int MONSTRUO = 0;
    public static final int PRECIPICIO = 1;
    public static final int RESPLANDOR = 2;
    public static final int HEDOR = 3;
    public static final int BRISA = 4;
    public static final int GOLPE = 5;
    //Valores de un estado de una casilla
    public static final int NO_INFO = 0;
    public static final int NO = 1;
    public static final int PUEDE = 2;
    public static final int SI = 3;

    int[][][] bC = new int[Tablero.DIMENSION][Tablero.DIMENSION][5];
    int[][] costes = new int[Tablero.DIMENSION][Tablero.DIMENSION];
    boolean[][][] paredes = new boolean[Tablero.DIMENSION][Tablero.DIMENSION][4];
    private Stack pilaAct = new Stack();

    /* añadirRegla
        Añade una regla a la base de conocimientos si ese conocimiento aporta
        informacion mas util al conocimiento ya obtenido en esa posicion, si ese
        conocimiento va a ser util para el proceso de inferencia se guarda en la
        pila.
     */
    public void añadirRegla(int posx, int posy, int regla, int value, boolean check) {
        if (inBounds(posx, posy)) {
            if (regla == GOLPE) {
                paredes[posx][posy][value] = true;
            } else if (!(value == PUEDE && (bC[posx][posy][regla] == SI || bC[posx][posy][regla] == NO))) {
                bC[posx][posy][regla] = value;
                if (check) {
                    pilaAct.push(posx);
                    pilaAct.push(posy);
                    pilaAct.push(regla);
                }
            }
            if (regla == MONSTRUO && value == NO) {
                if (inBounds(posx - 1, posy) && bC[posx - 1][posy][HEDOR] == SI) {
                    pilaAct.push(posx - 1);
                    pilaAct.push(posy);
                    pilaAct.push(HEDOR);
                }
                if (inBounds(posx + 1, posy) && bC[posx + 1][posy][HEDOR] == SI) {
                    pilaAct.push(posx + 1);
                    pilaAct.push(posy);
                    pilaAct.push(HEDOR);
                }
                if (inBounds(posx, posy - 1) && bC[posx][posy - 1][HEDOR] == SI) {
                    pilaAct.push(posx);
                    pilaAct.push(posy - 1);
                    pilaAct.push(HEDOR);
                }
                if (inBounds(posx, posy + 1) && bC[posx][posy + 1][HEDOR] == SI) {
                    pilaAct.push(posx);
                    pilaAct.push(posy + 1);
                    pilaAct.push(HEDOR);
                }
            } else if (regla == PRECIPICIO && value == NO) {
                if (inBounds(posx - 1, posy) && bC[posx - 1][posy][BRISA] == SI) {
                    pilaAct.push(posx - 1);
                    pilaAct.push(posy);
                    pilaAct.push(BRISA);
                }
                if (inBounds(posx + 1, posy) && bC[posx + 1][posy][BRISA] == SI) {
                    pilaAct.push(posx + 1);
                    pilaAct.push(posy);
                    pilaAct.push(BRISA);
                }
                if (inBounds(posx, posy - 1) && bC[posx][posy - 1][BRISA] == SI) {
                    pilaAct.push(posx);
                    pilaAct.push(posy - 1);
                    pilaAct.push(BRISA);
                }
                if (inBounds(posx, posy + 1) && bC[posx][posy + 1][BRISA] == SI) {
                    pilaAct.push(posx);
                    pilaAct.push(posy + 1);
                    pilaAct.push(BRISA);
                }
            }
        }
    }

    /* consecuencias
        Comprueba si se puede obtener nuevo conocimiento a partir del conocimiento
        ya obtenido, esto se hace mediante una pila donde se guardara aquel conocimiento
        que pueda servirnos para obtener concomiento nuevo.
     */
    public void consecuencias() {
        int posx, posy, regla;
        while (!pilaAct.isEmpty()) {
            regla = (int) pilaAct.pop();
            posy = (int) pilaAct.pop();
            posx = (int) pilaAct.pop();
            switch (regla) {
                case HEDOR:
                    if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][MONSTRUO] == PUEDE)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][MONSTRUO] == NO)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][MONSTRUO] == NO)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][MONSTRUO] == NO)) {
                        añadirRegla(posx - 1, posy, MONSTRUO, SI, false);
                    } else if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][MONSTRUO] == NO)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][MONSTRUO] == PUEDE)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][MONSTRUO] == NO)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][MONSTRUO] == NO)) {
                        añadirRegla(posx + 1, posy, MONSTRUO, SI, false);
                    } else if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][MONSTRUO] == NO)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][MONSTRUO] == NO)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][MONSTRUO] == PUEDE)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][MONSTRUO] == NO)) {
                        añadirRegla(posx, posy - 1, MONSTRUO, SI, false);
                    } else if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][MONSTRUO] == NO)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][MONSTRUO] == NO)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][MONSTRUO] == NO)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][MONSTRUO] == PUEDE)) {
                        añadirRegla(posx, posy + 1, MONSTRUO, SI, false);
                    }

                    if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][MONSTRUO] == NO)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][MONSTRUO] == NO)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][MONSTRUO] == NO)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][MONSTRUO] == NO)) {
                        añadirRegla(posx, posy, HEDOR, NO, false);
                    }
                    break;
                case BRISA:
                    if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][PRECIPICIO] == PUEDE)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][PRECIPICIO] == NO)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][PRECIPICIO] == NO)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][PRECIPICIO] == NO)) {
                        añadirRegla(posx - 1, posy, PRECIPICIO, SI, false);
                    } else if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][PRECIPICIO] == NO)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][PRECIPICIO] == PUEDE)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][PRECIPICIO] == NO)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][PRECIPICIO] == NO)) {
                        añadirRegla(posx + 1, posy, PRECIPICIO, SI, false);
                    } else if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][PRECIPICIO] == NO)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][PRECIPICIO] == NO)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][PRECIPICIO] == PUEDE)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][PRECIPICIO] == NO)) {
                        añadirRegla(posx, posy - 1, PRECIPICIO, SI, false);
                    } else if ((inBounds(posx - 1, posy) && bC[posx - 1][posy][PRECIPICIO] == NO)
                            && (inBounds(posx + 1, posy) && bC[posx + 1][posy][PRECIPICIO] == NO)
                            && (inBounds(posx, posy - 1) && bC[posx][posy - 1][PRECIPICIO] == NO)
                            && (inBounds(posx, posy + 1) && bC[posx][posy + 1][PRECIPICIO] == PUEDE)) {
                        añadirRegla(posx, posy + 1, PRECIPICIO, SI, false);
                    }
                    break;
                case MONSTRUO:
                    break;
                case PRECIPICIO:
                    break;
            }
        }
    }

    /* isOk
        Devuelve true si es seguro moverse a esa casilla.
     */
    public boolean isOk(int posx, int posy, int direccion) {
        boolean res = false;
        if (!paredes[posx][posy][direccion]) {
            switch (direccion) {
                case 0:
                    res = (bC[posx][posy + 1][MONSTRUO] == NO && bC[posx][posy + 1][PRECIPICIO] == NO);
                    break;
                case 1:
                    res = (bC[posx + 1][posy][MONSTRUO] == NO && bC[posx + 1][posy][PRECIPICIO] == NO);
                    break;
                case 2:
                    res = (bC[posx][posy - 1][MONSTRUO] == NO && bC[posx][posy - 1][PRECIPICIO] == NO);
                    break;
                case 3:
                    res = (bC[posx - 1][posy][MONSTRUO] == NO && bC[posx - 1][posy][PRECIPICIO] == NO);
                    break;
            }
        }
        return res;
    }

    /* inBounds
        Devuelve true si es posicion esta dentro de los limites del mapa.
     */
    private boolean inBounds(int posx, int posy) {
        boolean res = (posx >= 0 && posx < Tablero.DIMENSION && posy >= 0 && posy < Tablero.DIMENSION);
        return res;
    }

    /* getCostes
        Devuelve un array con los costes de las casillas adyacentes a la posicion dada
        devuelve MAX_INT si esa casilla esta fuera de limites del mapa.
     */
    public int[] getCostes(int posx, int posy) {
        int norte = inBounds(posx, posy + 1) ? costes[posx][posy + 1] : Integer.MAX_VALUE;
        int este = inBounds(posx + 1, posy) ? costes[posx + 1][posy] : Integer.MAX_VALUE;
        int sur = inBounds(posx, posy - 1) ? costes[posx][posy - 1] : Integer.MAX_VALUE;
        int oeste = inBounds(posx - 1, posy) ? costes[posx - 1][posy] : Integer.MAX_VALUE;
        int[] res = {norte, este, sur, oeste};
        return res;
    }

    /* addCostes
        Añade 1 al coste en la posicion dada.
     */
    public void addCostes(int posx, int posy) {
        costes[posx][posy]++;
    }

    /* resizeBc
        Agranda o acorta la dimension del array manteniendo los valores del 
        array antiguo, se agranda por la derecha y por abajo.
     */
    public void resizeBc(int dim) {
        //[JUGADOR, PRECIPICIO, TESORO, MONSTRUO]
        int oldDimension = bC.length - 1;
        int[][][] bC2 = new int[dim][dim][5];
        int[][] costes2 = new int[dim][dim];
        boolean[][][] paredes2 = new boolean[dim][dim][4];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i <= oldDimension && j <= oldDimension) {
                    if(!(i == oldDimension || j == oldDimension)){
                        paredes2[i][j] = paredes[i][j];
                    }
                    bC2[i][j] = bC[i][j];
                    costes2[i][j] = costes[i][j];
                }
            }
        }
        paredes = paredes2;
        bC = bC2;
        costes = costes2;
    }

    /* resizeBc
       Comprueba si hay un monstruo en la misma fila o columna a la posicion actual
        si lo hay lo mata y da las coordenadas del monstruo, si no devuelve -1, -1
     */
    public int[] matarMonstruo(int posx, int posy) {
        int[] res = new int[2];
        for (int i = 0; i < bC.length; i++) {
            if (bC[i][posy][MONSTRUO] == SI) {
                añadirRegla(i, posy, MONSTRUO, NO, false);
                res[0] = i;
                res[1] = posy;
                return res;
            }
        }
        for (int j = 0;
                j < bC.length;
                j++) {
            if (bC[posx][j][MONSTRUO] == SI) {
                añadirRegla(posx, j, MONSTRUO, NO, false);
                res[0] = posx;
                res[1] = j;
                return res;
            }
        }
        res[0] = -1;
        res[1] = -1;
        return res;
    }
}
