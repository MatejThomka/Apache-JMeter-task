package com.mth.jMeterTask.services;

import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;

public interface PersonService {

  TestPerson detail(Long id) throws JMeterException;
  TestPerson search(Long id, String name, String lastname, Integer yyyy, Integer yyyyMM, Integer yyyyMMdd) throws JMeterException;
  TestPerson update(Long id, String name, String lastname) throws JMeterException;
}
