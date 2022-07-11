package Controller;

import Model.Piezas.Pieza;
import java.util.ArrayList;

/**
 *
 * @author Victor Manuel Blanes Castro
 */
public class CaminoHamiltonianoHelper {

    private int[] posPieza;
    private Pieza pieza;
    private boolean[][] posChecked;
    private int lugaresLibresRestantes;
    private ArrayList<int[]> recorrido;

    public CaminoHamiltonianoHelper(int[] posPieza, Pieza pieza, boolean[][] posChecked, int lugaresLibresRestantes, ArrayList<int[]> recorrido) {
        this.posPieza = posPieza;
        this.pieza = pieza;
        this.posChecked = posChecked;
        this.lugaresLibresRestantes = lugaresLibresRestantes;
        this.recorrido = recorrido;
    }

    public int[] getPosPieza() {
        return posPieza;
    }

    public void setPosPieza(int[] posPieza) {
        this.posPieza = posPieza;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public boolean[][] getPosChecked() {
        return posChecked;
    }

    public void setPosChecked(boolean[][] posChecked) {
        this.posChecked = posChecked;
    }

    public int getLugaresLibresRestantes() {
        return lugaresLibresRestantes;
    }

    public void setRecorrido(ArrayList<int[]> recorrido) {
        this.recorrido = recorrido;
    }

    public ArrayList<int[]> getRecorrido() {
        return recorrido;
    }

    public void setLugaresLibresRestantes(int lugaresLibresRestantes) {
        this.lugaresLibresRestantes = lugaresLibresRestantes;
    }

}
