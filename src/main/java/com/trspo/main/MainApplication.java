package com.trspo.main;

import com.trspo.main.clients.RabbitMQClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        RabbitMQClient client = new RabbitMQClient();
        client.clientTests();
    }
}
