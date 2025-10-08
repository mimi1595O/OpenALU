/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;

/**
 *
 * @author mimi
 */
public class Wire {
    String Name;
    boolean val;
    void input(int in){
    switch(in){
                case 0 -> this.val = false;
                case 1 -> this.val = true;
                default -> throw new IllegalArgumentException("Input must be 1 or 0.");
     }
    }
    
}


