package org.zeromq.demo;

import org.zeromq.ContextFactory;
import org.zeromq.api.Context;
import org.zeromq.api.Message;
import org.zeromq.api.Socket;
import org.zeromq.api.SocketType;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Example5 {
    private static final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Example5().run();
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
            Socket rep = context.buildSocket(SocketType.REP).bind("tcp://*:5551");
            for (int task = 1; task <= 10000; task++) {
                String s = rep.receiveMessage().popString();
                assert (s.equals("READY"));
                rep.send(new Message(task));
            }
            context.close();
        }
    }

    class Worker implements Runnable {
        @Override
        public void run() {
            Context context = ContextFactory.createContext(1);
            Socket req = context.buildSocket(SocketType.REQ).connect("tcp://localhost:5551");
            do {
                req.send(new Message("READY"));
                int task = req.receiveMessage().popInt();
                System.out.println("Task #" + task);
                doWork(task);
            } while (running.get());

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
