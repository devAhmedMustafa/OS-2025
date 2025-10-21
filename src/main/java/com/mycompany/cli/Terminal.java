/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cli;

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
    
}
