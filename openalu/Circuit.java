
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; 
/**
 *
 * @author mimi
 */
public class Circuit {
     // A map to hold every node (inputs, outputs, internal, Vdd, Vss) by its name for quick access.
    private final Map<String, node> nodes = new HashMap<>();
    // A list of all transistors in the circuit.
    private final List<Components> components = new ArrayList<>();
    
    
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
    
    
    
    public void add_Input(String name, node.States state) {
        
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
    components.add(new Nmos(gate,source,drain));
    }
    
    public void add_Pmos(String gate_name,String source_name , String drain_name){
    node gate = get_Or_Create_Node(gate_name);
    node source = get_Or_Create_Node(source_name);
    node drain = get_Or_Create_Node(drain_name);
    components.add(new Pmos(gate,source,drain));
    }
    
    public void add_nand(String input_A, String input_B, String ... outputs ){
    node A = get_Or_Create_Node(input_A);
    node B = get_Or_Create_Node(input_B);
    List<node> outputnodes = new ArrayList<>();
    for (String out : outputs) outputnodes.add(get_Or_Create_Node(out) );
    components.add(new Nand(A,B,outputnodes.toArray(node[]::new)));
    }
    
    public void add_inverter(String input, String ... outputs){
        
    node IN =get_Or_Create_Node(input);
    List<node> outputnodes = new ArrayList<>();
    
    for(String out : outputs) outputnodes.add(get_Or_Create_Node(out));
    
    
    components.add(new Inverter(IN, outputnodes.toArray(node[]::new)));
    }
    
    
    public void run() {
    int pass = 0;
    
    while (true) {
        pass++;
        boolean stateChangedInThisPass = false;

        // Create a snapshot of all node states BEFORE this pass
        Map<String, node.States> statesBeforePass = new HashMap<>();
        for (Map.Entry<String, node> entry : nodes.entrySet()) {
            statesBeforePass.put(entry.getKey(), entry.getValue().get_State());
        }

        // Evaluate every component once
        for (Components component : components) {
            component.evaluate();
        }

        // Check if any node's state changed during this pass
        for (Map.Entry<String, node> entry : nodes.entrySet()) {
            if (statesBeforePass.get(entry.getKey()) != entry.getValue().get_State()) {
                stateChangedInThisPass = true;
                break; // A change was found, no need to check further
            }
        }

        // If a full pass resulted in no changes, the circuit is stable.
        if (!stateChangedInThisPass) {
            System.out.println("--- Simulation Stable after " + pass + " passes ---");
            break; // Exit the while loop
        }

        // Safety break for oscillators
        if (pass > 100) {
            System.out.println("Warning: Simulation unstable. Halting after 100 passes.");
            break;
        }
    }
}
    
    public String get_output() {
        StringBuilder sb = new StringBuilder();
        if(outputNodeNames.isEmpty()){
        sb.append("No output node specified");
        }
        else{
        for (String node_name : outputNodeNames){
            char State;
            node n = nodes.get(node_name);
            
            
            switch (n.get_State()){
                case HIGH -> State = '1';
                case LOW -> State = '0';
                default -> State = '?';
                
            
            }sb.append(String.format("%c",State));
        
        }
        }
        
        return sb.toString();
    }
    

public String getJson() {
    StringBuilder json = new StringBuilder();
    json.append("{\n");

    // 1. Add all Nodes
    json.append("  \"nodes\": {\n");
    String nodesJson = nodes.entrySet().stream()
        .map(entry -> {
            node n = entry.getValue();
            return String.format("    \"%s\": {\"state\": \"%s\", \"isConstant\": %b}",
                n.name, n.get_State().toString(), n.IsConst);
        })
        .collect(Collectors.joining(",\n"));
    json.append(nodesJson);
    json.append("\n  },\n");

    // 2. Add all Components
    json.append("  \"components\": [\n");
    String componentsJson = components.stream()
        .map(comp -> {
            String compClass = comp.getClass().getSimpleName();
            String inputsJson = comp.inputs.stream()
                .map(n -> "\"" + n.name + "\"")
                .collect(Collectors.joining(", "));
            String outputsJson = comp.outputs.stream()
                .map(n -> "\"" + n.name + "\"")
                .collect(Collectors.joining(", "));
            return String.format("    {\"type\": \"%s\", \"inputs\": [%s], \"outputs\": [%s]}",
                compClass, inputsJson, outputsJson);
        })
        .collect(Collectors.joining(",\n"));
    json.append(componentsJson);
    json.append("\n  ],\n");

    // 3. Add Input and Output Node Names for reference
    String inputNodesJson = nodes.values().stream()
            .filter(n -> !n.IsConst) // A simple heuristic: inputs are non-constant nodes
            .map(n -> "\"" + n.name + "\"")
            .collect(Collectors.joining(", "));
    json.append("  \"inputs\": [").append(inputNodesJson).append("],\n");

    String outputNodesJson = outputNodeNames.stream()
            .map(name -> "\"" + name + "\"")
            .collect(Collectors.joining(", "));
    json.append("  \"outputs\": [").append(outputNodesJson).append("]\n");


    json.append("}\n");
    return json.toString();
}
    
    
}
