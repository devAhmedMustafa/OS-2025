/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carwash;

import java.util.Queue;

/** id: 20220684
 *
 * @author lightum
 */
// Car.java
public class Car extends Thread {
    private final int number;
    private Queue<Car> queue;
    private Semaphore mutex, empty, full;

    public Car(int id, Queue<Car> queue, Semaphore mutex, Semaphore empty, Semaphore full) {
        this.number = id;
        this.queue = queue;
        this.mutex = mutex;
        this.empty = empty;
        this.full = full;
    }
    
    public int getNumber(){
        return this.number;
    }

    public void run() {
        System.out.println("Car " + number + " arrives.");
        empty.P();
        mutex.P();
        queue.add(this);
        System.out.println("Car " + number + " enters waiting queue.");
        mutex.V();
        full.V();
    }
}