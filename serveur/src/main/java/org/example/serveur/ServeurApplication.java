package org.example.serveur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServeurApplication implements Runnable {

    public static void main(String[] args) {
        SpringApplication.run(ServeurApplication.class, args);
    }

    @Override
    public void run() {

    }
}
