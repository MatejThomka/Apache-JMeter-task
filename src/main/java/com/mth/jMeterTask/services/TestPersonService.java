package com.mth.jMeterTask.services;

import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;
import java.util.List;

public interface TestPersonService {

  TestPerson detail(Long id) throws JMeterException;
  List<TestPerson> search(TestPerson testPerson) throws JMeterException;
  TestPerson update(Long id, TestPerson testPerson) throws JMeterException;
  void create(String name, String lastname, String birthNumber) throws JMeterException;
}
