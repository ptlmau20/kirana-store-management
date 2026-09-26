package com.kirana.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class KiranaStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiranaStoreApplication.class, args);
    }
}
