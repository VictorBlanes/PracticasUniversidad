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
public class Elefante extends Pieza {
    private int[][] mov = {{2,0},{1,1},{0,2},{-1,1},{-2,0},{-1,-1},{0,-2},{1,-1}};
    
    public Elefante(Color color){
        super("images/bE.png", "Elefante", color);
        super.setMovimientos(mov);
    }
}
