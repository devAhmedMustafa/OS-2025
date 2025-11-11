/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carwash;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author lightum
 */

class Semaphore {
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
        notifyAll();
    }
}

class Car extends Thread {
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

class Pump extends Thread {
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
                System.out.println("Pump" + id + ": " + "Car " + car.getNumber() + " login");
                System.out.println("Pump " + id + " starts servicing Car " + car.getNumber());
                mutex.V();
                empty.V();

                Thread.sleep((int) (Math.random() * 4000 + 2000));
                System.out.println("Pump " + id + " finished servicing Car " + car.getNumber());
                System.out.println("Bay " + id + " is now free");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ServiceStation {

    public static Queue<Car> waitingQueue;
    public static Semaphore mutex;
    public static Semaphore empty;
    public static Semaphore full;

    public static int maxWaitingArea;
    public static int numOfPumps;
    public static int numOfCars;

    public static void run() {
        try {
            java.util.Scanner sc = new java.util.Scanner(System.in);

            System.out.print("Enter number of pumps: ");
            numOfPumps = sc.nextInt();

            System.out.print("Enter size of waiting area (1-10): ");
            maxWaitingArea = sc.nextInt();
            
            System.out.print("Enter size of cars: ");
            numOfCars = sc.nextInt();

            if (maxWaitingArea < 1 || maxWaitingArea > 10) {
                System.out.println("Invalid waiting area size! Must be between 1 and 10.");
                sc.close();
                return;
            }

            waitingQueue = new LinkedList<>();
            mutex = new Semaphore(1);
            empty = new Semaphore(maxWaitingArea);
            full = new Semaphore(0);

            for (int i = 0; i < numOfPumps; i++) {
                Pump pump = new Pump(i + 1, waitingQueue, mutex, empty, full);
                pump.start();
            }

            int carId = 1;
            while (carId < numOfCars) {
                Car car = new Car(carId++, waitingQueue, mutex, empty, full);
                car.start();

                Thread.sleep((int) (Math.random() * 2000 + 1000));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

public class CarWash {
    public static void main(String[] args){
        ServiceStation.run();
    }
}
