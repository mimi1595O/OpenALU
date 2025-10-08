package openalu;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mimi
 */
public class node {
    String name;
    enum States {LOW,HIGH,FLOATING};
    States state;
    boolean IsConst;
    
    void set_State(States newState){
        if(IsConst){return;}
        state = newState;
    
    }
    
    States get_State(){
    
    return state;}
    node(String name, States state, boolean IsConst){
        this.name = name;
        this.state = state;
        this.IsConst = IsConst;
    }
    node(String name){
    this.name = name;
    this.state = States.FLOATING;
    this.IsConst = false;
    }
    
}
