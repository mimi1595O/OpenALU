/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package openalu;

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
   Circuit INVERTER_CIRCUIT = new Circuit();
   
   INVERTER_CIRCUIT.add_Input("i1",node.States.LOW);
   INVERTER_CIRCUIT.add_Pmos("i1", "Vdd", "out");
   INVERTER_CIRCUIT.add_Nmos("i1", "out", "Vss");
   INVERTER_CIRCUIT.add_Output("out");
   INVERTER_CIRCUIT.run();
   INVERTER_CIRCUIT.print_outputs();
    
   
   //AND GATE -> NOT(NAND)
   //IN i1 i2
   //P i1, Vdd, nand_out;
   //P i2, Vdd, nand_out;
   //NMOS i2, nand_internal, Vss;
   //NMOS i1, nand_out, nand_internal;
   //PMOS nand_out, Vdd, out;
   //NMOS nand_out, out, Vss;
    
   
   
   Circuit and = new Circuit();
   and.add_Input("i1", node.States.HIGH);
   and.add_Input("i2", node.States.LOW);
   and.add_Pmos("i1", "Vdd", "nand_out");
   and.add_Pmos("i2", "Vdd", "nand_out");
   and.add_Nmos("i2", "nand_internal", "Vss");
   and.add_Nmos("i1", "nand_out", "nand_internal");
   and.add_Pmos("nand_out", "Vdd", "out");
   and.add_Nmos("nand_out", "out", "Vss");
   and.add_Output("out");
   and.run();
   and.print_outputs();
    //END EXAMPLE
    

    }
}

/*
To read file to input:

Syntax IN input1, input2 input3 , ... inputN ;
       TRANSISTOR GATE , SOURCE , DRAIN ;
       OUT output1 , output2, ... outputN ;

IN ARG1 ... ARGN = FOR ARG in ARGs : Circuit.add_input(name , System.in.nextInt(0,1,t,f,T,F,?) ->convert to -> States);
P ARG1 ARG2 ARG3= Circuit.add_pmos(ARG1,ARG2,ARG3);
N ARG1 ARG2 ARG3= Circuit.add_nmos(ARG1,ARG2,ARG3);
OUT ARG1... ARGN = FOR ARG in ARGs : Circuit.add_output();
Circuit.run();
Circuit.print_outputs();

*/