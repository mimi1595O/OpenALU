/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mimi
 */
public abstract class Components {
   protected List<node> inputs;
   protected List<node> outputs;
   
   public Components(){
   this.inputs = new ArrayList<>();
   this.outputs = new ArrayList<>();
   }
   
   public abstract void evaluate();
    
}

