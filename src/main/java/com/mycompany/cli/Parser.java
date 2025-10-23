/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cli;

/**
 *
 * @author lightum
 */
public class Parser {
    private String commandName;
    private String[] args;
    
    public boolean parse(String input){
        
        try {    
        
            if (input.length() == 0)
                return false;
            
            String[] parsed = input.split(" ");
            commandName = parsed[0];
            args = new String[parsed.length-1];

            for (int i = 1; i < parsed.length; i++){
                args[i-1] = parsed[i];
            }
            
            return true;
        }
        catch(Exception e){
            throw e;
        }
        
    }
    
    public String getCommandName(){
        return commandName;
    }
    
    public String[] getArgs(){
        return args;
    }
}
