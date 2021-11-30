package pasadizoestrecho;

import IU.Tablero;
import java.util.Arrays;

/**
 *
 * @author vicbl
 */
public class Agente {

    private boolean[] percepciones = new boolean[8];
    private boolean[] vecCaracteristicas = new boolean[6];
    private boolean[] lastVecCaracteristicas = new boolean[6];
    private ConjuntoAcciones lastAccion;

    public Agente() {
    }

    public boolean[] percibir(Tablero tbl) {
        percepciones = tbl.setPercepciones();
        return percepciones;
    }

    public boolean[] actVecCaracteristicas() {
        vecCaracteristicas = new boolean[6];
        if (percepciones[1]) {                      //Norte
            vecCaracteristicas[0] = true;
            //System.out.println("C 1:0");
        } //X1
        if (percepciones[3]) {                      //Este
            vecCaracteristicas[1] = true;
            //System.out.println("C 3:1");
        } //X2
        if (percepciones[5]) {                      //Sur
            vecCaracteristicas[2] = true;
            //System.out.println("C 5:2");
        } //X3
        if (percepciones[7]) {                      //Oeste
            vecCaracteristicas[3] = true;
            //System.out.println("C 7:3");
        } //X4
        if (percepciones[0] || percepciones[2] //Se encarga del pattern 'x'
                || percepciones[4] || percepciones[6]) {
            vecCaracteristicas[4] = true;
            //System.out.println("C 4");
        }  //X5
        if (percepciones[1] || percepciones[3] //Se encarga del pattern '+'
                || percepciones[5] || percepciones[7]) {
            vecCaracteristicas[5] = true;
            //System.out.println("C 5");
        } //X6
        return vecCaracteristicas;
    }

    public ConjuntoAcciones efecAccion(Tablero tbl) {
        ConjuntoAcciones accion = null;
        if (lastAccion != null) {
            accion = lastAccion;
        }
        //printCaracteristicas(lastVecCaracteristicas);
        if (lastAccion == ConjuntoAcciones.ESTE && lastVecCaracteristicas[5]) { //V //"Si fui al este y ya estaba pegado a una pared"
            System.out.println("Fui al este y ya estaba pegado a una pared");
            if (vecCaracteristicas[0]) {
                if (vecCaracteristicas[1]) {
                    if (vecCaracteristicas[2]) {
                        accion = ConjuntoAcciones.OESTE;
                    } else {
                        accion = ConjuntoAcciones.SUR;
                    }
                } else {
                    accion = ConjuntoAcciones.ESTE;
                }
            } else {
                accion = ConjuntoAcciones.NORTE;
            }
        } else if (lastAccion == ConjuntoAcciones.SUR && lastVecCaracteristicas[5]) { //V //"Si fui al Sur y ya estaba pegado a una pared"
            System.out.println("Fui al Sur y ya estaba pegado a una pared");
            if (vecCaracteristicas[1]) {
                if (vecCaracteristicas[2]) {
                    if (vecCaracteristicas[3]) {
                        accion = ConjuntoAcciones.NORTE;
                    } else {
                        accion = ConjuntoAcciones.OESTE;
                    }
                } else {
                    accion = ConjuntoAcciones.SUR;
                }
            } else {
                accion = ConjuntoAcciones.ESTE;
            }
        } else if (lastAccion == ConjuntoAcciones.OESTE && lastVecCaracteristicas[5]) { //V //"Si fui al oeste y ya estaba pegado a una pared"
            System.out.println("Fui al Oeste y ya estaba pegado a una pared");
            if (vecCaracteristicas[2]) {
                if (vecCaracteristicas[3]) {
                    if (vecCaracteristicas[0]) {
                        accion = ConjuntoAcciones.ESTE;
                    } else {
                        accion = ConjuntoAcciones.NORTE;
                    }
                } else {
                    accion = ConjuntoAcciones.OESTE;
                }
            } else {
                accion = ConjuntoAcciones.SUR;
            }
        } else if (lastAccion == ConjuntoAcciones.NORTE && lastVecCaracteristicas[5]) { //V //"Si fui al Norte y ya estaba pegado a una pared"
            System.out.println("fui al Norte y ya estaba pegado a una pared");
            if (vecCaracteristicas[3]) {
                if (vecCaracteristicas[0]) {
                    if (vecCaracteristicas[1]) {
                        accion = ConjuntoAcciones.SUR;
                    } else {
                        accion = ConjuntoAcciones.ESTE;
                    }
                } else {
                    accion = ConjuntoAcciones.NORTE;
                }
            } else {
                accion = ConjuntoAcciones.OESTE;
            }
        } else if (vecCaracteristicas[5] && lastVecCaracteristicas[4] && !lastVecCaracteristicas[5]) { //"Se encarga de la accion posterior a una rotacion"
            System.out.println("Mi accion anterior fue una rotacion");
            if (lastAccion == ConjuntoAcciones.OESTE) {
                if (vecCaracteristicas[3]) {
                    if (vecCaracteristicas[2]) {
                        accion = ConjuntoAcciones.ESTE;
                    } else {
                        accion = ConjuntoAcciones.SUR;
                    }
                } else {
                    accion = ConjuntoAcciones.OESTE;
                }
            } else if (lastAccion == ConjuntoAcciones.ESTE) {
                if (vecCaracteristicas[1]) {
                    if (vecCaracteristicas[0]) {
                        accion = ConjuntoAcciones.OESTE;
                    } else {
                        accion = ConjuntoAcciones.NORTE;
                    }
                } else {
                    accion = ConjuntoAcciones.ESTE;
                }
            } else if (lastAccion == ConjuntoAcciones.NORTE) {
                if (vecCaracteristicas[0]) {
                    if (vecCaracteristicas[3]) {
                        accion = ConjuntoAcciones.SUR;
                    } else {
                        accion = ConjuntoAcciones.OESTE;
                    }
                } else {
                    accion = ConjuntoAcciones.NORTE;
                }
            } else if (lastAccion == ConjuntoAcciones.SUR) {
                if (vecCaracteristicas[2]) {
                    if (vecCaracteristicas[1]) {
                        accion = ConjuntoAcciones.NORTE;
                    } else {
                        accion = ConjuntoAcciones.ESTE;
                    }
                } else {
                    accion = ConjuntoAcciones.SUR;
                }
            }
        } 
        else {
            System.out.println("Accion por defecto");
            if (vecCaracteristicas[5]) {
                if (vecCaracteristicas[0]) {
                    if (vecCaracteristicas[1]) {
                        accion = ConjuntoAcciones.SUR;
                    } else {
                        accion = ConjuntoAcciones.ESTE;
                    }
                } else if (vecCaracteristicas[1]) {
                    accion = ConjuntoAcciones.SUR;
                } else if (vecCaracteristicas[3]) {
                    accion = ConjuntoAcciones.NORTE;
                }
            } else {
                accion = ConjuntoAcciones.NORTE;
            }
        }
        tbl.moverPlayer(accion);
        lastVecCaracteristicas = vecCaracteristicas;
        lastAccion = accion;
        System.out.println();
        return accion;
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
}
