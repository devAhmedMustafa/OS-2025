/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cli;

import java.util.Scanner;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
    
    public void cd(String[] args) {
        try {
            String targetDir;
            
            if (args.length == 0) {
                // No arguments - go to home directory
                targetDir = System.getProperty("user.home");
            } else if (args.length == 1) {
                if (args[0].equals("..")) {
                    // Go to parent directory
                    Path currentPath = Paths.get(System.getProperty("user.dir"));
                    Path parentPath = currentPath.getParent();
                    if (parentPath != null) {
                        targetDir = parentPath.toString();
                    } else {
                        System.out.println("Already at root directory");
                        return;
                    }
                } else {
                    // Go to specified directory (full or relative path)
                    targetDir = args[0];
                }
            } else {
                System.out.println("cd: too many arguments");
                return;
            }
            
            // Resolve the path
            Path targetPath;
            if (Paths.get(targetDir).isAbsolute()) {
                targetPath = Paths.get(targetDir);
            } else {
                targetPath = Paths.get(System.getProperty("user.dir")).resolve(targetDir);
            }
            
            // Check if directory exists and is actually a directory
            File targetFile = targetPath.toFile();
            if (!targetFile.exists()) {
                System.out.println("cd: " + targetDir + ": No such file or directory");
                return;
            }
            if (!targetFile.isDirectory()) {
                System.out.println("cd: " + targetDir + ": Not a directory");
                return;
            }
            
            // Change to the directory
            System.setProperty("user.dir", targetPath.toString());
            currentDir = targetPath.toString();
            
        } catch (Exception e) {
            System.out.println("cd: " + args[0] + ": " + e.getMessage());
        }
    }
    
    public void ls() {
        try {
            File currentDirFile = new File(System.getProperty("user.dir"));
            File[] files = currentDirFile.listFiles();
            
            if (files == null) {
                System.out.println("ls: cannot access current directory");
                return;
            }
            
            // Sort files alphabetically by name
            Arrays.sort(files, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            
            for (File file : files) {
                System.out.println(file.getName());
            }
            
        } catch (Exception e) {
            System.out.println("ls: " + e.getMessage());
        }
    }
    
    public void mkdir(String[] args) {
        if (args.length == 0) {
            System.out.println("mkdir: missing operand");
            return;
        }
        
        for (String dirName : args) {
            try {
                Path dirPath;
                if (Paths.get(dirName).isAbsolute()) {
                    dirPath = Paths.get(dirName);
                } else {
                    dirPath = Paths.get(System.getProperty("user.dir")).resolve(dirName);
                }
                
                File dir = dirPath.toFile();
                
                if (dir.exists()) {
                    System.out.println("mkdir: cannot create directory '" + dirName + "': File exists");
                    continue;
                }
                
                if (dir.mkdirs()) {
                    // Directory created successfully
                } else {
                    System.out.println("mkdir: cannot create directory '" + dirName + "': No such file or directory");
                }
                
            } catch (Exception e) {
                System.out.println("mkdir: cannot create directory '" + dirName + "': " + e.getMessage());
            }
        }
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
                    case "cd":
                        cd(args);
                        break;
                    case "ls":
                        ls();
                        break;
                    case "mkdir":
                        mkdir(args);
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
