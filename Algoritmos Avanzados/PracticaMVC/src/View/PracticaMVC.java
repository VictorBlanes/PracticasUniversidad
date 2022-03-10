package View;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PracticaMVC extends JFrame {

    private static PracticaMVC ventana;
    public static Grafica grafica;
    private static JPanel opContainer, editorMode;
    private static JLabel edMode;
    private static JButton auto;
    private static JCheckBox log, n, nlogn, cuadratic, nexp;

    public PracticaMVC() {
        super("Practica Patron MVC");
        opContainer = new JPanel();
        editorMode = new JPanel();
        edMode = new JLabel("Tipo Funciones");
        auto = new JButton("Mostrar");
        log = new JCheckBox("Logaritmica");
        n = new JCheckBox("Lineal");
        nlogn = new JCheckBox("Cuasilineal");
        cuadratic = new JCheckBox("Cuadratica");
        nexp = new JCheckBox("SubExponencial");
        addComponents();
    }

    private void addComponents() {
        //Modo Editor IU
        editorMode.setLayout(new BoxLayout(editorMode, BoxLayout.Y_AXIS));
        editorMode.add(edMode);
        editorMode.add(log);
        editorMode.add(n);
        editorMode.add(nlogn);
        editorMode.add(cuadratic);
        editorMode.add(nexp);
        editorMode.add(auto);

        //JPanel que reune todos los JPanel anteriores
        opContainer.setLayout(new BoxLayout(opContainer, BoxLayout.Y_AXIS));
        editorMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        opContainer.add(editorMode);

        grafica = new Grafica();

        auto.addActionListener((ActionEvent evt) -> {
            boolean[] selected = {log.isSelected(), n.isSelected(),
                nlogn.isSelected(), cuadratic.isSelected(), nexp.isSelected()};
            grafica.calcGraficos(selected);

        });
        
        this.setLayout(new FlowLayout());
        this.getContentPane().add(opContainer);
        this.getContentPane().add(grafica);
        this.setSize(grafica.getPreferredSize());
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        ventana = new PracticaMVC();
        ventana.setVisible(true);
    }

}
