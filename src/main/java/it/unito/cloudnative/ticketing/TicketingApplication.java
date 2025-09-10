package it.unito.cloudnative.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "it.unito.cloudnative.ticketing")
public class TicketingApplication {
  public static void main(String[] args) {
    SpringApplication.run(TicketingApplication.class, args);
  }
}
