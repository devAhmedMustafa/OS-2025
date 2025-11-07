/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carwash;

/**
 *
 * @author lightum
 */
import java.util.LinkedList;
import java.util.Queue;

public class ServiceStation {

    public static Queue<Car> waitingQueue;
    public static Semaphore mutex;
    public static Semaphore empty;
    public static Semaphore full;

    public static int maxWaitingArea;
    public static int numOfPumps;

    public static void run() {
        try {
            java.util.Scanner sc = new java.util.Scanner(System.in);

            System.out.print("Enter number of pumps: ");
            numOfPumps = sc.nextInt();

            System.out.print("Enter size of waiting area (1-10): ");
            maxWaitingArea = sc.nextInt();

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
            while (carId < 20) {
                Car car = new Car(carId++, waitingQueue, mutex, empty, full);
                car.start();

                Thread.sleep((int) (Math.random() * 2000 + 1000));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
