package View;

import Model.Piezas.Pieza;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import practicaBacktracking.PerEsdeveniments;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */
public class Tablero extends JPanel implements PerEsdeveniments {

    private Vista vista;
    private final int PHEIGHT = 900;
    private final int PWIDTH = 900;
    private Casilla[][] casillas;

    public Tablero(Vista vista) {
        this.vista = vista;
        new Casilla(vista);
        initCasillas();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int dimensionTablero = vista.getDimensionTablero();
        removeAll();
        this.setLayout(new GridLayout(dimensionTablero, dimensionTablero));
        for (int i = 0; i < dimensionTablero; i++) {
            for (int j = 0; j < dimensionTablero; j++) {
                this.add(casillas[j][i]);
            }
        }
        revalidate();
    }

    public void initCasillas() {
        int newDim = vista.getDimensionTablero();
        int dimX = PWIDTH / newDim;
        int dimY = PHEIGHT / newDim;
        Color fondo;
        Casilla[][] newCasilla = new Casilla[newDim][newDim];
        if (casillas == null) {
            for (int i = 0; i < newDim; i++) {
                for (int j = 0; j < newDim; j++) {
                    fondo = (i + j) % 2 == 0 ? new Color(0xAF7C59) : new Color(0xF4EEAA);
                    newCasilla[j][i] = new Casilla(j, i, PWIDTH / newDim, PHEIGHT / newDim, null, fondo);
                }
            }
        } else {
            int oldDim = casillas.length;
            if (newDim <= oldDim) {
                for (int i = 0; i < newDim; i++) {
                    for (int j = 0; j < newDim; j++) {
                        newCasilla[j][i] = casillas[j][i];
                        newCasilla[j][i].setDims(dimX, dimY);
                    }
                }
            } else if (newDim > casillas.length) {
                for (int i = 0; i < newDim; i++) {
                    for (int j = 0; j < newDim; j++) {
                        if (j < oldDim && i < oldDim) {
                            newCasilla[j][i] = casillas[j][i];
                            newCasilla[j][i].setDims(dimX, dimY);
                        } else {
                            fondo = (i + j) % 2 == 0 ? new Color(0xAF7C59) : new Color(0xF4EEAA);
                            newCasilla[j][i] = new Casilla(j, i, PWIDTH / newDim, PHEIGHT / newDim, null, fondo);
                        }
                    }
                }
            }
        }
        casillas = newCasilla;
    }

    void setRecorridoPieza(Pieza pieza) {
        ArrayList<int[]> recorrido = pieza.getRecorrido();
        for(int i = 0; i < recorrido.size(); i++){
            casillas[recorrido.get(i)[0]][recorrido.get(i)[1]].setColorRecorrido(pieza.getColor());
            casillas[recorrido.get(i)[0]][recorrido.get(i)[1]].setPosRecorrido(i);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PWIDTH, PHEIGHT);
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Repintar Tablero")) {
            initCasillas();
            this.repaint();
        } else if (s.startsWith("Activar Recorrido")) {
            Casilla.setRecorridoActivado(true);
            initCasillas();
            this.repaint();
        } else if (s.startsWith("Desactivar Recorrido")) {
            if (Casilla.isRecorridoActivado()) {
                Casilla.setRecorridoActivado(false);
                initCasillas();
                this.repaint();
                vista.notificar("Mensaje|Recorrido desactivado");
            }else{
                vista.notificar("Ventana Error|Error|No hay nignun recorrido activado");
            }
        } else if (s.startsWith("Quitar Pieza")) {
            String[] res = s.split("\\|");
            int x = Integer.valueOf(res[1]);
            int y = Integer.valueOf(res[2]);
            casillas[x][y].borrarFicha();
        }
    }

}
