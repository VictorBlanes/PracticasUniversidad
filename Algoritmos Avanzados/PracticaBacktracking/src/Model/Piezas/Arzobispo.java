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
public class Arzobispo extends Pieza {

    private int[][] mov = {{3, 0}, {2, 1}, {1, 2}, {0, 3}, {-1, 2}, {-2, 1}, {-3, 0}, {-2, -1}, {-1, -2}, {0, -3}, {1, -2}, {2, -1}};

    public Arzobispo(Color color) {
        super("images/bA.png", "Arzobispo", color);
        super.setMovimientos(mov);
    }
}
