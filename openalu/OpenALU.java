/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package openalu;

import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author mimi
 */
public class OpenALU {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    //IN I1 ;
    //P I1 Vdd out ; #power and ground are built-in nodes
    //N I1 out Vss ;
    //OUT out ;
    //example INVERTER
   
    //Circuit INVERTER_CIRCUIT = new Circuit();
   //INVERTER_CIRCUIT.add_Input("i1",node.States.HIGH);
   //INVERTER_CIRCUIT.add_Pmos("i1", "Vdd", "out");
   //INVERTER_CIRCUIT.add_Nmos("i1", "out", "Vss");
   //INVERTER_CIRCUIT.add_Output("out");
   //INVERTER_CIRCUIT.run();
   //INVERTER_CIRCUIT.print_outputs();
    
   
   //AND GATE -> NOT(NAND)
   
   
   
   //IN i1, i2;
   //P i1, Vdd, nand_out;
   //P i2, Vdd, nand_out;
   //N i2, nand_internal, Vss;
   //N i1, nand_out, nand_internal;
   //P nand_out, Vdd, out;
   //N nand_out, out, Vss;
   //OUT out;
   
   
   //Circuit and = new Circuit();
   //and.add_Input("i1", node.States.HIGH);
   //and.add_Input("i2", node.States.HIGH);
   //and.add_Pmos("i1", "Vdd", "nand_out");
   //and.add_Pmos("i2", "Vdd", "nand_out");
   //and.add_Nmos("i2", "nand_internal", "Vss");
   //and.add_Nmos("i1", "nand_out", "nand_internal");
   //and.add_Pmos("nand_out", "Vdd", "out");
   //and.add_Nmos("nand_out", "out", "Vss");
   //and.add_Output("out");
   //and.run();
   //and.print_outputs();
    //END EXAMPLE
    
    //Circuit nand = new Circuit();
    ///nand.add_Input("A", node.States.HIGH);
    //nand.add_Input("B", node.States.LOW);
    //nand.add_inverter("B", "B");
    //nand.add_nand("A", "B", "output_1","output_2" );
    
   
    //nand.add_Output("output_1");
    //nand.add_Output("output_2");
    //nand.run();
    //nand.print_outputs();
        Circuit circuit = new Circuit();
        Circuit_file_interpreter interpreter = new Circuit_file_interpreter(circuit);
        Scanner scan = new Scanner(System.in);
        System.out.println("input file name :");
        String filepath = scan.nextLine();
        
        File file = new File(filepath);
        String relativePath = file.getPath();
        
        
        
        System.out.println(Paths.get("").toAbsolutePath());
        
        try {
            interpreter.interpretFile(relativePath);
        } catch (IOException e) {
           System.err.println("Error loading file: " + relativePath);
        }
    }
    
}

/*
To read file to input:

Syntax IN input1, input2 input3 , ... inputN ;
       TRANSISTOR GATE , SOURCE , DRAIN ;
       OUT output1 , output2, ... outputN ;
       NAND input  1, input2, output... N;
       NOT input1 output ... N;

IN ARG1 ... ARGN = FOR ARG in ARGs : Circuit.add_input(name , System.in.nextInt(0,1,t,f,T,F,?) ->convert to -> States);
P ARG1 ARG2 ARG3= Circuit.add_pmos(ARG1,ARG2,ARG3);
N ARG1 ARG2 ARG3= Circuit.add_nmos(ARG1,ARG2,ARG3);
OUT ARG1... ARGN = FOR ARG in ARGs : Circuit.add_output();
Circuit.run();
Circuit.print_outputs();

*/
