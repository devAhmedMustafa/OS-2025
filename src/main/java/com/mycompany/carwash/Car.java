/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carwash;

import java.util.Queue;
import java.util.concurrent.locks.Lock;

/** id: 20220684
 *
 * @author lightum
 */
class Car extends Thread {
  private static int carCounter = 1;
    private final int id;
    private final Queue<Car> waitingQueue;
    private final CustomSemaphore empty;
    private final CustomSemaphore full;
    private final Lock mutex;
    private final ServiceStation gui;

    public Car(Queue<Car> waitingQueue, CustomSemaphore empty, CustomSemaphore full, Lock mutex, ServiceStation gui) {
        this.id = carCounter++;
        this.waitingQueue = waitingQueue;
        this.empty = empty;
        this.full = full;
        this.mutex = mutex;
        this.gui = gui;
    }

    public int getCarId() {
        return id;
    }

    @Override
    public void run() {
        gui.logActivity("Car C" + id + " arrived");

        try {
            empty.acquire();

            mutex.lock();
            try {
                waitingQueue.add(this);
                gui.logActivity("Car C" + id + " enters the queue. Queue size: " + waitingQueue.size());
                gui.updateQueueDisplay(waitingQueue.size());
            } finally {
                mutex.unlock();
            }

            full.release();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            gui.logActivity("Car C" + id + " arrival interrupted.");
        }
    }
}
