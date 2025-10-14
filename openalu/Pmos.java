/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;

/**
 *
 * @author mimi
 */
public class Pmos extends Components {
    private node gate,source,drain;
    
    public Pmos(node gate, node source, node drain) {
        this.gate = gate;
        this.source = source;
        this.drain = drain;
}

    @Override
    public void evaluate() {
       if(gate.get_State() == node.States.LOW){
           if(source.get_State() != node.States.FLOATING && drain.get_State() != source.get_State()) drain.set_State(source.get_State());
           else if (drain.get_State() != node.States.FLOATING && source.get_State() != drain.get_State()) source.set_State(drain.get_State());
       
       }
    }    
    }

