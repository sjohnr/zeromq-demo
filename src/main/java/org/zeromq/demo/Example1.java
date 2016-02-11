package org.zeromq.demo;

import org.zeromq.ContextFactory;
import org.zeromq.api.Context;
import org.zeromq.api.Message;
import org.zeromq.api.Socket;
import org.zeromq.api.SocketType;

public class Example1 {
    /**
     * @param args
     */
    public static void main(String[] args) {
        new Example1().run();
    }

    public void run() {
        new Thread(new Server()).start();
        new Thread(new Client()).start();
    }

    class Client implements Runnable {
        @Override
        public void run() {
            Context context = ContextFactory.createContext(1);
            Socket req = context.buildSocket(SocketType.REQ).connect("tcp://localhost:5551");
            req.send(new Message("Hello"));
            String s = req.receiveMessage().popString();
            assert (s.equals("World"));
            System.out.println("Client received: " + s);
            context.close();
        }
    }

    class Server implements Runnable {
        @Override
        public void run() {
            Context context = ContextFactory.createContext(1);
            Socket rep = context.buildSocket(SocketType.REP).bind("tcp://*:5551");
            String s = rep.receiveMessage().popString();
            assert (s.equals("Hello"));
            System.out.println("Server received: " + s);
            rep.send(new Message("World"));
            context.close();
        }
    }
}
