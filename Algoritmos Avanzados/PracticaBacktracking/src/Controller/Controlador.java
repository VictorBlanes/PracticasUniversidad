package Controller;

import Model.Data;
import Model.Piezas.Pieza;
import View.Vista;
import java.util.ArrayList;
import java.util.Arrays;
import practicaBacktracking.PerEsdeveniments;
import practicaBacktracking.PracticaBacktracking;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */
public class Controlador extends Thread implements PerEsdeveniments {

    private static PracticaBacktracking main;
    private static Vista vista;
    private static Data modelo;
    boolean calculoEnProgreso = false;

    public Controlador(PracticaBacktracking main, int dimensionTablero) {
        Controlador.main = main;
        Controlador.modelo = new Data(this, dimensionTablero);
        Controlador.vista = new Vista(this);
    }

    private void caminoHamiltoniano() {
        int[] posPieza;
        ArrayList<Pieza> piezas;
        boolean[][] posChecked;
        int lugaresLibresRestantes;
        if (modelo.hayPiezasEnTablero()) {
            piezas = modelo.getPiezasEnTablero();
            posChecked = new boolean[modelo.getDimensionTablero()][modelo.getDimensionTablero()];
            lugaresLibresRestantes = modelo.getDimensionTablero() * modelo.getDimensionTablero();
            for (int i = 0; i < piezas.size(); i++) {
                posPieza = Arrays.copyOf(piezas.get(i).getPosPieza(), piezas.get(i).getPosPieza().length);
                posChecked[posPieza[0]][posPieza[1]] = true;
                piezas.get(i).getRecorrido().add(posPieza);
                lugaresLibresRestantes--;
            }
            notificar("Mensaje|Calculando...");
            double start = System.nanoTime();
            int res = trueCaminoHamiltoniano(0, piezas, lugaresLibresRestantes, posChecked);
            double end = System.nanoTime();
            if (calculoEnProgreso) {
                modelo.setTiempoEjecucion((end - start) / 1000000000.0);
                notificar("Mensaje|Calculo finalizado, tiempo tardado: " + modelo.getTiempoEjecucion() + " segundos.");
                if (res == 0) {
                    mostrarRecorrido();
                } else {
                    notificar("Ventana Error|Error Calculo|No se ha encontrado ninguna solucion");
                }
            }
        } else {
            notificar("Ventana Error|Error Calculo|No hay pieza en el tablero");
        }
    }

    private int trueCaminoHamiltoniano(int turno, ArrayList<Pieza> piezas, int lugaresLibres, boolean[][] lugaresVisitados) {
        int newLugaresLibres;
        int[] posPieza;
        Pieza currentPieza;
        if (calculoEnProgreso) {
            currentPieza = piezas.get(turno % piezas.size());
            posPieza = Arrays.copyOf(currentPieza.getPosPieza(), currentPieza.getPosPieza().length);
            if (lugaresLibres == 0) {
                return 0;
            }
            int[][] movPosibles = currentPieza.getMovimientos();
            for (int x = 0; x < movPosibles.length; x++) {
                int[] newPos = {posPieza[0] + movPosibles[x][0], posPieza[1] + movPosibles[x][1]};
                if (posValida(newPos)) {
                    if (!lugaresVisitados[newPos[0]][newPos[1]]) {
                        newLugaresLibres = lugaresLibres - 1;
                        currentPieza.getRecorrido().add(newPos);
                        lugaresVisitados[newPos[0]][newPos[1]] = true;
                        currentPieza.setPosPieza(newPos[0], newPos[1]);
                        newLugaresLibres = trueCaminoHamiltoniano(turno + 1, piezas, newLugaresLibres, lugaresVisitados);
                        if (newLugaresLibres == 0) {
                            return 0;
                        } else {
                            lugaresVisitados[newPos[0]][newPos[1]] = false;
                            currentPieza.getRecorrido().remove(currentPieza.getRecorrido().size() - 1);
                            if (currentPieza.getRecorrido().isEmpty()) {
                                lugaresVisitados[newPos[0]][newPos[1]] = false;
                            }
                        }
                    }
                }
            }
            return lugaresLibres;
        } else {
            return -1;
        }
    }

    private void mostrarRecorrido() {
        ArrayList<Pieza> piezasEnTablero = modelo.getPiezasEnTablero();
        for (int i = 0; i < piezasEnTablero.size(); i++) {
            vista.setRecorridoPieza(piezasEnTablero.get(i));
        }
        notificar("Activar Recorrido");
    }

    private boolean posValida(int[] newPos) {
        if (newPos[0] < 0 || newPos[0] >= modelo.getDimensionTablero()) {
            return false;
        }

        if (newPos[1] < 0 || newPos[1] >= modelo.getDimensionTablero()) {
            return false;
        }

        return true;
    }

    private void resetPiezas() {
        ArrayList<Pieza> piezas = modelo.getPiezasEnTablero();
        Pieza pieza;
        int[] startingPos;
        for (int i = 0; i < piezas.size(); i++) {
            pieza = piezas.get(i);
            startingPos = pieza.getStartingPos();
            pieza.newRecorrido();
            pieza.setPosPieza(startingPos[0], startingPos[1]);
        }
    }

    public int getDimensionTablero() {
        return modelo.getDimensionTablero();
    }

    public void setPiezaTablero(Pieza pieza) {
        modelo.addPiezaEnTablero(pieza);
    }

    public void borrarPieza(Pieza pieza) {
        modelo.getPiezasEnTablero().remove(pieza);
    }

    @Override
    public void run() {
        calculoEnProgreso = true;
        caminoHamiltoniano();
        calculoEnProgreso = false;
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Ejecutar Calculo")) {
            if (!calculoEnProgreso) {
                calculoEnProgreso = true;
                resetPiezas();
                new Thread(this).start();
            } else {
                notificar("Mensaje|Ya hay un calculo en progreso!");
            }
        } else if (s.startsWith("Parar Ejecucion")) {
            if (calculoEnProgreso) {
                calculoEnProgreso = false;
                notificar("Mensaje|Calculo detenido");
            } else {
                notificar("Mensaje|No hay ningun calculo en proceso.");
            }
        } else if (s.startsWith("Activar Recorrido") || (s.startsWith("Quitar Pieza"))
                || (s.startsWith("Ventana Error")) || (s.startsWith("Mensaje"))) {
            vista.notificar(s);
        } else if (s.startsWith("Eliminar Recorrido")) {
            resetPiezas();
        } else if (s.startsWith("Cambiar Dimension")) {
            String[] res = s.split("\\|");
            modelo.setDimensionTablero(Integer.valueOf(res[1]));
        }
    }
}
