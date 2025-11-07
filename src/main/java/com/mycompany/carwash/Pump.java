/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carwash;

import java.util.Queue;

/**
 *
 * @author lightum
 */
// Pump.java
public class Pump extends Thread {
    private int id;
    private Queue<Car> queue;
    private Semaphore mutex, empty, full;

    public Pump(int id, Queue<Car> queue, Semaphore mutex, Semaphore empty, Semaphore full) {
        this.id = id;
        this.queue = queue;
        this.mutex = mutex;
        this.empty = empty;
        this.full = full;
    }

    public void run() {
        while (true) {
            try {
                full.P();
                mutex.P();
                Car car = queue.poll();
                System.out.println("Pump " + id + " starts servicing Car " + car.getNumber());
                mutex.V();
                empty.V();

                Thread.sleep((int) (Math.random() * 4000 + 2000));
                System.out.println("Pump " + id + " finished servicing Car " + car.getNumber());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
