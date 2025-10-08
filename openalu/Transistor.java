/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;

/**
 *
 * @author mimi
 */
public abstract class Transistor {
    node gate;
    node source;
    node drain;
    
    protected Transistor(node gate, node source, node drain) {
        this.gate = gate;
        this.source = source;
        this.drain = drain;
    }
    public abstract boolean is_close_circuited();
    
}

