/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cuevamonstruo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author vicbl
 */
public class VentanaFinal extends JFrame {

    private JButton aceptar;
    private JLabel texto;

    public VentanaFinal() {
        super("Mensaje");
        initComponents();
    }

    private void initComponents() {
        this.setSize(150,100);
        this.setResizable(false);
        aceptar = new JButton();
        texto = new JLabel();

        aceptar.setText("Ok");
        aceptar.setBounds(250, 200, 60, 20);

        aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });

        texto.setText("Ejecucion finalizada!");
        texto.setBounds(10, 10, 100, 20);

        this.add(aceptar);
        this.add(texto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
