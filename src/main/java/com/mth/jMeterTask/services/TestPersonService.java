package com.mth.jMeterTask.services;

import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;

public interface TestPersonService {

  TestPerson detail(Long id) throws JMeterException;
  TestPerson search(Long id, String name, String lastname, String date) throws JMeterException;
  TestPerson update(Long id, String name, String lastname) throws JMeterException;
  void create(String name, String lastname, String birthNumber) throws JMeterException;
}
