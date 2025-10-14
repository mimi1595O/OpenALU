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
public class Nand extends Components {
    public Nand(node A, node B, node... outputs){
        super();
        this.inputs.add(A);
        this.inputs.add(B);
        this.outputs.addAll(Arrays.asList(outputs));
    }
    
    
    @Override
    public void evaluate() {
        node a = inputs.get(0);
        node b = inputs.get(1);
        
        if(a.get_State() == node.States.HIGH && b.get_State() == node.States.HIGH) for (node output : outputs) output.set_State((node.States.LOW)); else for(node output : outputs) output.set_State(node.States.HIGH);
        
        
    }
    
}
