package org.zeromq.demo.example7;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class StatsProcessor {
    @Inject
    private StatsCounter stats;
    
    public void doWork() {
        stats.addCount(1);
    }
}
