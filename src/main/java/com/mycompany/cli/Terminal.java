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
        return System.getProperty("user.dir");
    }
    
    public void cd(String targetDir){
        currentDir = targetDir;
    }
    
    public void chooseCommandAction(){
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print(currentDir + "$ ");
            String line = scanner.nextLine().trim();
            
            if (line.isEmpty()) {
                continue;
            }
            
            if (parser.parse(line)) {
                String command = parser.getCommandName();
                String[] args = parser.getArgs();
                
                switch (command) {
                    case "pwd":
                        System.out.println(pwd());
                        break;
                    case "exit":
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Command '" + command + "' not found");
                }
            } else {
                System.out.println("Invalid command format");
            }
        }
    }
    
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.parser = new Parser();
        terminal.chooseCommandAction();
    }
    
}
