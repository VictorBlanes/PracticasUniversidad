/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cuevamonstruo;

import IU.Tablero;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author vicbl
 */
public class CuevaMonstruo extends JFrame implements MouseListener, KeyListener {

    private static Tablero tablero;
    private static boolean start = false;
    private static Agente robot = new Agente();

    public CuevaMonstruo() {
        super("Cueva del monstruo practica");
        tablero = new Tablero();
        tablero.addMouseListener(this);

        JPanel test = new JPanel();
        test.setLayout(new BoxLayout(test, BoxLayout.Y_AXIS));

        JPanel setDimension = new JPanel();
        setDimension.setLayout(new BoxLayout(setDimension, BoxLayout.Y_AXIS));
        JLabel modDimension = new JLabel("Modificar dimension");
        modDimension.setAlignmentX(Component.CENTER_ALIGNMENT);
        setDimension.add(modDimension);

        JPanel subSetDimension = new JPanel();
        subSetDimension.setLayout(new BoxLayout(subSetDimension, BoxLayout.X_AXIS));
        JTextField campoDimension = new JTextField();
        JButton bottonDimension = new JButton("OK");
        campoDimension.setAlignmentY(Component.CENTER_ALIGNMENT);
        subSetDimension.add(campoDimension);
        subSetDimension.add(bottonDimension);
        setDimension.add(subSetDimension);

        JPanel editorMode = new JPanel();
        editorMode.setLayout(new BoxLayout(editorMode, BoxLayout.Y_AXIS));
        JLabel edMode = new JLabel("Modo editor");

        JRadioButton monstruo = new JRadioButton("Monstruo");
        JRadioButton tesoro = new JRadioButton("Tesoro");
        JRadioButton precipicio = new JRadioButton("Precipicio");
        ButtonGroup editorGroup = new ButtonGroup();
        editorGroup.add(monstruo);
        editorGroup.add(tesoro);
        editorGroup.add(precipicio);

        editorMode.add(edMode);
        editorMode.add(monstruo);
        editorMode.add(tesoro);
        editorMode.add(precipicio);

        JPanel ejecucion = new JPanel();
        ejecucion.setLayout(new BoxLayout(ejecucion, BoxLayout.Y_AXIS));
        JLabel ejMode = new JLabel("Ejecutar programa");
        JCheckBox startEje = new JCheckBox("Modo Ejecucion");
        JPanel subEjecucion = new JPanel();
        subEjecucion.setLayout(new BoxLayout(subEjecucion, BoxLayout.X_AXIS));
        JButton step = new JButton("Step");
        JButton auto = new JButton("Auto");
        subEjecucion.add(step);
        subEjecucion.add(auto);
        ejMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        startEje.setAlignmentX(Component.LEFT_ALIGNMENT);
        subEjecucion.setAlignmentX(Component.LEFT_ALIGNMENT);
        ejecucion.add(ejMode);
        ejecucion.add(startEje);
        ejecucion.add(subEjecucion);

        setDimension.setAlignmentX(Component.LEFT_ALIGNMENT);
        editorMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        ejecucion.setAlignmentX(Component.LEFT_ALIGNMENT);

        test.add(Box.createRigidArea(new Dimension(10, 30)));
        test.add(setDimension);
        test.add(Box.createRigidArea(new Dimension(10, 150)));
        test.add(editorMode);
        test.add(Box.createRigidArea(new Dimension(10, 210)));
        test.add(ejecucion);
        test.add(Box.createRigidArea(new Dimension(10, 10)));

        this.setLayout(new FlowLayout());
        this.getContentPane().add(test);
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
            int i, j = 0;
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
        if (start) {
            robot.percibir(tablero);
            robot.actVecCaracteristicas();
            robot.efecAccion(tablero);
            tablero.repaint();
        }
    }

    public static void main(String[] args) {
        CuevaMonstruo pe = new CuevaMonstruo();
        pe.setVisible(true);

    }

}
