package cuevamonstruo;

import IU.Tablero;

/**
 *
 * @author Victor Manuel Blanes Castro
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
    /* actVecCaracteristicas
        A partir de las percepciones del agente actualiza el vector de caractersiticas.
    */
    public boolean[] actVecCaracteristicas() {
        vecCaracteristicas = new boolean[6];
        if (percepciones[1]) { //X1 - Norte
            vecCaracteristicas[0] = true;
        }
        if (percepciones[3]) { //X2 - Este
            vecCaracteristicas[1] = true;
        }
        if (percepciones[5]) { //X3 - Sur
            vecCaracteristicas[2] = true;
        }
        if (percepciones[7]) { //X4 - Oeste
            vecCaracteristicas[3] = true;
        }
        if (percepciones[0] || percepciones[2] //X5 - Pattern 'x'
                || percepciones[4] || percepciones[6]) {
            vecCaracteristicas[4] = true;
        }
        if (percepciones[1] || percepciones[3] //X6 - Pattern '+'
                || percepciones[5] || percepciones[7]) {
            vecCaracteristicas[5] = true;
        }
        return vecCaracteristicas;
    }
    
    /* efecAccion
        A partir del vector de caracteristicas y la accion anterior elige 
        que accion tomar.
    */
    public ConjuntoAcciones efecAccion(Tablero tbl) {
        ConjuntoAcciones accion = null;
        if (lastAccion == ConjuntoAcciones.ESTE && lastVecCaracteristicas[5]) {
            // Por defecto intenta continuar en la misma direccion que la anterior, 
            // si no puede va comprobando si puede rotar en sentido antihorario
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
        } else if (lastAccion == ConjuntoAcciones.SUR && lastVecCaracteristicas[5]) {
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
        } else if (lastAccion == ConjuntoAcciones.OESTE && lastVecCaracteristicas[5]) {
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
        } else if (lastAccion == ConjuntoAcciones.NORTE && lastVecCaracteristicas[5]) {
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
            // Reevalua la direccion que tiene que seguir en el caso posterior a un cambio de direccion
            // Por defecto intenta continuar en la misma direccion que habia tomado
            // Si no puede, intenta girar en sentido antihorario
            if (lastAccion == ConjuntoAcciones.OESTE) {
                if (vecCaracteristicas[3]) {
                    if (vecCaracteristicas[0]) {
                        accion = ConjuntoAcciones.ESTE;
                    } else {
                        accion = ConjuntoAcciones.NORTE;
                    }
                } else {
                    accion = ConjuntoAcciones.OESTE;
                }
            } else if (lastAccion == ConjuntoAcciones.ESTE) {
                if (vecCaracteristicas[1]) {
                    if (vecCaracteristicas[2]) {
                        accion = ConjuntoAcciones.OESTE;
                    } else {
                        accion = ConjuntoAcciones.SUR;
                    }
                } else {
                    accion = ConjuntoAcciones.ESTE;
                }
            } else if (lastAccion == ConjuntoAcciones.NORTE) {
                if (vecCaracteristicas[0]) {
                    if (vecCaracteristicas[1]) {
                        accion = ConjuntoAcciones.SUR;
                    } else {
                        accion = ConjuntoAcciones.ESTE;
                    }
                } else {
                    accion = ConjuntoAcciones.NORTE;
                }
            } else if (lastAccion == ConjuntoAcciones.SUR) {
                if (vecCaracteristicas[2]) {
                    if (vecCaracteristicas[3]) {
                        accion = ConjuntoAcciones.NORTE;
                    } else {
                        accion = ConjuntoAcciones.OESTE;
                    }
                } else {
                    accion = ConjuntoAcciones.SUR;
                }
            }
        } 
        else {
            // Accion por defecto, por defecto el agente ira norte, si no 
            // puede ir norte intentara girar en sentido horario
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
