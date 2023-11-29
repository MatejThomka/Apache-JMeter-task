package com.mth.jMeterTask.exceptions;

import org.springframework.http.HttpStatus;

public class JMeterException extends Exception {
  HttpStatus httpStatus;

  public JMeterException(String message) {
    super(message);
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public JMeterException(String message, HttpStatus status) {
    super(message);
    this.httpStatus = status;
  }
}
