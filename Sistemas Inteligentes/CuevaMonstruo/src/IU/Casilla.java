package IU;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Victor Manuel Blanes Castro
 */
public class Casilla {

    private Rectangle2D.Float rec;
    private Color col;
    private boolean[] estadoCasilla = new boolean[4];

    public Casilla(Rectangle2D.Float r, Color c, boolean[] ocu) {
        this.rec = r;
        this.col = c;
        //[JUGADOR, PRECIPICIO, TESORO, MONSTRUO]
        this.estadoCasilla = ocu;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(col);
        g2d.fill(this.rec);
        Ellipse2D.Float eli = new Ellipse2D.Float(this.rec.x, this.rec.y,
                this.rec.width, this.rec.height);
        if (this.estadoCasilla[0]) {
            g2d.setColor(Color.BLACK);
            g2d.fill(eli);
        }
        if (this.estadoCasilla[1]) {
            g2d.setColor(Color.CYAN);
            g2d.fill(eli);
        }
        if (this.estadoCasilla[2]) {
            g2d.setColor(Color.YELLOW);
            g2d.fill(eli);
        }
        if (this.estadoCasilla[3]) {
            g2d.setColor(Color.RED);
            g2d.fill(eli);
        }
    }

    public Rectangle2D.Float getRec() {
        return rec;
    }

    public boolean[] getEstadoCasilla() {
        return estadoCasilla;
    }

    public void setEstadoCasilla(boolean[] estadoCasilla) {
        this.estadoCasilla = estadoCasilla;
    }

    public void setEspecificoEstadoCasilla(boolean estado, int index) {
        this.estadoCasilla[index] = estado;
    }

}
