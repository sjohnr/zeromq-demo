package org.zeromq.demo.example8;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class Example8PostConstruct implements Runnable {
    @Inject
    private StatsProcessor processor;
    
    @Inject
    private StatsCounter counter;
    
    @PostConstruct
    public void initialize() {
        counter.initialize();
        
        for (int i = 1; i <= 100; i++) {
            new Thread(this).start();
        }
    }
    
    @Override
    public void run() {
        for (int i = 1; i <= 1000000; i++) {
            processor.doWork();
        }
    }
}
