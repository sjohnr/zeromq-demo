package org.zeromq.demo;

import org.zeromq.ContextFactory;
import org.zeromq.api.Context;
import org.zeromq.api.Message;
import org.zeromq.api.Socket;
import org.zeromq.api.SocketType;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Example6 {
    private static final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Example6().run();
    }

    public void run() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Context context = ContextFactory.createContext(1);
        Socket push = context.buildSocket(SocketType.PUSH).bind("inproc://work-queue");
        for (int n = 1; n <= 10; n++) {
            threadPool.execute(new Puller(context));
        }
        for (int task = 1; task <= 10000; task++) {
            push.send(new Message(task));
        }

        try {
            Thread.sleep(600 * 1000);
        } catch (InterruptedException ignored) {
        }
        running.set(false);
        context.close();
        threadPool.shutdown();
    }
    
    class Puller implements Runnable {
        private Context context;

        public Puller(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            Socket pull = context.buildSocket(SocketType.PULL).connect("inproc://work-queue");
            while (running.get()) {
                int task = pull.receiveMessage().popInt();
                System.out.println("Task #" + task);
                doWork(task);
            }
            System.out.println("Exiting");
        }

        private void doWork(int task) {
            try {
                Thread.sleep(new Random().nextInt(5) * 1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
