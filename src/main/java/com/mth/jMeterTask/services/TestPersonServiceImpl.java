package com.mth.jMeterTask.services;

import com.mth.jMeterTask.exceptions.BirthNumberException;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;
import com.mth.jMeterTask.models.enums.Gender;
import com.mth.jMeterTask.repositories.TestPersonRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    if (invalidBirthNumber(testPerson)) {
      throw new BirthNumberException("Birth number is invalid!");
    }

    return testPerson;
  }

  @Override
  public List<TestPerson> search(TestPerson testPerson) throws JMeterException {
    String nameRegex = (testPerson.getName() != null) ? testPerson.getName().replaceAll("\\*", "") : null;
    String lastnameRegex = (testPerson.getLastname() != null) ? testPerson.getLastname().replaceAll("\\*", "") : null;
//    String birthNumber = (testPerson.getBirthNumber() != null) ? dateCorrection(testPerson.getBirthNumber()) : null;

//    if (birthNumber != null) {
//      if (invalidBirthNumber(birthNumber)){
//        throw new BirthNumberException("Birth number is invalid!");
//      }
//    }


    List<TestPerson> testPersonList = new ArrayList<>();

    if (nameRegex != null && lastnameRegex == null && testPerson.getBirthNumber() == null){
      testPersonList.addAll(testPersonRepository.findAllByNameStartingWith(nameRegex));
    } else if (nameRegex == null && lastnameRegex != null && testPerson.getBirthNumber() == null) {
      testPersonList.addAll(testPersonRepository.findAllByLastnameStartingWith(lastnameRegex));
    } else if (nameRegex != null && lastnameRegex != null && testPerson.getBirthNumber() == null) {
      testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndLastnameStartingWith(nameRegex, lastnameRegex));
    } else if (nameRegex == null && lastnameRegex == null && testPerson.getBirthNumber() != null) {
//      testPersonList.addAll(testPersonRepository.findAllByBirthNumberContaining(birthNumber));
    }


    return testPersonList;
  }

  @Override
  public TestPerson update(Long id, TestPerson testPerson) throws JMeterException {
    TestPerson updatingPerson = testPersonRepository.findById(id);

    if (invalidBirthNumber(updatingPerson)) {
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
  public void create(String name, String lastname, String birthNumber) throws JMeterException{
    TestPerson testPerson = new TestPerson(name, lastname, birthNumber);
    if (testPersonRepository.exists(Example.of(testPerson))) {
      throw new JMeterException("This user already exist!");
    }
    testPersonRepository.save(testPerson);
  }

  private boolean invalidBirthNumber(TestPerson testPerson) throws JMeterException {

    if (testPerson.getBirthNumber().matches("\\d{6}/\\d{3,4}")) {
      int day = Integer.parseInt(testPerson.getBirthNumber().substring(4, 6));
      int month = Integer.parseInt(testPerson.getBirthNumber().substring(2, 4));
      int year = Integer.parseInt(testPerson.getBirthNumber().substring(0, 2));

      if (testPerson.getGender() == Gender.FEMALE) {
        month = Integer.parseInt(testPerson.getBirthNumber().substring(2, 4));
        month -= 50;
      }

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
      dateFormat.setLenient(false);

      try {
        Date date = dateFormat.parse(String.format("%02d%02d%02d", year, month, day));
      } catch (ParseException e) {
        throw new JMeterException(e.getMessage());
      }
    }
    return false;
  }

//  private List<String> dateCorrection(String birthNumber) {
//    List<String> dates = List.of("date1", "date2");
//    String cleanDate = (birthNumber != null) ? birthNumber.replaceAll("-", "") : null;
//
//    StringBuilder stringBuilder = new StringBuilder();
//
//
//
//    if (cleanDate != null){
//      for (int i = 0; i < dates.size() - 1; i++) {
//        for (int j = 0; j < cleanDate.length() - 1; j++) {
//          if (i == 1 && j == 6) {
//            stringBuilder.append('5');
//          } else {
//            stringBuilder.append(cleanDate.charAt(j));
//          }
//          dates.add(stringBuilder.toString());
//          dates.remove(2);
//        }
//      }
//
//      if (cleanDate.length() == 4){
//        dateV1 = cleanDate.substring(2, 4);
//        dateV2 = cl
//      } else if (cleanDate.length() == 6) {
//        dateV1 = cleanDate.substring(2, 6);
//      } else if (cleanDate.length() == 8) {
//        dateV1 = cleanDate.substring(2, 8);
//      }
//    }
//
//
//    return date;
//  }
}

