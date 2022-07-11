package View;

import Model.Piezas.Arzobispo;
import Model.Piezas.Caballo;
import Model.Piezas.Elefante;
import Model.Piezas.Halcon;
import Model.Piezas.Pieza;
import Model.Piezas.Reina;
import Model.Piezas.Torre;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import practicaBacktracking.PerEsdeveniments;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */
public class Casilla extends JPanel implements MouseListener, PerEsdeveniments {

    private static Vista vista;
    private static boolean recorridoActivado = false;
    private int posX, posY;
    private int pixDimX, pixDimY;
    private int posRecorrido;
    private Pieza pieza;
    private BufferedImage imagen_pieza;
    private Color colorCasilla, colorRecorrido;
    private Random rng;

    public Casilla(Vista vista) {
        Casilla.vista = vista;
    }

    public Casilla(int posX, int posY, int pixDimX, int pixDimY, Pieza pieza, Color colorCasilla) {
        this.posX = posX;
        this.posY = posY;
        this.pixDimX = pixDimX;
        this.pixDimY = pixDimY;
        this.colorCasilla = colorCasilla;
        this.rng = new Random();
        this.pieza = pieza;
        if (pieza != null) {
            try {
                imagen_pieza = ImageIO.read(new File(pieza.getRUTA_IMAGEN()));
            } catch (IOException e) {
                notificar("Ventana Error|ErrorIO|Error cargando imagen: " + this.pieza.getRUTA_IMAGEN());
            }
        }
        this.addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(colorCasilla);
        g2d.fillRect(0, 0, pixDimX, pixDimY);
        int dimension = vista.getDimensionTablero();
        if (pieza != null) {
            g2d.drawImage(imagen_pieza, 5, 5, pixDimX - 10, pixDimY - 10, null);
        } else if (recorridoActivado && posRecorrido != 0) {
            g2d.setColor(colorRecorrido);
            g2d.setFont(new Font("TimesRoman", Font.BOLD, 300 / dimension));
            g2d.drawString(posRecorrido + "", (pixDimX / 2) - 5, (pixDimX / 2) + 5);
        }
    }

    public void borrarFicha() {
        this.pieza = null;
        this.imagen_pieza = null;
    }

    public void setDims(int dimX, int dimY) {
        this.pixDimX = dimX;
        this.pixDimY = dimY;
    }

    public static void setRecorridoActivado(boolean recorridoActivado) {
        Casilla.recorridoActivado = recorridoActivado;
    }

    public static boolean isRecorridoActivado() {
        return recorridoActivado;
    }

    public void setPosRecorrido(int posRecorrido) {
        this.posRecorrido = posRecorrido;
    }

    public Color getColorRecorrido() {
        return colorRecorrido;
    }

    public void setColorRecorrido(Color colorRecorrido) {
        this.colorRecorrido = colorRecorrido;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Pieza oldPieza = pieza;
        int dimensionTablero = vista.getDimensionTablero();
        Color color = new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
        switch (vista.getPiezaSeleccionada()) {
            case "Arzobispo" ->
                this.pieza = new Arzobispo(color);
            case "Caballo" ->
                this.pieza = new Caballo(color);
            case "Elefante" ->
                this.pieza = new Elefante(color);
            case "Halcon" ->
                this.pieza = new Halcon(color);
            case "Reina" ->
                this.pieza = new Reina(dimensionTablero, color);
            case "Torre" ->
                this.pieza = new Torre(dimensionTablero, color);
        }
        try {
            if (pieza != null) {
                pieza.setStartingPos(posX, posY);
                if (oldPieza != null && oldPieza.getNombre().equals(pieza.getNombre())) {
                    borrarFicha();
                    vista.borrarPieza(oldPieza);
                } else if (oldPieza != null) {
                    vista.setPiezaTablero(this.pieza);
                    vista.borrarPieza(oldPieza);
                    imagen_pieza = ImageIO.read(new File(pieza.getRUTA_IMAGEN()));
                } else {
                    vista.setPiezaTablero(this.pieza);
                    imagen_pieza = ImageIO.read(new File(pieza.getRUTA_IMAGEN()));
                }
            }
        } catch (IOException ex) {
            notificar("Ventana Error|ErrorIO|Error cargando imagen: " + this.pieza.getRUTA_IMAGEN());
        }
        notificar("Repintar Tablero");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void notificar(String s) {
        if ((s.startsWith("Repintar Tablero")) || (s.startsWith("Cambiar Pieza"))
                || (s.startsWith("Ventana Error"))) {
            vista.notificar(s);
        }
    }

}
