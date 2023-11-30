package com.mth.jMeterTask.services;

import com.mth.jMeterTask.exceptions.BirthNumberException;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;
import com.mth.jMeterTask.models.enums.Gender;
import com.mth.jMeterTask.repositories.TestPersonRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    String birthNumber = testPerson.getBirthNumber();

    if (invalidBirthNumber(birthNumber, testPerson.getGender())) {
      throw new BirthNumberException("Birth number is invalid!");
    }

    return testPerson;
  }

  @Override
  public List<TestPerson> search(TestPerson testPerson) throws JMeterException {
    String nameRegex = (testPerson.getName() != null) ? testPerson.getName().replaceAll("\\*", "") : null;
    String lastnameRegex = (testPerson.getLastname() != null) ? testPerson.getLastname().replaceAll("\\*", "") : null;

    Set<TestPerson> testPersonList = new HashSet<>();

    if (nameRegex != null && lastnameRegex == null && testPerson.getBirthNumber() == null){
      testPersonList.addAll(testPersonRepository.findAllByNameStartingWith(nameRegex));
    } else if (nameRegex == null && lastnameRegex != null && testPerson.getBirthNumber() == null) {
      testPersonList.addAll(testPersonRepository.findAllByLastnameStartingWith(lastnameRegex));
    } else if (nameRegex != null && lastnameRegex != null && testPerson.getBirthNumber() == null) {
      testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndLastnameStartingWith(nameRegex, lastnameRegex));
    } else if (nameRegex == null && lastnameRegex == null && testPerson.getBirthNumber() != null) {
      testPersonList.addAll(testPersonRepository.findAllByBirthNumberStartingWith(dateCorrection(testPerson.getBirthNumber(), false)));
      testPersonList.addAll(testPersonRepository.findAllByBirthNumberStartingWith(dateCorrection(testPerson.getBirthNumber(), true)));
    } else if (nameRegex != null && lastnameRegex == null && testPerson.getBirthNumber() != null) {
      testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndBirthNumberStartingWith(nameRegex, dateCorrection(testPerson.getBirthNumber(), false)));
      testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndBirthNumberStartingWith(nameRegex, dateCorrection(testPerson.getBirthNumber(), true)));
    } else if (nameRegex == null && lastnameRegex != null && testPerson.getBirthNumber() != null) {
      testPersonList.addAll(testPersonRepository.findAllByLastnameStartingWithAndBirthNumberStartingWith(lastnameRegex, dateCorrection(testPerson.getBirthNumber(), false)));
      testPersonList.addAll(testPersonRepository.findAllByLastnameStartingWithAndBirthNumberStartingWith(lastnameRegex, dateCorrection(testPerson.getBirthNumber(), true)));
    }


    return testPersonList.stream().toList();
  }

  @Override
  public TestPerson update(Long id, TestPerson testPerson) throws JMeterException {
    TestPerson updatingPerson = testPersonRepository.findById(id);

    String birthNumber = testPerson.getBirthNumber();

    if (invalidBirthNumber(birthNumber, updatingPerson.getGender())) {
      throw new BirthNumberException("Birth number is invalid!");
    }

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
  public void create(String name, String lastname, String birthNumber, Gender gender) throws JMeterException{
    TestPerson testPerson = new TestPerson(name, lastname, birthNumber, gender);
    if (testPersonRepository.exists(Example.of(testPerson))) {
      throw new JMeterException("This user already exist!");
    }
    testPersonRepository.save(testPerson);
  }

  private boolean invalidBirthNumber(String birthNumber, Gender gender) throws JMeterException {

    if (birthNumber.matches("\\d{6}/\\d{3,4}")) {
      int day = Integer.parseInt(birthNumber.substring(4, 6));
      int month = Integer.parseInt(birthNumber.substring(2, 4));
      int year = Integer.parseInt(birthNumber.substring(0, 2));

      if (gender == Gender.FEMALE) {
        month = Integer.parseInt(birthNumber.substring(2, 4));
        month -= 50;
      }

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
      dateFormat.setLenient(false);

      try {
        Date date = dateFormat.parse(String.format("%02d%02d%02d", year, month, day));
      } catch (ParseException e) {
        throw new JMeterException(e.getMessage());
      }
    } else {
      return true;
    }
    return false;
  }

  private String dateCorrection(String birthNumber, boolean isFemale) throws JMeterException {
    String searchedDate = (birthNumber != null) ? birthNumber.replaceAll("-", "") : null;
    String cleanDate = null;
    int date;
    Gender gender = Gender.MALE;

    if (isFemale) {
      gender = Gender.FEMALE;
    }

    assert searchedDate != null;
    if (searchedDate.length() == 4) {
      cleanDate = searchedDate.substring(2, 4);
    } else if (searchedDate.length() == 6) {
      cleanDate = searchedDate.substring(2, 6);
    } else if (searchedDate.length() == 8) {
      cleanDate = searchedDate.substring(2, 8);
    }


    assert cleanDate != null;
    date = Integer.parseInt(cleanDate);


    if (cleanDate.length() == 4 && isFemale) {
      date += 50;
    } else if (cleanDate.length() == 6 && isFemale) {
      date += 5000;
    }

    cleanDate = String.valueOf(date);

    if (invalidBirthNumber(cleanDate, gender)){
      throw new BirthNumberException("Birth number is invalid!");
    }

    return cleanDate;
  }
}

