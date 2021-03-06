package cuevamonstruo;

import Agente.Agente;
import IU.Tablero;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */

public class CuevaMonstruo extends JFrame implements MouseListener, KeyListener {

    private static CuevaMonstruo cm;
    private static Tablero tablero;
    private static final Agente robot = new Agente();
    private static JPanel opContainer, setDimension, subSetDimension, editorMode, ejecucion, subEjecucion;
    private static JLabel modDimension, edMode, ejMode;
    private static JButton bottonDimension, step, auto;
    private static JRadioButton monstruo, tesoro, precipicio;
    private static JSpinner campoDimension;
    private static JCheckBox startEje;
    private static ButtonGroup editorGroup;

    public CuevaMonstruo() {
        super("Cueva del monstruo practica");
        opContainer = new JPanel();
        setDimension = new JPanel();
        subSetDimension = new JPanel();
        editorMode = new JPanel();
        ejecucion = new JPanel();
        subEjecucion = new JPanel();
        modDimension = new JLabel("Modificar dimension");
        edMode = new JLabel("Modo editor");
        ejMode = new JLabel("Ejecutar programa");
        bottonDimension = new JButton("OK");
        step = new JButton("Step");
        auto = new JButton("Auto");
        monstruo = new JRadioButton("Monstruo");
        tesoro = new JRadioButton("Tesoro");
        precipicio = new JRadioButton("Precipicio");
        campoDimension = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
        startEje = new JCheckBox("Modo Ejecucion");
        editorGroup = new ButtonGroup();
        addComponents();
    }

    private void addComponents() {
        //Modificar Dimension IU
        setDimension.setLayout(new BoxLayout(setDimension, BoxLayout.Y_AXIS));
        modDimension.setAlignmentX(Component.CENTER_ALIGNMENT);
        setDimension.add(modDimension);

        subSetDimension.setLayout(new BoxLayout(subSetDimension, BoxLayout.X_AXIS));
        campoDimension.setAlignmentY(Component.CENTER_ALIGNMENT);
        subSetDimension.add(campoDimension);
        subSetDimension.add(bottonDimension);
        setDimension.add(subSetDimension);

        //Modo Editor IU
        editorMode.setLayout(new BoxLayout(editorMode, BoxLayout.Y_AXIS));
        editorGroup.add(monstruo);
        editorGroup.add(tesoro);
        editorGroup.add(precipicio);
        editorMode.add(edMode);
        editorMode.add(monstruo);
        editorMode.add(tesoro);
        editorMode.add(precipicio);

        //Ejecutar Programa IU
        subEjecucion.setLayout(new BoxLayout(subEjecucion, BoxLayout.X_AXIS));
        subEjecucion.add(step);
        subEjecucion.add(auto);

        ejecucion.setLayout(new BoxLayout(ejecucion, BoxLayout.Y_AXIS));
        ejMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        startEje.setAlignmentX(Component.LEFT_ALIGNMENT);
        subEjecucion.setAlignmentX(Component.LEFT_ALIGNMENT);
        ejecucion.add(ejMode);
        ejecucion.add(startEje);
        ejecucion.add(subEjecucion);

        //JPanel que reune todos los JPanel anteriores
        opContainer.setLayout(new BoxLayout(opContainer, BoxLayout.Y_AXIS));
        setDimension.setAlignmentX(Component.LEFT_ALIGNMENT);
        editorMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        ejecucion.setAlignmentX(Component.LEFT_ALIGNMENT);
        opContainer.add(Box.createRigidArea(new Dimension(10, 30)));
        opContainer.add(setDimension);
        opContainer.add(Box.createRigidArea(new Dimension(10, 150)));
        opContainer.add(editorMode);
        opContainer.add(Box.createRigidArea(new Dimension(10, 210)));
        opContainer.add(ejecucion);
        opContainer.add(Box.createRigidArea(new Dimension(10, 10)));

        tablero = new Tablero();
        tablero.addMouseListener(this);

        bottonDimension.addActionListener((ActionEvent evt) -> {
            tablero.resizeArray((int) campoDimension.getValue());
            robot.resizeBc((int) campoDimension.getValue());
            tablero.moverPlayer(Tablero.DIMENSION - 1, 0);
            repaint();
        });
        step.addActionListener((ActionEvent evt) -> {
            if (startEje.isSelected()) {
                robot.nextAccion();
            }
        });
        auto.addActionListener((ActionEvent evt) -> {
            if (startEje.isSelected()) {
                robot.startStop();
            }
        });

        this.setLayout(new FlowLayout());
        this.getContentPane().add(opContainer);
        this.getContentPane().add(tablero);
        this.setSize(tablero.getPreferredSize());
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        tablero.setPlayer(Tablero.DIMENSION - 1, 0);
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
        if (!startEje.isSelected()) {
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
            //[JUGADOR, PRECIPICIO, TESORO, MONSTRUO]
            if (monstruo.isSelected()) {
                tablero.setEspecificoEstadoCasilla(i, j, 3);
                robot.setFlechas(tablero.getNum_monstruos());
            } else if (tesoro.isSelected()) {
                tablero.setEspecificoEstadoCasilla(i, j, 2);
            } else if (precipicio.isSelected()) {
                tablero.setEspecificoEstadoCasilla(i, j, 1);
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
    }

    private static boolean startRobot() {
        return robot.efecAccion(tablero, 0, 0);
    }

    public static void main(String[] args) {
        VentanaFinal vf = new VentanaFinal();
        boolean res = false;
        cm = new CuevaMonstruo();
        cm.setVisible(true);
        res = startRobot();
        if (res){
            vf.setVisible(true);
        }
    }

}
