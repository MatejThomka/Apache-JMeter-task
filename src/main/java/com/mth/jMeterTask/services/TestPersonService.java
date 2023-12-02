package com.mth.jMeterTask.services;

import com.mth.jMeterTask.entities.records.TestPersonRecord;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.entities.TestPerson;
import java.util.List;
import lombok.NonNull;

public interface TestPersonService {

  TestPersonRecord detail(@NonNull Integer id) throws JMeterException;

  List<TestPersonRecord> search(TestPerson testPerson) throws JMeterException;

  TestPersonRecord update(Integer id,
                    TestPerson testPerson) throws JMeterException;

}
