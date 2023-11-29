package com.mth.jMeterTask.services;

import com.mth.jMeterTask.exceptions.BirthNumberException;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;
import com.mth.jMeterTask.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService{

  private final PersonRepository personRepository;

  @Autowired
  public PersonServiceImpl(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @Override
  public TestPerson detail(Long id) throws JMeterException {
    TestPerson testPerson = new TestPerson();
    testPerson.setId(id);
    testPerson.setName(personRepository.findById(id).getName());
    testPerson.setLastname(personRepository.findById(id).getLastname());
    testPerson.setBirthNumber(personRepository.findById(id).getBirthNumber());

    if (!validateBirthNumber(testPerson.getBirthNumber())) {
      throw new BirthNumberException();
    }

    return testPerson;
  }

  @Override
  public TestPerson search(Long id, String name, String lastname, Integer year, Integer month,
                           Integer day) throws JMeterException {
    return null;
  }

  @Override
  public TestPerson update(Long id, String name, String lastname) throws JMeterException {
    return null;
  }

  private boolean validateBirthNumber(String birthNumber) {
    return true;
  }
}
