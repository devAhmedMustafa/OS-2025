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
            
            // Check for redirection operators
            if (input.contains(" > ") || input.contains(" >> ")) {
                String[] parts;
                if (input.contains(" >> ")) {
                    parts = input.split(" >> ", 2);
                    commandName = parts[0].trim();
                    args = new String[1];
                    args[0] = ">>" + parts[1].trim();
                } else {
                    parts = input.split(" > ", 2);
                    commandName = parts[0].trim();
                    args = new String[1];
                    args[0] = ">" + parts[1].trim();
                }
                
                // Parse the command part
                String[] commandParts = commandName.split(" ");
                commandName = commandParts[0];
                String[] newArgs = new String[commandParts.length - 1 + 1]; // +1 for redirection
                
                for (int i = 1; i < commandParts.length; i++) {
                    newArgs[i-1] = commandParts[i];
                }
                newArgs[commandParts.length - 1] = args[0]; // Add redirection info
                args = newArgs;
                
                return true;
            }
            
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
