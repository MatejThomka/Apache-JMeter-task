package com.mth.jMeterTask.exceptions;

import org.springframework.http.HttpStatus;

public class TestPersonAlreadyExistException extends JMeterException{
  public TestPersonAlreadyExistException(String message, HttpStatus status) {
    super(message, status);
  }
}
