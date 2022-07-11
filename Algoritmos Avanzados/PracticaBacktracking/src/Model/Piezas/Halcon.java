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
public class Halcon extends Pieza {
    private int[][] mov = {{2,0},{3,0},{2,2},{3,3},{0,2},{0,3},{-2,2},{-3,3},{-2,0},{-3,0},{-2,-2},{-3,-3},{0,-2},{0,-3},{2,-2},{3,-3}};
    
    public Halcon(Color color){
        super("images/bH.png", "Halcon", color);
        super.setMovimientos(mov);
    }
}
