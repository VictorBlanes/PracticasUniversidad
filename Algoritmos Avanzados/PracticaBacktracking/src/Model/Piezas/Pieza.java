/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Model.Piezas;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author vicbl
 */
public abstract class Pieza {

    private String RUTA_IMAGEN, nombre;
    private int[][] movimientos;
    private ArrayList<int[]> recorrido;
    private int[] posPieza, startingPos;
    private Color color;
    
    public Pieza(String RUTA_IMAGEN, String nombre, Color color) {
        this.RUTA_IMAGEN = RUTA_IMAGEN;
        this.nombre = nombre;
        this.recorrido = new ArrayList();
        this.posPieza = new int[2];
        this.startingPos = new int[2];
        this.color = color;
    }

    public String getRUTA_IMAGEN() {
        return RUTA_IMAGEN;
    }

    public void setRUTA_IMAGEN(String RUTA_IMAGEN) {
        this.RUTA_IMAGEN = RUTA_IMAGEN;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int[][] getMovimientos() {
        return movimientos;
    }

    public ArrayList<int[]> getRecorrido() {
        return recorrido;
    }

    public void setRecorridoItem(int[] recorrido) {
        this.recorrido.add(recorrido);
    }
    
    public void newRecorrido() {
        this.recorrido = new ArrayList();
    }
    
    public void setMovimientos(int[][] movimientos) {
        this.movimientos = movimientos;
    }

    public int[] getPosPieza() {
        return posPieza;
    }

    public int[] getStartingPos() {
        return startingPos;
    }

    public void setStartingPos(int x, int y) {
        this.startingPos[0] = x;
        this.startingPos[1] = y;
        this.posPieza[0] = x;
        this.posPieza[1] = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPosPieza(int x, int y) {
        this.posPieza[0] = x;
        this.posPieza[1] = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pieza other = (Pieza) obj;
        if (!Objects.equals(this.RUTA_IMAGEN, other.RUTA_IMAGEN)) {
            return false;
        }
        if(!Objects.equals(this.posPieza, other.posPieza)){
            return false;
        }
        return Objects.equals(this.nombre, other.nombre);
    }
}
