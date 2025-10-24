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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.io.PrintWriter;
import java.io.FileWriter;

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
    
    public void rmdir(String[] args) {
        if (args.length == 0) {
            System.out.println("rmdir: missing operand");
            return;
        }
        
        if (args.length == 1 && args[0].equals("*")) {
            // Remove all empty directories in current directory
            try {
                File currentDirFile = new File(System.getProperty("user.dir"));
                File[] files = currentDirFile.listFiles();
                
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory() && file.list() != null && file.list().length == 0) {
                            if (file.delete()) {
                                System.out.println("Removed directory: " + file.getName());
                            } else {
                                System.out.println("rmdir: cannot remove '" + file.getName() + "': Directory not empty or permission denied");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("rmdir: " + e.getMessage());
            }
        } else {
            // Remove specific directories
            for (String dirName : args) {
                try {
                    Path dirPath;
                    if (Paths.get(dirName).isAbsolute()) {
                        dirPath = Paths.get(dirName);
                    } else {
                        dirPath = Paths.get(System.getProperty("user.dir")).resolve(dirName);
                    }
                    
                    File dir = dirPath.toFile();
                    
                    if (!dir.exists()) {
                        System.out.println("rmdir: cannot remove '" + dirName + "': No such file or directory");
                        continue;
                    }
                    
                    if (!dir.isDirectory()) {
                        System.out.println("rmdir: cannot remove '" + dirName + "': Not a directory");
                        continue;
                    }
                    
                    if (dir.list() != null && dir.list().length > 0) {
                        System.out.println("rmdir: cannot remove '" + dirName + "': Directory not empty");
                        continue;
                    }
                    
                    if (dir.delete()) {
                        // Directory removed successfully
                    } else {
                        System.out.println("rmdir: cannot remove '" + dirName + "': Permission denied");
                    }
                    
                } catch (Exception e) {
                    System.out.println("rmdir: cannot remove '" + dirName + "': " + e.getMessage());
                }
            }
        }
    }
    
    public void touch(String[] args) {
        if (args.length == 0) {
            System.out.println("touch: missing file operand");
            return;
        }
        
        for (String fileName : args) {
            try {
                Path filePath;
                if (Paths.get(fileName).isAbsolute()) {
                    filePath = Paths.get(fileName);
                } else {
                    filePath = Paths.get(System.getProperty("user.dir")).resolve(fileName);
                }
                
                File file = filePath.toFile();
                
                if (file.exists()) {
                    // File exists, update its timestamp
                    file.setLastModified(System.currentTimeMillis());
                } else {
                    // Create parent directories if they don't exist
                    File parentDir = file.getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    
                    // Create the file
                    if (file.createNewFile()) {
                        // File created successfully
                    } else {
                        System.out.println("touch: cannot create file '" + fileName + "': Permission denied");
                    }
                }
                
            } catch (IOException e) {
                System.out.println("touch: cannot create file '" + fileName + "': " + e.getMessage());
            } catch (Exception e) {
                System.out.println("touch: cannot create file '" + fileName + "': " + e.getMessage());
            }
        }
    }
    
    public void cp(String[] args) {
        if (args.length < 2) {
            System.out.println("cp: missing file operand");
            return;
        }
        
        boolean recursive = false;
        String sourceFile;
        String destFile;
        
        // Check for -r flag
        if (args.length == 3 && args[0].equals("-r")) {
            recursive = true;
            sourceFile = args[1];
            destFile = args[2];
        } else if (args.length == 2) {
            sourceFile = args[0];
            destFile = args[1];
        } else {
            System.out.println("cp: invalid arguments");
            return;
        }
        
        try {
            Path sourcePath;
            Path destPath;
            
            // Resolve source path
            if (Paths.get(sourceFile).isAbsolute()) {
                sourcePath = Paths.get(sourceFile);
            } else {
                sourcePath = Paths.get(System.getProperty("user.dir")).resolve(sourceFile);
            }
            
            // Resolve destination path
            if (Paths.get(destFile).isAbsolute()) {
                destPath = Paths.get(destFile);
            } else {
                destPath = Paths.get(System.getProperty("user.dir")).resolve(destFile);
            }
            
            File source = sourcePath.toFile();
            File dest = destPath.toFile();
            
            if (!source.exists()) {
                System.out.println("cp: cannot stat '" + sourceFile + "': No such file or directory");
                return;
            }
            
            if (recursive) {
                // Recursive copy for directories
                if (!source.isDirectory()) {
                    System.out.println("cp: '" + sourceFile + "': Not a directory");
                    return;
                }
                
                if (dest.exists() && !dest.isDirectory()) {
                    System.out.println("cp: '" + destFile + "': Not a directory");
                    return;
                }
                
                // Create destination directory if it doesn't exist
                if (!dest.exists()) {
                    dest.mkdirs();
                }
                
                // Copy directory recursively
                copyDirectoryRecursively(sourcePath, destPath);
                
            } else {
                // Regular file copy
                if (!source.isFile()) {
                    System.out.println("cp: '" + sourceFile + "': Not a regular file");
                    return;
                }
                
                // Create parent directories if they don't exist
                File parentDir = dest.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                
                // Copy the file
                Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            }
            
        } catch (IOException e) {
            System.out.println("cp: cannot copy '" + sourceFile + "' to '" + destFile + "': " + e.getMessage());
        } catch (Exception e) {
            System.out.println("cp: " + e.getMessage());
        }
    }
    
    private void copyDirectoryRecursively(Path source, Path dest) throws IOException {
        Files.walk(source).forEach(sourcePath -> {
            try {
                Path destPath = dest.resolve(source.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(destPath);
                } else {
                    Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                System.out.println("cp: error copying '" + sourcePath + "': " + e.getMessage());
            }
        });
    }
    
    public void rm(String[] args) {
        if (args.length == 0) {
            System.out.println("rm: missing operand");
            return;
        }
        
        for (String fileName : args) {
            try {
                Path filePath;
                if (Paths.get(fileName).isAbsolute()) {
                    filePath = Paths.get(fileName);
                } else {
                    filePath = Paths.get(System.getProperty("user.dir")).resolve(fileName);
                }
                
                File file = filePath.toFile();
                
                if (!file.exists()) {
                    System.out.println("rm: cannot remove '" + fileName + "': No such file or directory");
                    continue;
                }
                
                if (!file.isFile()) {
                    System.out.println("rm: cannot remove '" + fileName + "': Is a directory");
                    continue;
                }
                
                if (file.delete()) {
                    // File removed successfully
                } else {
                    System.out.println("rm: cannot remove '" + fileName + "': Permission denied");
                }
                
            } catch (Exception e) {
                System.out.println("rm: cannot remove '" + fileName + "': " + e.getMessage());
            }
        }
    }
    
    public void cat(String[] args) {
        if (args.length == 0) {
            System.out.println("cat: missing file operand");
            return;
        }
        
        for (String fileName : args) {
            try {
                Path filePath;
                if (Paths.get(fileName).isAbsolute()) {
                    filePath = Paths.get(fileName);
                } else {
                    filePath = Paths.get(System.getProperty("user.dir")).resolve(fileName);
                }
                
                File file = filePath.toFile();
                
                if (!file.exists()) {
                    System.out.println("cat: " + fileName + ": No such file or directory");
                    continue;
                }
                
                if (!file.isFile()) {
                    System.out.println("cat: " + fileName + ": Is a directory");
                    continue;
                }
                
                // Read and display file contents
                List<String> lines = Files.readAllLines(filePath);
                for (String line : lines) {
                    System.out.println(line);
                }
                
            } catch (IOException e) {
                System.out.println("cat: " + fileName + ": " + e.getMessage());
            } catch (Exception e) {
                System.out.println("cat: " + fileName + ": " + e.getMessage());
            }
        }
    }
    
    public void wc(String[] args) {
        if (args.length == 0) {
            System.out.println("wc: missing file operand");
            return;
        }
        
        for (String fileName : args) {
            try {
                Path filePath;
                if (Paths.get(fileName).isAbsolute()) {
                    filePath = Paths.get(fileName);
                } else {
                    filePath = Paths.get(System.getProperty("user.dir")).resolve(fileName);
                }
                
                File file = filePath.toFile();
                
                if (!file.exists()) {
                    System.out.println("wc: " + fileName + ": No such file or directory");
                    continue;
                }
                
                if (!file.isFile()) {
                    System.out.println("wc: " + fileName + ": Is a directory");
                    continue;
                }
                
                // Read file content
                String content = new String(Files.readAllBytes(filePath));
                
                // Count lines
                int lines = content.split("\n", -1).length;
                if (content.endsWith("\n")) {
                    lines--; // Don't count empty line at end
                }
                
                // Count words (split by whitespace)
                String[] words = content.trim().split("\\s+");
                int wordCount = words.length == 1 && words[0].isEmpty() ? 0 : words.length;
                
                // Count characters (including spaces)
                int characters = content.length();
                
                // Display in format: lines words characters filename
                System.out.println(lines + " " + wordCount + " " + characters + " " + fileName);
                
            } catch (IOException e) {
                System.out.println("wc: " + fileName + ": " + e.getMessage());
            } catch (Exception e) {
                System.out.println("wc: " + fileName + ": " + e.getMessage());
            }
        }
    }
    
    private boolean hasRedirection(String[] args) {
        if (args.length == 0) return false;
        String lastArg = args[args.length - 1];
        return lastArg.startsWith(">") || lastArg.startsWith(">>");
    }
    
    private String[] getCommandArgs(String[] args) {
        if (!hasRedirection(args)) return args;
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 0, commandArgs, 0, args.length - 1);
        return commandArgs;
    }
    
    private String getRedirectionFile(String[] args) {
        if (!hasRedirection(args)) return null;
        String lastArg = args[args.length - 1];
        if (lastArg.startsWith(">>")) {
            return lastArg.substring(2);
        } else if (lastArg.startsWith(">")) {
            return lastArg.substring(1);
        }
        return null;
    }
    
    private boolean isAppendRedirection(String[] args) {
        if (!hasRedirection(args)) return false;
        String lastArg = args[args.length - 1];
        return lastArg.startsWith(">>");
    }
    
    private void executeWithRedirection(String command, String[] args, String outputFile, boolean append) {
        try {
            // Create a temporary file to capture output
            File tempFile = File.createTempFile("cli_output", ".tmp");
            tempFile.deleteOnExit();
            
            // Redirect System.out to the temp file
            PrintWriter originalOut = new PrintWriter(System.out);
            PrintWriter fileWriter = new PrintWriter(new FileWriter(tempFile));
            System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(tempFile)));
            
            // Execute the command
            executeCommand(command, getCommandArgs(args));
            
            // Restore System.out
            System.setOut(originalOut);
            fileWriter.close();
            
            // Copy temp file to target file
            Path tempPath = tempFile.toPath();
            Path targetPath = Paths.get(System.getProperty("user.dir")).resolve(outputFile);
            
            if (append) {
                // Append to existing file
                Files.write(targetPath, Files.readAllBytes(tempPath), 
                          java.nio.file.StandardOpenOption.CREATE, 
                          java.nio.file.StandardOpenOption.APPEND);
            } else {
                // Overwrite file
                Files.copy(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
            
        } catch (Exception e) {
            System.out.println("Redirection error: " + e.getMessage());
        }
    }
    
    private void executeCommand(String command, String[] args) {
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
            case "rmdir":
                rmdir(args);
                break;
            case "touch":
                touch(args);
                break;
            case "cp":
                cp(args);
                break;
            case "rm":
                rm(args);
                break;
            case "cat":
                cat(args);
                break;
            case "wc":
                wc(args);
                break;
            default:
                System.out.println("Command '" + command + "' not found");
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
                
                if (command.equals("exit")) {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                
                if (hasRedirection(args)) {
                    String outputFile = getRedirectionFile(args);
                    boolean append = isAppendRedirection(args);
                    executeWithRedirection(command, args, outputFile, append);
                } else {
                    executeCommand(command, args);
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
