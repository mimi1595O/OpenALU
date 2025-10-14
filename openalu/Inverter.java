/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;

import java.util.Arrays;

/**
 *
 * @author USER
 */
public class Inverter extends Components {
    public Inverter(node in, node... outputs){
    super();
    this.inputs.add(in);
    this.outputs.addAll(Arrays.asList(outputs));
    }
    @Override
    public void evaluate() {
        node in = inputs.get(0);
        switch(in.get_State()){
            case node.States.HIGH -> {
                for (node out : outputs) out.set_State(node.States.LOW);
            }
            case node.States.LOW -> {
                for (node out : outputs) out.set_State(node.States.HIGH);
            }
            default -> {
                for (node out : outputs) out.set_State(node.States.FLOATING);
            }
            
        } 
    }
    
}
