package org.zeromq.demo.example8;

import org.springframework.stereotype.Component;
import org.zeromq.api.Context;
import org.zeromq.api.Message;
import org.zeromq.api.Socket;
import org.zeromq.api.SocketType;

import javax.inject.Inject;

@Component
public class StatsProcessor {
    private Message ONE = new Message(1);

    @Inject
    private Context context;
    private ThreadLocal<Socket> threadLocal = new ThreadLocal<Socket>() {
        @Override
        protected Socket initialValue() {
            return context.buildSocket(SocketType.PUSH).connect("inproc://stats-queue");
        }
    };
    
    public void doWork() {
        threadLocal.get().send(ONE);
    }
}
