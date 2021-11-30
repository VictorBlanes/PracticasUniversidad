package pasadizoestrecho;

import IU.Tablero;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Victor Manuel Blanes Castro
 */
public class PasadizoEstrecho extends JFrame implements MouseListener, KeyListener {

    private static Tablero tablero;
    private static boolean start = false;
    private static Agente robot = new Agente();

    public PasadizoEstrecho() {
        super("Agente reactivo practica");
        tablero = new Tablero();
        tablero.addMouseListener(this);
        this.getContentPane().add(tablero);
        this.setSize(tablero.getPreferredSize());
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println(e.getButton());
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!start) {
            int x = e.getX(), y = e.getY();
            int i = 0, j = 0;
            boolean encontrado = false;
            //Busca la casilla que ha clickado el usuario
            for (i = 0; i < Tablero.DIMENSION && !encontrado; i++) {
                for (j = 0; j < Tablero.DIMENSION && !encontrado; j++) {
                    encontrado = tablero.esCasilla(i, j, x, y);
                }
            }
            --i;
            --j;
            // El primer if evita que el usuario pueda eliminar los bordes del mapa
            if (!(i == 0 || j == 0 || i == (Tablero.DIMENSION - 1) || j == (Tablero.DIMENSION - 1))) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    tablero.ocuparDesocupar(i, j);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    tablero.setPlayer(i, j);
                }
            }
            tablero.repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            start = !start;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            nextAccion();
        }
    }

    private static void nextAccion() {
        ConjuntoAcciones accion;
        if (start) {
            robot.percibir(tablero);
            robot.actVecCaracteristicas();
            accion = robot.efecAccion(tablero);
            tablero.repaint();
        }
    }

    public static void main(String[] args) {
        PasadizoEstrecho pe = new PasadizoEstrecho();
        pe.setVisible(true);

    }
}
