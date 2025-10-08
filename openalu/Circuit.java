
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mimi
 */
public class Circuit {
     // A map to hold every node (inputs, outputs, internal, Vdd, Vss) by its name for quick access.
    private final Map<String, node> nodes = new HashMap<>();
    // A list of all transistors in the circuit.
    private final List<Transistor> transistors = new ArrayList<>();
    // Lists to remember the names and order of declared inputs and outputs.
    private final List<String> inputNodeNames = new ArrayList<>();
    private final List<String> outputNodeNames = new ArrayList<>();
    
    Circuit(){
        node vddNode = new node("Vdd", node.States.HIGH, true);
        this.nodes.put("Vdd", vddNode);
        node vssNode = new node("Vss", node.States.LOW, true);
        this.nodes.put("Vss", vssNode);
    
    }
    private node get_Or_Create_Node(String name){
    
    return this.nodes.computeIfAbsent(name, node::new);
    }
    
    private node get_Or_Create_Node(String name, int state){
    
    return this.nodes.computeIfAbsent(name, node::new);
    
    
    }
    
    
    public void add_Input(String name, node.States state) {
        this.inputNodeNames.add(name);
        get_Or_Create_Node(name);
        this.nodes.get(name).set_State(state);
    }
    
    public void add_Output(String name){
        this.outputNodeNames.add(name);
        get_Or_Create_Node(name);
    }
    
    public void add_Nmos(String gate_name,String source_name, String drain_name){
    node gate = get_Or_Create_Node(gate_name);
    node source = get_Or_Create_Node(source_name);
    node drain = get_Or_Create_Node(drain_name);
    transistors.add(new Nmos(gate,source,drain));
    }
    
    public void add_Pmos(String gate_name,String source_name , String drain_name){
    node gate = get_Or_Create_Node(gate_name);
    node source = get_Or_Create_Node(source_name);
    node drain = get_Or_Create_Node(drain_name);
    transistors.add(new Pmos(gate,source,drain));
    }
    
    public void run(){
        while(true){
        boolean state_changed = false;
        for (Transistor transistor : transistors){
            if(transistor.is_close_circuited()){
              node source = transistor.source;
              node drain = transistor.drain;
              if(source.get_State() != node.States.FLOATING && source.get_State() != drain.get_State())
              {drain.set_State(source.get_State()); state_changed = true;}
              else if(drain.get_State() != node.States.FLOATING && drain.get_State() != source.get_State())
              {source.set_State(drain.get_State()); state_changed = true;}
              
                }
    
            }
        if(!state_changed) break;
        }   
    }
    
    public void print_outputs() {
        System.out.print("Result: ");
        for (String name : outputNodeNames) {
            node n = nodes.get(name);
            if (n.state == node.States.HIGH) System.out.print("1 ");
            
            
            else if (n.get_State()== node.States.LOW) System.out.print("0 ");
            
            else System.out.print("? "); // '?' for FLOATING/unstable
            
        }
        System.out.println();
    }
}
