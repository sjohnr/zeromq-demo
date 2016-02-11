package org.zeromq.demo.example8;

import org.springframework.stereotype.Component;
import org.zeromq.api.Context;
import org.zeromq.api.Message;
import org.zeromq.api.Socket;
import org.zeromq.api.SocketType;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class StatsCounter implements Runnable {
    @Inject
    private Context context;
    private Socket pull;
    private Socket pub;
    
    private AtomicBoolean running = new AtomicBoolean(true);
    
    public void initialize() {
        pull = context.buildSocket(SocketType.PULL).bind("inproc://stats-queue");
        pub = context.buildSocket(SocketType.PUB).bind("inproc://stats-topic");
        
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        int count = 0;
        while (running.get()) {
            count += pull.receiveMessage().popInt();
            if (count % 1000000 == 0) {
                System.out.println("Count: " + count);
                pub.send(new Message(count));
            }
        }
    }
}
