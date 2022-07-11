package View;

import Model.Piezas.Arzobispo;
import Model.Piezas.Caballo;
import Model.Piezas.Elefante;
import Model.Piezas.Halcon;
import Model.Piezas.Pieza;
import Model.Piezas.Reina;
import Model.Piezas.Torre;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import practicaBacktracking.PerEsdeveniments;

/**
 *
 * @author Victor Manuel Blanes Castro
 */
class ImagenVista extends JPanel implements PerEsdeveniments {

    private BufferedImage imagenPieza;
    private static Vista vista;
    private Pieza pieza;

    public ImagenVista(Vista vista, String pieza) {
        ImagenVista.vista = vista;
        switch (pieza) {
            case "Arzobispo" ->
                this.pieza = new Arzobispo(Color.BLACK);
            case "Caballo" ->
                this.pieza = new Caballo(Color.BLACK);
            case "Elefante" ->
                this.pieza = new Elefante(Color.BLACK);
            case "Halcon" ->
                this.pieza = new Halcon(Color.BLACK);
            case "Reina" ->
                this.pieza = new Reina(0, Color.BLACK);
            case "Torre" ->
                this.pieza = new Torre(0, Color.BLACK);
        }
        if (this.pieza != null) {
            try {
                imagenPieza = ImageIO.read(new File(this.pieza.getRUTA_IMAGEN()));
            } catch (IOException e) {
                notificar("Ventana Error|ErrorIO|Error cargando imagen: " + this.pieza.getRUTA_IMAGEN());
            }
        }
    }

    private void setImagenPieza(String pieza) {
        switch (pieza) {
            case "Arzobispo" ->
                this.pieza = new Arzobispo(Color.BLACK);
            case "Caballo" ->
                this.pieza = new Caballo(Color.BLACK);
            case "Elefante" ->
                this.pieza = new Elefante(Color.BLACK);
            case "Halcon" ->
                this.pieza = new Halcon(Color.BLACK);
            case "Reina" ->
                this.pieza = new Reina(0, Color.BLACK);
            case "Torre" ->
                this.pieza = new Torre(0, Color.BLACK);
        }
        if (this.pieza != null) {
            try {
                imagenPieza = ImageIO.read(new File(this.pieza.getRUTA_IMAGEN()));
            } catch (IOException e) {
                System.err.println("Error cargando imagen: " + this.pieza.getRUTA_IMAGEN());
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (pieza != null) {
            g2d.drawImage(imagenPieza, 15, 15, 150, 150, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150, 150);
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Cambiar Pieza")) {
            String[] res = s.split("\\|");
            String nombrePieza = res[1];
            setImagenPieza(nombrePieza);
            this.repaint();
        } else if (s.startsWith("Ventana Error")) {
            vista.notificar(s);
        }
    }

}
