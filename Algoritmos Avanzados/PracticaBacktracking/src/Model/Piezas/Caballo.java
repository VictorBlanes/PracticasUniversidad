/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Piezas;

import java.awt.Color;

/**
 *
 * @author vicbl
 */
public class Caballo extends Pieza {

    private int[][] mov = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

    public Caballo(Color color) {
        super("images/bN.png", "Caballo", color);
        super.setMovimientos(mov);
    }
}
