package Agente;

import IU.Tablero;
import java.util.Stack;

public class BaseConocimientos {

    //Estados de una casilla
    public static final int MONSTRUO = 0;
    public static final int PRECIPICIO = 1;
    public static final int RESPLANDOR = 2;
    public static final int HEDOR = 3;
    public static final int BRISA = 4;
    //Valores de un estado de una casilla
    public static final int NO_INFO = 0;
    public static final int NO = 1;
    public static final int PUEDE = 2;
    public static final int SI = 3;
    int[][][] bC = new int[Tablero.DIMENSION][Tablero.DIMENSION][5];
    int[][] costes = new int[Tablero.DIMENSION][Tablero.DIMENSION];
    private Stack pilaAct = new Stack();

    public void añadirRegla(int posx, int posy, int regla, int value, boolean check) {
        if (inBounds(posx, posy)) {
            if (!(value == PUEDE && (bC[posx][posy][regla] == SI || bC[posx][posy][regla] == NO))) {
                bC[posx][posy][regla] = value;
                if (check) {
                    pilaAct.push(posx);
                    pilaAct.push(posy);
                    pilaAct.push(regla);
                }
            } else if (regla == MONSTRUO && value == NO) {
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

    public boolean isOk(int posx, int posy) {
        boolean res = (inBounds(posx, posy) && bC[posx][posy][MONSTRUO] == NO && bC[posx][posy][PRECIPICIO] == NO);
        return res;
    }

    private boolean inBounds(int posx, int posy) {
        boolean res = (posx >= 0 && posx < Tablero.DIMENSION && posy >= 0 && posy < Tablero.DIMENSION);
        return res;
    }

    public int[] getCostes(int posx, int posy) {
        int norte = inBounds(posx, posy + 1) ? costes[posx][posy + 1] : Integer.MAX_VALUE;
        int este = inBounds(posx + 1, posy) ? costes[posx + 1][posy] : Integer.MAX_VALUE;
        int sur = inBounds(posx, posy - 1) ? costes[posx][posy - 1] : Integer.MAX_VALUE;
        int oeste = inBounds(posx - 1, posy) ? costes[posx - 1][posy] : Integer.MAX_VALUE;
        int[] res = {norte, este, sur, oeste};
        return res;
    }

    public void addCostes(int posx, int posy) {
        costes[posx][posy]++;
    }

    public void resizeBc(int dim) {
        //[JUGADOR, PRECIPICIO, TESORO, MONSTRUO]
        boolean[] estados;
        int oldDimension = bC.length - 1;
        int[][][] bC2 = new int[dim][dim][5];
        int[][] costes2 = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i <= oldDimension && j <= oldDimension) {
                    bC2[i][j] = bC[i][j];
                    costes2[i][j] = costes[i][j];
                }
            }
        }
        bC = bC2;
        costes = costes2;
    }
}
