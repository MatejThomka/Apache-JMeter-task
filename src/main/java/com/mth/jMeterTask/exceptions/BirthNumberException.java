package com.mth.jMeterTask.exceptions;

import org.springframework.http.HttpStatus;

public class BirthNumberException extends JMeterException {

  public BirthNumberException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
