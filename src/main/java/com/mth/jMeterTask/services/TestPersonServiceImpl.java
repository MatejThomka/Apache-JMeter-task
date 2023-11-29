package com.mth.jMeterTask.services;

import com.mth.jMeterTask.exceptions.BirthNumberException;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;
import com.mth.jMeterTask.repositories.TestPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class TestPersonServiceImpl implements TestPersonService {

  private final TestPersonRepository testPersonRepository;

  @Autowired
  public TestPersonServiceImpl(TestPersonRepository testPersonRepository) {
    this.testPersonRepository = testPersonRepository;
  }

  @Override
  public TestPerson detail(Long id) throws JMeterException {

    TestPerson testPerson = testPersonRepository.findById(id);

    if (!validateBirthNumber(testPerson.getBirthNumber())) {
      throw new BirthNumberException();
    }

    return testPerson;
  }

  @Override
  public TestPerson search(Long id, String name, String lastname, String date) throws JMeterException {
    String nameRegex = (name != null) ? name.replaceAll("\\*", "") : null;
    String lastnameRegex = (lastname != null) ? lastname.replaceAll("\\*", "") : null;

    TestPerson testPerson = new TestPerson();

    if (!validateBirthNumber(testPerson.getBirthNumber())) {
      throw new BirthNumberException();
    }
    return new TestPerson();
  }

  @Override
  public TestPerson update(Long id, TestPerson testPerson) throws JMeterException {
    TestPerson updatingPerson = testPersonRepository.findById(id);

    if (testPerson.getName() != null && testPerson.getLastname() == null) {
      updatingPerson.setName(testPerson.getName());
    } else if (testPerson.getName() == null && testPerson.getLastname() != null) {
      updatingPerson.setLastname(testPerson.getLastname());
    } else {
      updatingPerson.setName(testPerson.getName());
      updatingPerson.setLastname(testPerson.getLastname());
    }

    testPersonRepository.save(updatingPerson);

    return updatingPerson;
  }

  @Override
  public void create(String name, String lastname, String birthNumber) throws JMeterException{
    TestPerson testPerson = new TestPerson(name, lastname, birthNumber);
    if (testPersonRepository.exists(Example.of(testPerson))) {
      throw new JMeterException("This user already exist!");
    }
    testPersonRepository.save(testPerson);
  }

  private boolean validateBirthNumber(String birthNumber) {
    return true;
  }
}
