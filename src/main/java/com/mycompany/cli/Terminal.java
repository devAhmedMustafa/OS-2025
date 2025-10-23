/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cli;

import java.util.Scanner;

/**
 *
 * @author lightum
 */
public class Terminal {
    
    Parser parser;
    String currentDir;
    
    public Terminal(){
        currentDir = System.getProperty("user.dir");
    }
    
    public String pwd() {
        return currentDir;
    }
    
    public void cd(String targetDir){
        currentDir = targetDir;
    }
    
    public void chooseCommandAction(){
        Scanner scanner = new Scanner(System.in);
        System.out.print(currentDir+"$");
        String line = scanner.nextLine();
        scanner.close();
        
        parser.parse(line);
        
        switch (parser.getCommandName()){
            case "pwd":
                System.out.println(pwd());
            
            default:
                System.out.println("Command "+parser.getCommandName()+ " not found");
        }
    }
    
}
