package View;

import Controller.Controlador;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import practicaMVC.PerEsdeveniments;

/**
 *
 * @author vicbl
 */
public class Vista extends JFrame implements PerEsdeveniments {

    private Controlador controlador;
    private static Grafica grafica;
    private static JPanel panelFinal, panelSelector, panelBotones, panelStartStop, panelZoom;
    private static JLabel labelFunciones;
    private static JButton ejecutar, parar, zoomIn, zoomOut;
    private static JCheckBox log, n, nlogn, cuadratic, nexp;

    public Vista(Controlador controlador) {
        super("Practica Patron MVC");
        this.controlador = controlador;
        panelFinal = new JPanel();
        panelSelector = new JPanel();
        labelFunciones = new JLabel("Tipo Funciones");
        panelBotones = new JPanel();
        panelStartStop = new JPanel();
        panelZoom = new JPanel();
        ejecutar = new JButton("Mostrar");
        parar = new JButton("Parar");
        zoomIn = new JButton("+");
        zoomOut = new JButton("-");
        log = new JCheckBox("Logaritmica");
        n = new JCheckBox("Lineal");
        nlogn = new JCheckBox("Cuasilineal");
        cuadratic = new JCheckBox("Cuadratica");
        nexp = new JCheckBox("SubExponencial");
        addComponents();
        this.setVisible(true);
    }

    private void addComponents() {
        //Modo Editor IU
        panelSelector.setLayout(new BoxLayout(panelSelector, BoxLayout.Y_AXIS));
        panelSelector.add(labelFunciones);
        panelSelector.add(log);
        panelSelector.add(n);
        panelSelector.add(nlogn);
        panelSelector.add(cuadratic);
        panelSelector.add(nexp);

        panelZoom.setLayout(new BoxLayout(panelZoom, BoxLayout.X_AXIS));
        panelZoom.add(zoomOut);
        panelZoom.add(zoomIn);
        
        panelStartStop.setLayout(new BoxLayout(panelStartStop, BoxLayout.X_AXIS));
        panelStartStop.add(ejecutar);
        panelStartStop.add(parar);

        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelStartStop.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelZoom.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBotones.add(panelStartStop);
        panelBotones.add(panelZoom);

        //JPanel que reune todos los JPanel anteriores
        panelFinal.setLayout(new BoxLayout(panelFinal, BoxLayout.Y_AXIS));
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFinal.add(panelSelector);
        panelFinal.add(Box.createRigidArea(new Dimension(10, 210)));
        panelFinal.add(panelBotones);

        grafica = new Grafica(this);

        ejecutar.addActionListener((ActionEvent evt) -> {
            notificar("Limpiar Data");
            notificar("Calcular Graficos");

        });

        parar.addActionListener((ActionEvent evt) -> {
            notificar("Detener Graficos");
        });

        zoomIn.addActionListener((ActionEvent evt) -> {
            notificar("Bajar Maximo");
            notificar("Mostrar Resultados");
        });

        zoomOut.addActionListener((ActionEvent evt) -> {
            notificar("Subir Maximo");
            notificar("Mostrar Resultados");
        });

        this.setLayout(new FlowLayout());
        this.getContentPane().add(panelFinal);
        this.getContentPane().add(grafica);
        this.setSize(grafica.getPreferredSize());
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public boolean[] getSelected() {
        boolean[] selected = {log.isSelected(), n.isSelected(),
            nlogn.isSelected(), cuadratic.isSelected(), nexp.isSelected()};
        return selected;
    }

    public int getNumGraph() {
        return controlador.getNum_graph();
    }

    public double getMax() {
        return controlador.getMax();
    }

    public int getBase() {
        return controlador.getNum_graph();
    }

    public double[][] getData() {
        return controlador.getData();
    }

    public boolean[] getCalcDone() {
        return controlador.getCalcDone();
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Mostrar Resultados")) {
            grafica.notificar(s);
        } else if (s.startsWith("Calcular Graficos") || s.startsWith("Detener Graficos")
                || s.startsWith("Limpiar Data") || s.startsWith("Bajar Maximo")
                || s.startsWith("Subir Maximo")) {
            controlador.notificar(s);
        }
    }

}
