package com.mth.jMeterTask;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class JMeterTaskApplication {

  public static void main(String[] args) {
    SpringApplication.run(JMeterTaskApplication.class, args);
  }

}
