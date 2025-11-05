/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carwash;

/**
 *
 * @author lightum
 */
public class Buffer {
    private int size;
    private Object store[];
    private int inptr = 0, outptr = 0;
    
    Semaphore spaces;
    Semaphore elements;
    Semaphore mutex;
    
    public Buffer(int size){
        this.size = size;
        this.store = new Object[size];
        spaces = new Semaphore(size);
        elements = new Semaphore(0);
        mutex = new Semaphore(1);
    }
    
    public void produce(Object value){
        spaces.P();
        mutex.P();
        store[inptr] = value;
        inptr = (inptr + 1) % size;
        mutex.V();
        elements.V();
    }
    
    public Object consume(){
        Object value;
        elements.P();
        mutex.P();
        value = store[outptr];
        outptr = (outptr + 1) % size;
        mutex.V();
        spaces.V();
        return value;
    }
}
