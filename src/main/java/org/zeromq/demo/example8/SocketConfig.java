package org.zeromq.demo.example8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.ContextFactory;
import org.zeromq.api.Context;

@Configuration
public class SocketConfig {
    @Bean
    public Context context() {
        return ContextFactory.createContext(1);
    }
}
