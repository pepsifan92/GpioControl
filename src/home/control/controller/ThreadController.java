package home.control.controller;

import home.control.Thread.PwmFadeThread;
import home.control.model.PinConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThreadController {

    private static List<Thread> runningThreads;

    public ThreadController() {
        runningThreads = new ArrayList<Thread>();
    }

    /**
     * Determines if an Thread is currently running on a specific thread, and stops it
     * @param thread
     */
    public static void addAndStart(Thread thread, PinConfiguration conf) {
        thread.setName("" + conf.getNumber());
        interruptThreadWhenRunningOnPin(conf.getNumber());
        runningThreads.add(thread);
        thread.start();
    }

    public static void remove(Thread thread) {
        runningThreads.remove(thread);
    }

    private static void interruptThreadWhenRunningOnPin(int number) {
        Iterator<Thread> iter = runningThreads.iterator();
        while (iter.hasNext()) {
            Thread thread = iter.next();
            if (thread.getName().equals("" + number)) {
                System.out.println("Killing Thread Running on Pin " + number);
                iter.remove();
                if (thread instanceof PwmFadeThread) {
                    ((PwmFadeThread) thread).kill();
                }
            }
        }
    }

    public static void printThreads() {
        System.out.println("Currently Running Threads: " + runningThreads.size());
        for (Thread thread : runningThreads) {
            System.out.println("Thread Running on Pin " + thread.getName());
        }
    }

}
