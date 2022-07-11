package View;

import Controller.Controlador;
import Model.Piezas.Pieza;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import practicaBacktracking.PerEsdeveniments;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */
public class Vista extends JFrame implements PerEsdeveniments {

    private static Controlador controlador;
    private static Tablero tablero;
    private static ImagenVista panelImagen;
    private static JPanel panelFinal, opContainer, subEditorMode, editorMode, comboBotones, comboLabelImagen, comboLabelDimension, textScroll, comboLabelPiezas;
    private static JLabel elegirPiezas, elegirDimension, piezaSeleccionada;
    private static JButton ejecutar, parar, recorrido;
    private static JSpinner dimension;
    private static JComboBox piezas;
    private static JScrollPane sp;
    private static JTextArea resultados;

    public Vista(Controlador controlador) {
        super("Practica Karatsuba");
        String[] nombre_piezas = {"Arzobispo", "Caballo", "Elefante", "Halcon", "Reina", "Torre"};
        Vista.controlador = controlador;
        opContainer = new JPanel();
        editorMode = new JPanel();
        subEditorMode = new JPanel();
        comboBotones = new JPanel();
        comboLabelPiezas = new JPanel();
        comboLabelDimension = new JPanel();
        comboLabelImagen = new JPanel();
        panelFinal = new JPanel();
        textScroll = new JPanel();

        elegirPiezas = new JLabel("Piezas");
        piezas = new JComboBox(nombre_piezas);
        elegirDimension = new JLabel("Dimension");
        piezaSeleccionada = new JLabel();
        dimension = new JSpinner(new SpinnerNumberModel(8, 2, 30, 1));
        ejecutar = new JButton("Calcular");
        parar = new JButton("Parar");
        recorrido = new JButton("Desactivar recorrido");
        resultados = new JTextArea(28, 30);
        sp = new JScrollPane(resultados, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addComponents();
        this.setVisible(true);
    }

    private void addComponents() {
        comboLabelPiezas.setLayout(new BoxLayout(comboLabelPiezas, BoxLayout.Y_AXIS));
        comboLabelPiezas.setAlignmentX(Component.LEFT_ALIGNMENT);
        elegirPiezas.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        piezas.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        comboLabelPiezas.add(elegirPiezas);
        comboLabelPiezas.add(piezas);

        comboLabelDimension.setLayout(new BoxLayout(comboLabelDimension, BoxLayout.Y_AXIS));
        comboLabelDimension.setAlignmentX(Component.LEFT_ALIGNMENT);
        elegirDimension.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        dimension.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        comboLabelDimension.add(elegirDimension);
        comboLabelDimension.add(dimension);

        panelImagen = new ImagenVista(this, (String) piezas.getSelectedItem());
        piezaSeleccionada.setText("Pieza Seleccionada: " + (String) piezas.getSelectedItem());

        comboLabelImagen.setLayout(new BoxLayout(comboLabelImagen, BoxLayout.Y_AXIS));
        comboLabelImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        piezaSeleccionada.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panelImagen.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        comboLabelImagen.add(piezaSeleccionada);
        comboLabelImagen.add(panelImagen);

        subEditorMode.setLayout(new BoxLayout(subEditorMode, BoxLayout.X_AXIS));
        subEditorMode.add(comboLabelPiezas);
        subEditorMode.add(Box.createRigidArea(new Dimension(10, 0)));
        subEditorMode.add(comboLabelDimension);

        comboBotones.setLayout(new BoxLayout(comboBotones, BoxLayout.X_AXIS));
        comboBotones.add(ejecutar);
        comboBotones.add(parar);
        comboBotones.add(recorrido);

        editorMode.setLayout(new BoxLayout(editorMode, BoxLayout.Y_AXIS));
        subEditorMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboLabelImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        editorMode.add(subEditorMode);
        editorMode.add(Box.createRigidArea(new Dimension(10, 100)));
        editorMode.add(comboLabelImagen);
        editorMode.add(Box.createRigidArea(new Dimension(10, 100)));
        editorMode.add(comboBotones);

        resultados.setEditable(false);
        textScroll.add(sp);

        opContainer.setLayout(new BoxLayout(opContainer, BoxLayout.Y_AXIS));
        comboBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboLabelPiezas.setAlignmentX(Component.LEFT_ALIGNMENT);
        textScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        opContainer.add(editorMode);
        opContainer.add(Box.createRigidArea(new Dimension(10, 10)));
        opContainer.add(textScroll);

        tablero = new Tablero(this);
        tablero.setOpaque(true);

        ejecutar.addActionListener((ActionEvent evt) -> {
            notificar("Ejecutar Calculo");
        });

        parar.addActionListener((ActionEvent evt) -> {
            notificar("Parar Ejecucion");
        });

        recorrido.addActionListener((ActionEvent evt) -> {
            notificar("Eliminar Recorrido");
            notificar("Desactivar Recorrido");
        });

        dimension.addChangeListener((ChangeEvent e) -> {
            notificar("Cambiar Dimension|" + (int) dimension.getValue());
            notificar("Repintar Tablero");
        });

        piezas.addActionListener((ActionEvent evt) -> {
            String nombrePieza = (String) piezas.getSelectedItem();
            piezaSeleccionada.setText("Pieza Seleccionada: " + nombrePieza);
            notificar("Cambiar Pieza|" + nombrePieza);
        });

        panelFinal.setLayout(new FlowLayout());
        panelFinal.add(opContainer);
        panelFinal.add(tablero);

        this.getContentPane().setLayout(new FlowLayout());
        this.getContentPane().add(panelFinal);
        this.setSize(panelFinal.getSize());
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getPiezaSeleccionada() {
        return (String) piezas.getSelectedItem();
    }

    public int getDimensionTablero() {
        return controlador.getDimensionTablero();
    }

    void setPiezaTablero(Pieza pieza) {
        controlador.setPiezaTablero(pieza);
    }

    public void setRecorridoPieza(Pieza pieza) {
        tablero.setRecorridoPieza(pieza);
    }
    public void borrarPieza(Pieza pieza){
        controlador.borrarPieza(pieza);
    }
    private void appendText(String text) {
        resultados.setText(text + "\n" + resultados.getText());
    }

    private void errorVentana(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(Vista.this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Ejecutar Calculo") || s.startsWith("Parar Ejecucion")
                || s.startsWith("Eliminar Recorrido") || s.startsWith("Cambiar Dimension")) {
            controlador.notificar(s);
        } else if (s.startsWith("Repintar Tablero") || s.startsWith("Activar Recorrido")
                || (s.startsWith("Quitar Pieza")) || (s.startsWith("Desactivar Recorrido"))) {
            tablero.notificar(s);
        } else if (s.startsWith("Cambiar Pieza")) {
            panelImagen.notificar(s);
        } else if (s.startsWith("Mensaje")) {
            String[] res = s.split("\\|");
            appendText(res[1]);
        } else if (s.startsWith("Ventana Error")) {
            String[] res = s.split("\\|");
            errorVentana(res[1], res[2]);
        }
    }
}
