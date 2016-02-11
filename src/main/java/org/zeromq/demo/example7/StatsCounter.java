package org.zeromq.demo.example7;

import org.springframework.stereotype.Component;

@Component
public class StatsCounter {
    private int count = 0;
    
    public synchronized void addCount(int count) {
        this.count += count;
        if (this.count % 1000000 == 0) {
            System.out.println("Count: " + this.count);
        }
    }
}
