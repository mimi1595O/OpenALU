// Circuit_file_interpreter.java (Final Version with Full Error Context)
package openalu;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Circuit_file_interpreter {

    private final Circuit circuit;
    private final Scanner scanner;

    // A map of component blueprints (class definitions)
    private final Map<String, ComponentBlueprint> blueprints = new HashMap<>();
    private final List<String> inputNodeNames = new ArrayList<>();
    private int instanceCounter = 0;

    // Helper class to store a blueprint's internal command and its original line number
    private static class BlueprintCommand {
        final int lineNumber;
        final String command;

        BlueprintCommand(int lineNumber, String command) {
            this.lineNumber = lineNumber;
            this.command = command;
        }
    }

    // Updated ComponentBlueprint to use the helper class
    private static class ComponentBlueprint {
        String name;
        List<String> orderedPorts = new ArrayList<>();
        List<BlueprintCommand> internalNetlist = new ArrayList<>();
    }


    public Circuit_file_interpreter(Circuit circuit) {
        this.circuit = circuit;
        this.scanner = new Scanner(System.in);
    }

    public void interpretFile(String filePath) throws IOException {
        scanForBlueprints(filePath);
        System.out.println("Found blueprints: " + blueprints.keySet());

        buildCircuit(filePath);

        handleInputs();
        circuit.run();
        circuit.print_outputs();
    }

    private void scanForBlueprints(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            ComponentBlueprint currentBlueprint = null;
            int lineCounter = 0; 
            boolean inClassBody = false;

            while ((line = reader.readLine()) != null) {
                lineCounter++;
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) continue;

                if (trimmedLine.startsWith("class ")) {
                    Pattern p = Pattern.compile("class\\s+(\\w+)\\s*\\((.*)\\)\\s*\\{");
                    Matcher m = p.matcher(trimmedLine);
                    if (m.find()) {
                        currentBlueprint = new ComponentBlueprint();
                        currentBlueprint.name = m.group(1);
                        parsePorts(currentBlueprint, m.group(2));
                        inClassBody = true;
                    }
                } else if (inClassBody) {
                    if (trimmedLine.startsWith("}")) {
                        if (currentBlueprint != null) {
                            blueprints.put(currentBlueprint.name, currentBlueprint);
                        }
                        currentBlueprint = null;
                        inClassBody = false;
                    } else if (currentBlueprint != null) {
                        // We are inside a class, so process the commands
                        String[] commands = trimmedLine.split(";");
                        for (String cmd : commands) {
                            if (!cmd.trim().isEmpty()) {
                                // Store the command with its original line number
                                currentBlueprint.internalNetlist.add(new BlueprintCommand(lineCounter, cmd.trim()));
                            }
                        }
                    }
                }
            }
        }
    }

    private void parsePorts(ComponentBlueprint blueprint, String portsStr) {
        String[] portDeclarations = portsStr.split(";");
        for (String decl : portDeclarations) {
            String[] parts = decl.trim().split("[\\s,]+");
            for (int i = 1; i < parts.length; i++) {
                blueprint.orderedPorts.add(parts[i]);
            }
        }
    }

    private void buildCircuit(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inClassBody = false;
            int lineCounter = 0;

            while ((line = reader.readLine()) != null) {
                lineCounter++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith("class ")) {
                    inClassBody = true;
                    continue;
                }
                if (inClassBody && line.startsWith("}")) {
                    inClassBody = false;
                    continue;
                }
                if (inClassBody) continue;

                String[] commands = line.split(";");
                for (String command : commands) {
                    if (!command.trim().isEmpty()) {
                        parseLine(command.trim(), lineCounter);
                    }
                }
            }
        }
    }

    private boolean parseLine(String line, int lineNum) {
        String[] tokens = line.split("[\\s,]+");
        String command = tokens[0].toUpperCase();
        String argsLine = line.substring(command.length()).trim();

        if (blueprints.containsKey(command)) {
            handleComponentInstantiation(tokens, blueprints.get(command), lineNum);
            return true;
        }

        switch (command) {
            case "IN": handleInputsDeclaration(argsLine); break;
            case "OUT": handleOutputs(argsLine); break;
            case "NAND": handleNand(argsLine, lineNum); break;
            case "NOT": handleNot(argsLine, lineNum); break;
            case "P": handleTransistor(argsLine, true, lineNum); break;
            case "N": handleTransistor(argsLine, false, lineNum); break;
            default:
                System.out.println("Error on line " + lineNum + ": Unknown command '" + command + "'");
                return false;
        }
        return true;
    }

    private void handleComponentInstantiation(String[] tokens, ComponentBlueprint blueprint, int instantiationLineNum) {
        String instanceName = blueprint.name + "_" + (instanceCounter++);
        System.out.println("Instantiating " + instanceName);

        Map<String, String> portMapping = new HashMap<>();
        List<String> instanceArgs = new ArrayList<>(Arrays.asList(tokens).subList(1, tokens.length));

        if (instanceArgs.size() != blueprint.orderedPorts.size()) {
            System.err.println("Error on line " + instantiationLineNum + ": Instantiating '" + blueprint.name + "' failed. Expected " + blueprint.orderedPorts.size() + " arguments but got " + instanceArgs.size() + ".");
            return;
        }
        for (int i = 0; i < blueprint.orderedPorts.size(); i++) {
            portMapping.put(blueprint.orderedPorts.get(i), instanceArgs.get(i));
        }

        for (BlueprintCommand bpCommand : blueprint.internalNetlist) {
            String internalLine = bpCommand.command;
            int originalLineNum = bpCommand.lineNumber;

            String[] internalTokens = internalLine.split("[\\s,]+");
            StringBuilder expandedLine = new StringBuilder(internalTokens[0] + " ");

            for (int i = 1; i < internalTokens.length; i++) {
                String token = internalTokens[i];
                if (portMapping.containsKey(token)) {
                    expandedLine.append(portMapping.get(token)).append(",");
                } else {
                    expandedLine.append(instanceName).append("_").append(token).append(",");
                }
            }
            parseLine(expandedLine.substring(0, expandedLine.length() - 1), originalLineNum);
        }
    }

    private void handleInputsDeclaration(String line) {
        String[] inputs = line.split("[,\\s]+");
        for (String input : inputs) {
            if (!input.isEmpty()) inputNodeNames.add(input);
        }
    }

    private void handleInputs() {
        System.out.println("--- Please provide input values (1 for HIGH, 0 for LOW) ---");
        for (String name : inputNodeNames) {
            while (true) {
                System.out.print(name + " = ");
                String input = scanner.nextLine().trim();
                if ("1".equals(input) || "t".equalsIgnoreCase(input)) {
//                    circuit.add_Input(name, node.States.HIGH);
                    circuit.add_Input(name);
                    break;
                } else if ("0".equals(input) || "f".equalsIgnoreCase(input)) {
//                    circuit.add_Input(name, node.States.LOW);
                    circuit.add_Input(name);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 1 or 0.");
                }
            }
        }
    }

    private void handleOutputs(String outputsLine) {
        String[] outputs = outputsLine.split("[,\\s]+");
        for (String output : outputs) {
            circuit.add_Output(output.trim());
        }
    }

    private void handleTransistor(String line, boolean isPmos, int lineNum) {
        String[] tokens = line.split("[,\\s]+");
        if (tokens.length != 3) {
            String type = isPmos ? "PMOS" : "NMOS";
            System.err.println("Error on line " + lineNum + ": Invalid " + type + " format. Expected Gate, Source, Drain. Got: " + line);
            return;
        }
        if (isPmos) {
            circuit.add_Pmos(tokens[0].trim(), tokens[1].trim(), tokens[2].trim());
        } else {
            circuit.add_Nmos(tokens[0].trim(), tokens[1].trim(), tokens[2].trim());
        }
    }

    private void handleNand(String line, int lineNum) {
        String[] tokens = line.split("[,\\s]+");
        if (tokens.length < 3) {
            System.err.println("Error on line " + lineNum + ": Invalid NAND format. Expected at least 2 inputs and 1 output. Got: " + line);
            return;
        }
        String in1 = tokens[0], in2 = tokens[1];
        String[] outputs = Arrays.copyOfRange(tokens, 2, tokens.length);
        circuit.add_nand(in1, in2, outputs);
    }

    private void handleNot(String line, int lineNum) {
        String[] tokens = line.split("[,\\s]+");
        if (tokens.length < 2) {
            System.err.println("Error on line " + lineNum + ": Invalid NOT format. Expected at least 1 input and 1 output. Got: " + line);
            return;
        }
        String in = tokens[0];
        String[] outputs = Arrays.copyOfRange(tokens, 1, tokens.length);
        circuit.add_inverter(in, outputs);
    }
}