package IU;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import cuevamonstruo.ConjuntoAcciones;

/**
 *
 * @author Victor Manuel Blanes Castro
 */
public class Tablero extends JPanel {

    public static int DIMENSION = 5;

    private static final int MAXIMO = 600;
    private static int LADO = MAXIMO / DIMENSION;
    private static final Color BLANCO = new Color(242, 242, 242);
    private static final Color NEGRO = new Color(230, 230, 230);
    private Casilla t[][];
    private boolean jugadorEnMapa = false;

    private static final int JUGADOR = 0;
    private static final int PRECIPICIO = 1;
    private static final int TESORO = 2;
    private static final int MONSTRUO = 3;

    public static final int HEDOR = 0;
    public static final int BRISA = 1;
    public static final int RESPLANDOR = 2;
    public static final int GOLPE = 3;

    public Tablero() {
        new Casilla();
        boolean[] estados;
        t = new Casilla[DIMENSION][DIMENSION];
        int y = 0;
        for (int i = 0; i < DIMENSION; i++) {
            int x = 0;
            for (int j = 0; j < DIMENSION; j++) {
                Rectangle2D.Float r = new Rectangle2D.Float(x, y, LADO, LADO);
                Color col;
                if ((i % 2 == 1 && j % 2 == 1) || (i % 2 == 0 && j % 2 == 0)) {
                    col = BLANCO;
                } else {
                    col = NEGRO;
                }
                estados = new boolean[4];
                t[i][j] = new Casilla(r, col, estados);
                x += LADO;
            }
            y += LADO;
        }
    }

    public void resizeArray(int dim) {
        //[JUGADOR, PRECIPICIO, TESORO, MONSTRUO]
        boolean[] estados;
        int oldDimension = DIMENSION - 1;
        DIMENSION = dim;
        LADO = MAXIMO / DIMENSION;
        boolean player = false;
        Casilla t2[][] = new Casilla[DIMENSION][DIMENSION];
        int y = 0;
        for (int i = 0; i < DIMENSION; i++) {
            int x = 0;
            for (int j = 0; j < DIMENSION; j++) {
                Rectangle2D.Float r = new Rectangle2D.Float(x, y, LADO, LADO);
                Color col;
                if ((i % 2 == 1 && j % 2 == 1) || (i % 2 == 0 && j % 2 == 0)) {
                    col = BLANCO;
                } else {
                    col = NEGRO;
                }
                if (i < oldDimension && j < oldDimension) {
                    if (t[i][j].getEstadoCasilla()[0]) {
                        player = true;
                    }
                    t2[i][j] = new Casilla(r, col, t[i][j].getEstadoCasilla());
                } else {
                    estados = new boolean[4];
                    t2[i][j] = new Casilla(r, col, estados);
                }
                x += LADO;
            }
            y += LADO;
        }
        t = t2;
        jugadorEnMapa = player;
    }

    @Override
    public void paintComponent(Graphics g) {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                t[i][j].paintComponent(g);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAXIMO, MAXIMO);
    }

    public boolean esCasilla(int i, int j, int x, int y) {
        return t[i][j].getRec().contains(x, y);
    }

    public boolean estaOcupado(int i, int j) {
        return t[i][j].getEstadoCasilla()[0];
    }

    public void setPlayer(int i, int j) {
        boolean[] estado = {false, false, false, false};
        if (!contains(t[i][j].getEstadoCasilla(), true) && !jugadorEnMapa) {
            estado[0] = true;
            t[i][j].setEspecificoEstadoCasilla(true, 0);
            jugadorEnMapa = true;
        } else if (t[i][j].getEstadoCasilla()[0]) {
            t[i][j].setEspecificoEstadoCasilla(false, 0);
            jugadorEnMapa = false;
        }
    }

    public void moverPlayer(ConjuntoAcciones accion) {
        boolean encontrado = false;
        int i = 0, j = 0;
        for (i = 0; i < Tablero.DIMENSION && !encontrado; i++) {
            for (j = 0; j < Tablero.DIMENSION && !encontrado; j++) {
                encontrado = t[i][j].getEstadoCasilla()[0];
            }
        }
        if (encontrado) {
            t[--i][--j].setEspecificoEstadoCasilla(false, 0);
            switch (accion) {
                case NORTE ->
                    t[--i][j].setEspecificoEstadoCasilla(true, 0);
                case SUR ->
                    t[++i][j].setEspecificoEstadoCasilla(true, 0);
                case ESTE ->
                    t[i][++j].setEspecificoEstadoCasilla(true, 0);
                case OESTE ->
                    t[i][--j].setEspecificoEstadoCasilla(true, 0);
            }
        }
    }

    public Rectangle getRectangle(int i, int j) {
        return t[i][j].getRec().getBounds();
    }

    public boolean[] setPercepciones(int posx, int posy) {
        boolean[] percepciones = new boolean[4];
        if (posx == 0 || posy == 0 || posx == (DIMENSION - 1) || posy == (DIMENSION - 1)) {
            percepciones[GOLPE] = true;
        }
        if ((posx > 0 && t[posx - 1][posy].getEstadoCasilla()[MONSTRUO])
                || (posx < (DIMENSION - 1) && t[posx + 1][posy].getEstadoCasilla()[MONSTRUO])
                || (posy > 0 && t[posx][posy - 1].getEstadoCasilla()[MONSTRUO])
                || (posy < (DIMENSION - 1) && t[posx][posy + 1].getEstadoCasilla()[MONSTRUO])) {
            percepciones[HEDOR] = true;
        }
        if (t[posx][posy].getEstadoCasilla()[TESORO]) {
            percepciones[RESPLANDOR] = true;
        }
        if ((posx > 0 && t[posx - 1][posy].getEstadoCasilla()[PRECIPICIO])
                || (posx < (DIMENSION - 1) && t[posx + 1][posy].getEstadoCasilla()[PRECIPICIO])
                || (posy > 0 && t[posx][posy - 1].getEstadoCasilla()[PRECIPICIO])
                || (posy < (DIMENSION - 1) && t[posx][posy + 1].getEstadoCasilla()[PRECIPICIO])) {
            percepciones[BRISA] = true;
        }
        return percepciones;
    }

    private boolean contains(boolean[] b, boolean mark) {
        for (int i = 0; i < b.length; i++) {
            if (b[i] == mark) {
                return true;
            }
        }
        return false;
    }

    public void setEspecificoEstadoCasilla(int i, int j, int index) {
        boolean[] estado = new boolean[4];
        estado[index] = !t[i][j].getEstadoCasilla()[index];
        t[i][j].setEstadoCasilla(estado);
    }
}
