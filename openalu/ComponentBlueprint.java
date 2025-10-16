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

/**
 * Represents the definition of a user-defined component (a "class" in the script).
 * It stores the component's interface (inputs/outputs) and its internal netlist.
 */
public class ComponentBlueprint {
    String name;
    List<String> orderedPorts = new ArrayList<>(); // All ports in the order they are declared
    List<String> internalNetlist = new ArrayList<>();

    public ComponentBlueprint(String name) {
        this.name = name;
    }
}

