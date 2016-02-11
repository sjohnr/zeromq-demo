package org.zeromq.demo.example7;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class Example7PostConstruct implements Runnable {
    @Inject
    private StatsProcessor statsProcessor;
    
    @PostConstruct
    public void initialize() {
        for (int i = 1; i <= 100; i++) {
            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        for (int i = 1; i <= 1000000; i++) {
            statsProcessor.doWork();
        }
    }
}
