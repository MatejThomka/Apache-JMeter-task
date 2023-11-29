package com.mth.jMeterTask.services;

import com.mth.jMeterTask.exceptions.BirthNumberException;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;
import com.mth.jMeterTask.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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

    TestPerson testPerson = personRepository.findById(id);

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

  @Override
  public void create(String name, String lastname, String birthNumber) throws JMeterException{
    TestPerson testPerson = new TestPerson(name, lastname, birthNumber);
    if (personRepository.exists(Example.of(testPerson))) {
      throw new JMeterException("This user already exist!");
    }
    personRepository.save(testPerson);
  }

  private boolean validateBirthNumber(String birthNumber) {
    return true;
  }
}
