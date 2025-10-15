/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package openalu;
import java.io.*;
import java.util.*;
/**
 *
 * @author USER
 */
public class Circuit_file_interpreter {
    

    private final Circuit circuit;
    private final Scanner scanner;

    public Circuit_file_interpreter(Circuit circuit) {
        this.circuit = circuit;
        this.scanner = new Scanner(System.in);
    }

    public void interpretFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            int n_line_counter = 0; //for checking which real \n line is the errror coming from.
            while ((line = reader.readLine()) != null) {
                n_line_counter++;
                
                int commentIndex = line.indexOf('#');
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex);
                }

                // 2. Split the line into multiple commands using the semicolon
                String[] commands = line.split(";");
                for (String command : commands){
                    String final_command = command.trim();
                    if (!final_command.isEmpty()){
                        if (parseLine(final_command)){ //If error
                            System.out.println("\tError from line "+n_line_counter);
                            
                        };
                        
                    }
                }
            }
        }

        circuit.run();
        circuit.print_outputs();
    }

    private boolean parseLine(String line) { //Returns true if error
        
        
        if (line.startsWith("IN ")) {
            handleInputs(line.substring(3).trim());
        } else if (line.startsWith("P ")) {
            handleTransistor(line.substring(2).trim(), true);
        } else if (line.startsWith("N ")) {
            handleTransistor(line.substring(2).trim(), false);
        } else if (line.startsWith("OUT ")) {
            handleOutputs(line.substring(4).trim());
        } else if (line.startsWith("NAND ")) {
            handleNand(line.substring(5).trim());
        } else if (line.startsWith("NOT ")) {
            handleNot(line.substring(4).trim());
        } else {
            System.out.println( "Error: Unknown component. ("+line.substring(0, line.indexOf(' '))+")" );
            return true;
        }
        return false;
    }

    private void handleInputs(String inputsLine) {
        String[] inputs = inputsLine.split("[,\\s]+"); // comma or space
        for (String input : inputs) {
            node.States state = readStateFromUser(input);
            circuit.add_Input(input, state);
        }
    }

    private node.States readStateFromUser(String name) {
        while (true) {
            System.out.print("Enter state for input \"" + name + "\" (0,1,t,f,T,F,?): ");
            String val = scanner.nextLine().trim().toLowerCase();

            switch (val) {
                case "1", "t", "true" -> {
                    return node.States.HIGH;
                }
                case "0", "f", "false" -> {
                    return node.States.LOW;
                }
                case "?" -> {
                    return node.States.FLOATING;
                }
                default -> System.out.println("Invalid input. Try again.");
            }
        }
    }

    private void handleTransistor(String line, boolean isPmos) {
        String[] tokens = line.split(",");
        if (tokens.length != 3) {
            System.out.println("Invalid transistor format: " + line);
            return;
        }

        String gate = tokens[0].trim();
        String source = tokens[1].trim();
        String drain = tokens[2].trim();

        if (isPmos) {
            circuit.add_Pmos(gate, source, drain);
        } else {
            circuit.add_Nmos(gate, source, drain);
        }
    }

    private void handleOutputs(String outputsLine) {
        String[] outputs = outputsLine.split("[,\\s]+");
        for (String output : outputs) {
            circuit.add_Output(output.trim());
        }
    }

    // Optional: future implementation of NAND gate macro
    private void handleNand(String line) {
        String[] tokens = line.split("[,\\s]+");
        if (tokens.length < 3) {
            System.out.println("Invalid NAND format: " + line);
            return;
        }

        String in1 = tokens[0], in2 = tokens[1];
        List<String> outputs = new ArrayList<>();
        for (int i =2; i < tokens.length; ++i) outputs.add(tokens[i]);
        
        circuit.add_nand(in1, in2, outputs.toArray(String[]::new));
    }

    // Optional: future implementation of NOT gate macro
    private void handleNot(String line) {
        String[] tokens = line.split("[,\\s]+");
        if (tokens.length < 2) {
            System.out.println("Invalid NOT format: " + line);
            return;
        }

        String input = tokens[0];
        List<String> outputs = new ArrayList<>();
        for (int i = 1; i < tokens.length; ++i) outputs.add(tokens[i]);
       circuit.add_inverter(input, outputs.toArray(String[]::new));
    }
}


