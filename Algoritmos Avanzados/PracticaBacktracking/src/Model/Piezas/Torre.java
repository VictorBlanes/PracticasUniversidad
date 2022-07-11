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
public class Torre extends Pieza {

    public Torre(int dimensionTablero, Color color) {
        super("images/bT.png", "Torre", color);
        super.setMovimientos(setMovTorre(dimensionTablero));
    }

    private int[][] setMovTorre(int dimensionTablero) {
        int[][] res = new int[dimensionTablero * 4][2];
        for (int i = 1; i < dimensionTablero; i++) {
            int[][] movs = {{i, 0}, {0, i}, {-i, 0}, {0, -i}};
            for (int[] mov : movs) {
                res[i] = mov;
                i++;
            }
        }
        return res;
    }
}
