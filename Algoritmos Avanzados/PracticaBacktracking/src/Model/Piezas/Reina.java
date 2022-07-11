/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Piezas;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author vicbl
 */
public class Reina extends Pieza {

    public Reina(int dimensionTablero, Color color) {
        super("images/bR.png", "Reina", color);
        super.setMovimientos(setMovReina(dimensionTablero));
    }

    private int[][] setMovReina(int dimensionTablero) {
        int[][] res = new int[dimensionTablero * 8][2];
        for (int i = 1; i < dimensionTablero; i++) {
            int[][] movs = {{i, 0}, {i, i}, {0, i}, {-i, i}, {-i, 0}, {-i, -i}, {0, -i}, {i, -i}};
            for (int[] mov : movs) {
                res[i] = mov;
                i++;
            }
        }
        return res;
    }
}
