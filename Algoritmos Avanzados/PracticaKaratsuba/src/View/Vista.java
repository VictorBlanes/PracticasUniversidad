package View;

import Controller.Controlador;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import practicaKaratsuba.PerEsdeveniments;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */

public class Vista extends JFrame implements PerEsdeveniments {

    private static Controlador controlador;
    private static Grafica grafica;
    private static JPanel panelFinal, zonaIzquierda, comboEdicion, algoritmoUmbral, comboLabelAlgoritmo, comboLabelUmbral,  comboBotones, panelTextArea;
    private static JLabel elegirAlgoritmo, elegirUmbral, elegirNumeros;
    private static JButton calcular, parar, estudio;
    private static JSpinner umbral;
    private static JComboBox algoritmos;
    private static JScrollPane sp;
    private static JTextField numero1, numero2;
    private static JTextArea resultados;

    public Vista(Controlador controlador) {
        super("Practica Karatsuba");
        String[] algoritmos_names = {"Karatsuba", "Mixto", "Tradicional"};
        Vista.controlador = controlador;
        zonaIzquierda = new JPanel();
        comboEdicion = new JPanel();
        algoritmoUmbral = new JPanel();
        comboLabelAlgoritmo = new JPanel();
        comboLabelUmbral = new JPanel();
        panelFinal = new JPanel();
        panelTextArea = new JPanel();

        elegirAlgoritmo = new JLabel("Algoritmo");
        elegirUmbral = new JLabel("Umbral");
        elegirNumeros = new JLabel("Numeros");
        algoritmos = new JComboBox(algoritmos_names);
        calcular = new JButton("Calcular");
        parar = new JButton("Parar");
        estudio = new JButton("Estudio");
        umbral = new JSpinner(new SpinnerNumberModel(320, 1, null, 1));
        numero1 = new JTextField();
        numero2 = new JTextField();
        resultados = new JTextArea(30, 40);
        sp = new JScrollPane(resultados, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addComponents();
        this.setVisible(true);
    }

    private void addComponents() {
        comboLabelAlgoritmo.setLayout(new BoxLayout(comboLabelAlgoritmo, BoxLayout.Y_AXIS));
        comboLabelAlgoritmo.setAlignmentX(Component.LEFT_ALIGNMENT);
        elegirAlgoritmo.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        algoritmos.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        comboLabelAlgoritmo.add(elegirAlgoritmo);
        comboLabelAlgoritmo.add(algoritmos);

        comboLabelUmbral.setLayout(new BoxLayout(comboLabelUmbral, BoxLayout.Y_AXIS));
        comboLabelUmbral.setAlignmentX(Component.LEFT_ALIGNMENT);
        elegirUmbral.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        umbral.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        comboLabelUmbral.add(elegirUmbral);
        comboLabelUmbral.add(umbral);

        algoritmoUmbral.setLayout(new BoxLayout(algoritmoUmbral, BoxLayout.X_AXIS));
        algoritmoUmbral.add(comboLabelAlgoritmo);
        algoritmoUmbral.add(Box.createRigidArea(new Dimension(10, 0)));
        algoritmoUmbral.add(comboLabelUmbral);

        comboEdicion.setLayout(new BoxLayout(comboEdicion, BoxLayout.Y_AXIS));
        numero1.setText("0");
        numero1.setColumns(30);
        numero2.setText("0");
        numero2.setColumns(30);
        elegirNumeros.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        numero1.setAlignmentX(Component.LEFT_ALIGNMENT);
        numero2.setAlignmentX(Component.LEFT_ALIGNMENT);
        algoritmoUmbral.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboEdicion.add(elegirNumeros);
        comboEdicion.add(numero1);
        comboEdicion.add(numero2);
        comboEdicion.add(Box.createRigidArea(new Dimension(0, 40)));
        comboEdicion.add(algoritmoUmbral);

        comboBotones.setLayout(new BoxLayout(comboBotones, BoxLayout.X_AXIS));
        comboBotones.add(calcular);
        comboBotones.add(parar);
        comboBotones.add(estudio);

        zonaIzquierda.setLayout(new BoxLayout(zonaIzquierda, BoxLayout.Y_AXIS));
        comboEdicion.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        zonaIzquierda.add(comboEdicion);
        zonaIzquierda.add(Box.createRigidArea(new Dimension(10, 250)));
        zonaIzquierda.add(comboBotones);

        grafica = new Grafica(controlador);

        calcular.addActionListener((ActionEvent evt) -> {
            if (!numero1.getText().matches("^([0])$|^([-]?[1-9]+[0-9]*)$")) {
                errorVentana("Error de validacion", "¡El numero introducido en el primer campo no es valido!");
            } else if (!numero2.getText().matches("^([0])$|^([-]?[1-9]+[0-9]*)$")) {
                errorVentana("Error de validacion", "¡El numero introducido en el segundo campo no es valido!");
            } else {
                String data = String.format("Actualizar Modelo|%s|%s|%s|%s", algoritmos.getSelectedItem(), umbral.getValue(), numero1.getText(), numero2.getText());
                notificar(data);
                notificar("Ejecutar Calculo");
            }
        });

        parar.addActionListener((ActionEvent evt) -> {
            umbral.setEnabled(true);
            notificar("Parar Ejecucion");
        });

        estudio.addActionListener((ActionEvent evt) -> {
            umbral.setEnabled(false);
            notificar("Ejecutar Estudio");
        });

        resultados.setEditable(false);
        panelTextArea.add(sp);

        panelFinal.setLayout(new FlowLayout());
        panelFinal.add(zonaIzquierda);
        panelFinal.add(grafica);
        panelFinal.add(panelTextArea);

        this.getContentPane().setLayout(new FlowLayout());
        this.getContentPane().add(panelFinal);
        this.setSize(panelFinal.getSize());
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void appendResults() {
        String res;
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String num1 = controlador.getNumero1(), num2 = controlador.getNumero2(), algoritmo = controlador.getAlgoritmo(), resultado = controlador.getResultado();
        double tiempoEje = controlador.getTiempoEjecuccion();
        res = String.format("\nResultados %s:\n   Input:\n       Numero 1: %s\n       Numero 2: %s\n   Output:\n       Algoritmo %s calculado en %,.3f segundos: %s\n", formatter.format(ts), num1, num2, algoritmo, tiempoEje, resultado);
        resultados.setText(res + resultados.getText());
    }

    public void newResults(String res) {
        resultados.setText(res);
    }

    public void errorVentana(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(Vista.this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Actualizar Modelo") || s.startsWith("Ejecutar Calculo")
                || s.startsWith("Ejecutar Estudio") || s.startsWith("Parar Ejecucion")) {
            controlador.notificar(s);
        } else if (s.startsWith("Calculo Terminado")) {
            appendResults();
        } else if (s.startsWith("Actualizar Progreso")) {
            s = s.replaceAll("Actualizar Progreso", "");
            s = String.format("Realizando estudio de umbral optimo\n    Progreso: %s/700\n", s);
            newResults(s);
        } else if (s.startsWith("Actualizar Grafica")) {
            int umbralOptimo = controlador.getUmbral();
            umbral.setValue(umbralOptimo);
            umbral.setEnabled(true);
            grafica.setData(controlador.getTiemposEstudio(), umbralOptimo);
            newResults("\nUmbral optimo encontrado: " + umbralOptimo);
        } else if (s.startsWith("Ejecucion Parada")) {
            newResults("\nEjecucion detenida.");
        }
    }
}
