/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;

/**
 *
 * @author mimi
 */
public class Pmos extends Transistor {
    public Pmos(node gate, node source, node drain) {
        
        super(gate, source, drain);}
    @Override
    public boolean is_close_circuited(){
        
        return gate.state == node.States.LOW;     
    }
}
