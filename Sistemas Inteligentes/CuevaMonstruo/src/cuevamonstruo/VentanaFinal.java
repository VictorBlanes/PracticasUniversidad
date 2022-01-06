package cuevamonstruo;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Victor Manuel Blanes Castro
 */
public class VentanaFinal extends JFrame {

    private JButton aceptar;
    private JLabel texto;

    public VentanaFinal() {
        super("Mensaje");
        initComponents();
    }

    private void initComponents() {
        this.setSize(150, 100);
        this.setResizable(false);
        aceptar = new JButton();
        texto = new JLabel();

        aceptar.setText("Ok");
        aceptar.setBounds(250, 200, 60, 20);

        aceptar.addActionListener((ActionEvent evt) -> {
            System.exit(0);
        });

        texto.setText("Ejecucion finalizada!");
        texto.setBounds(10, 10, 100, 20);

        this.add(aceptar);
        this.add(texto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
