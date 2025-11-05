/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carwash;

/**
 *
 * @author lightum
 */
public class Semaphore {
    private int value;
    
    public Semaphore(int value){
        this.value = value;
    }
    
    public synchronized void P(){
        while (value < 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        
        value--;
    }
    
    public synchronized void V(){
        value++;
        if (value <= 0) notify();
    }
}
