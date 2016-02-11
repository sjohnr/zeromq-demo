package org.zeromq.demo;

import org.zeromq.ContextFactory;
import org.zeromq.api.Context;
import org.zeromq.api.Message;
import org.zeromq.api.Socket;
import org.zeromq.api.SocketType;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Example3 {
    private static final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Example3().run();
    }

    public void run() {
        new Thread(new Master()).start();
        for (int n = 1; n <= 10; n++) {
            new Thread(new Worker()).start();
        }
    }

    class Master implements Runnable {
        @Override
        public void run() {
            Context context = ContextFactory.createContext(1);
            Socket req = context.buildSocket(SocketType.REQ).bind("tcp://*:5551");
            for (int task = 1; task <= 10000; task++) {
                req.send(new Message(task));
                String s = req.receiveMessage().popString();
                assert (s.equals("DONE"));
            }
            context.close();
        }
    }

    class Worker implements Runnable {
        @Override
        public void run() {
            Context context = ContextFactory.createContext(1);
            Socket rep = context.buildSocket(SocketType.REP).connect("tcp://localhost:5551");
            while (running.get()) {
                int task = rep.receiveMessage().popInt();
                System.out.println("Task #" + task);
                doWork(task);
                rep.send(new Message("DONE"));
            }

            context.close();
            running.set(false);
        }

        private void doWork(int task) {
            try {
                Thread.sleep(new Random().nextInt(5) * 1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
