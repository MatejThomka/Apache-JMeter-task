package com.mth.jMeterTask.services;

import com.mth.jMeterTask.entities.enums.SearchType;
import com.mth.jMeterTask.exceptions.BirthNumberException;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.entities.TestPerson;
import com.mth.jMeterTask.entities.enums.Gender;
import com.mth.jMeterTask.repositories.TestPersonRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestPersonServiceImpl implements TestPersonService {

  private final TestPersonRepository testPersonRepository;

  @Override
  public TestPerson detail(Long id) throws JMeterException {

    TestPerson testPerson = testPersonRepository.findById(id);

    String birthNumber = testPerson.getBirthNumber();

    invalidBirthNumber(birthNumber, testPerson.getGender());

    return testPerson;
  }

  @Override
  public List<TestPerson> search(TestPerson testPerson) throws JMeterException {

    String nameRegex = (testPerson.getName() != null) ? testPerson.getName().replaceAll("\\*", "") : null;
    String lastnameRegex = (testPerson.getLastname() != null) ? testPerson.getLastname().replaceAll("\\*", "") : null;

    Set<TestPerson> testPersonList = new HashSet<>();

    switch (getSearchType(testPerson)) {
      case BY_ID -> testPersonList.add(testPersonRepository.findById(testPerson.getId()));
      case BY_NAME -> testPersonList.addAll(testPersonRepository.findAllByNameStartingWith(nameRegex));
      case BY_LASTNAME -> testPersonList.addAll(testPersonRepository.findAllByLastnameStartingWith(lastnameRegex));
      case BY_NAME_AND_LASTNAME -> testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndLastnameStartingWith(nameRegex, lastnameRegex));
      case BY_DATE -> {
        testPersonList.addAll(testPersonRepository.findAllByBirthNumberStartingWith(dateCorrection(testPerson.getBirthNumber(), false)));
        testPersonList.addAll(testPersonRepository.findAllByBirthNumberStartingWith(dateCorrection(testPerson.getBirthNumber(), true)));
      }
      case BY_NAME_AND_DATE -> {
        testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndBirthNumberStartingWith(nameRegex, dateCorrection(testPerson.getBirthNumber(), false)));
        testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndBirthNumberStartingWith(nameRegex, dateCorrection(testPerson.getBirthNumber(), true)));
      }
      case BY_LASTNAME_AND_DATE -> {
        testPersonList.addAll(testPersonRepository.findAllByLastnameStartingWithAndBirthNumberStartingWith(lastnameRegex, dateCorrection(testPerson.getBirthNumber(), false)));
        testPersonList.addAll(testPersonRepository.findAllByLastnameStartingWithAndBirthNumberStartingWith(lastnameRegex, dateCorrection(testPerson.getBirthNumber(), true)));
      }
      case BY_NAME_AND_LASTNAME_AND_DATE -> {
        testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndLastnameStartingWithAndBirthNumberStartingWith(nameRegex, lastnameRegex, dateCorrection(testPerson.getBirthNumber(), false)));
        testPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndLastnameStartingWithAndBirthNumberStartingWith(nameRegex, lastnameRegex, dateCorrection(testPerson.getBirthNumber(), true)));
      }
      case null -> testPersonList.add(null);
    }

    List<TestPerson> personList = new ArrayList<>(testPersonList);

    for (TestPerson person : personList) {
      invalidBirthNumber(person.getBirthNumber(), testPerson.getGender());
    }

    return personList;
  }

  @Override
  public TestPerson update(Long id,
                           TestPerson testPerson) throws JMeterException {
    TestPerson updatingPerson = testPersonRepository.findById(id);
    String birthNumber = updatingPerson.getBirthNumber();

    invalidBirthNumber(birthNumber, testPerson.getGender());

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
  public void create(String name,
                     String lastname,
                     String birthNumber,
                     Gender gender)
      throws JMeterException {
    TestPerson testPerson = new TestPerson(name, lastname, birthNumber, gender);
    if (testPersonRepository.exists(Example.of(testPerson))) {
      throw new JMeterException("This user already exist!");
    }
    testPersonRepository.save(testPerson);
  }

  private void invalidBirthNumber(String birthNumber,
                                  Gender gender) throws JMeterException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    dateFormat.setLenient(false);

    Long birthNumberWithoutSlash = Long.parseLong(birthNumber.replaceAll("/", ""));

    int day = Integer.parseInt(birthNumber.substring(4, 6));
    int month = Integer.parseInt(birthNumber.substring(2, 4));
    int year = Integer.parseInt(birthNumber.substring(0, 2));

    if (gender == Gender.FEMALE) {
      month -= 50;
    }


    if (birthNumber.length() == 10) {
      parseOldBirthNumber(day, month, year, dateFormat);
    } else {
      parseNewBirthNumber(day, month, year, dateFormat, birthNumberWithoutSlash);
    }
  }

  private void parseOldBirthNumber(int day,
                                   int month,
                                   int year,
                                   SimpleDateFormat format) throws JMeterException {

    if (year > 53) throw new BirthNumberException("Incorrect year of birth!");


    try {
      format.parse(String.format("%02d%02d%02d", year, month, day));
    } catch (ParseException e) {
      throw new BirthNumberException();
    }
  }

  private void parseNewBirthNumber(int day,
                                   int month,
                                   int year,
                                   SimpleDateFormat format,
                                   Long birthNumber) throws JMeterException {

    if (birthNumber % 11 != 0) throw new BirthNumberException("Birth number is incorrect! Not dividable with 11!");

    try {
      format.parse(String.format("%02d%02d%02d", year, month, day));
    } catch (ParseException e) {
      throw new BirthNumberException(e.getMessage());
    }
  }


  private String dateCorrection(String birthNumber,
                                boolean isFemale) {

    String searchedDate = (birthNumber != null) ? birthNumber.replaceAll("-", "") : null;
    String cleanDate = null;
    int date;

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

    return cleanDate;
  }

  private SearchType getSearchType(TestPerson testPerson) {

    if (testPerson.getId() != null) {
      return SearchType.BY_ID;
    } else if (testPerson.getName() != null && testPerson.getLastname() == null &&
        testPerson.getBirthNumber() == null) {
      return SearchType.BY_NAME;
    } else if (testPerson.getName() == null && testPerson.getLastname() != null &&
        testPerson.getBirthNumber() == null) {
      return SearchType.BY_LASTNAME;
    } else if (testPerson.getName() == null && testPerson.getLastname() == null &&
        testPerson.getBirthNumber() != null) {
      return SearchType.BY_DATE;
    } else if (testPerson.getName() != null && testPerson.getLastname() != null &&
        testPerson.getBirthNumber() == null) {
      return SearchType.BY_NAME_AND_LASTNAME;
    } else if (testPerson.getName() != null && testPerson.getLastname() == null) {
      return SearchType.BY_NAME_AND_DATE;
    } else if (testPerson.getName() == null && testPerson.getLastname() != null) {
      return SearchType.BY_LASTNAME_AND_DATE;
    } else if (testPerson.getName() != null) {
      return SearchType.BY_NAME_AND_LASTNAME_AND_DATE;
    }

    return null;
  }
}

