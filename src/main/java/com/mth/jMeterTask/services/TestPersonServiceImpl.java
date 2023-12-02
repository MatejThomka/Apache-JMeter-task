package com.mth.jMeterTask.services;

import com.mth.jMeterTask.entities.records.TestPersonRecord;
import com.mth.jMeterTask.entities.enums.SearchType;
import com.mth.jMeterTask.exceptions.BirthNumberException;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.entities.TestPerson;
import com.mth.jMeterTask.entities.enums.Gender;
import com.mth.jMeterTask.exceptions.TestPersonNotFoundException;
import com.mth.jMeterTask.repositories.TestPersonRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestPersonServiceImpl implements TestPersonService {

  private final TestPersonRepository testPersonRepository;

  /**
   * Retrieves details about a person based on the provided ID.
   *
   * @param id - Identifier of the searched person.
   * @exception TestPersonNotFoundException - Thrown if a person with the given ID is not found.
   * @return TestPersonRecord - A record containing the name, surname, and birth number of the person.
   */
  @Override
  public TestPersonRecord detail(@NonNull Integer id) throws JMeterException {

    log.info("Getting detail from ID: " + id);

    TestPerson testPerson = testPersonRepository.findById(id).orElseThrow(() -> new TestPersonNotFoundException(id + " not found!"));

    invalidBirthNumber(testPerson.getBirthNumber(), testPerson.getGender());

    return new TestPersonRecord(testPerson.getName(), testPerson.getLastname(), testPerson.getBirthNumber());
  }

  /**
   * Searches and returns a list of persons based on the criteria specified in TestPerson.
   *
   * @param testPerson - An object containing search criteria.
   * @return List<TestPersonRecord> - A list of records containing the name, surname, and birth number of the found persons.
   * @throws JMeterException - Thrown for general errors in JMeter.
   */
  @Override
  public List<TestPersonRecord> search(TestPerson testPerson) throws JMeterException {

    log.info("Search users and list them by searching options. " + testPerson);

    String nameRegex = (testPerson.getName() != null) ? testPerson.getName().replaceAll("\\*", "") : null;
    String lastnameRegex = (testPerson.getLastname() != null) ? testPerson.getLastname().replaceAll("\\*", "") : null;

    Set<TestPerson> clearPersonList = new HashSet<>();

    switch (getSearchType(testPerson)) {
      case BY_ID -> clearPersonList.add(testPersonRepository.findById(testPerson.getId()).orElseThrow(() -> new TestPersonNotFoundException(testPerson.getId() + "not found!")));
      case BY_NAME -> clearPersonList.addAll(testPersonRepository.findAllByNameStartingWith(nameRegex));
      case BY_LASTNAME -> clearPersonList.addAll(testPersonRepository.findAllByLastnameStartingWith(lastnameRegex));
      case BY_NAME_AND_LASTNAME -> clearPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndLastnameStartingWith(nameRegex, lastnameRegex));
      case BY_DATE -> {
        clearPersonList.addAll(testPersonRepository.findAllByBirthNumberStartingWith(dateCorrection(testPerson.getBirthNumber(), false)));
        clearPersonList.addAll(testPersonRepository.findAllByBirthNumberStartingWith(dateCorrection(testPerson.getBirthNumber(), true)));
      }
      case BY_NAME_AND_DATE -> {
        clearPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndBirthNumberStartingWith(nameRegex, dateCorrection(testPerson.getBirthNumber(), false)));
        clearPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndBirthNumberStartingWith(nameRegex, dateCorrection(testPerson.getBirthNumber(), true)));
      }
      case BY_LASTNAME_AND_DATE -> {
        clearPersonList.addAll(testPersonRepository.findAllByLastnameStartingWithAndBirthNumberStartingWith(lastnameRegex, dateCorrection(testPerson.getBirthNumber(), false)));
        clearPersonList.addAll(testPersonRepository.findAllByLastnameStartingWithAndBirthNumberStartingWith(lastnameRegex, dateCorrection(testPerson.getBirthNumber(), true)));
      }
      case BY_NAME_AND_LASTNAME_AND_DATE -> {
        clearPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndLastnameStartingWithAndBirthNumberStartingWith(nameRegex, lastnameRegex, dateCorrection(testPerson.getBirthNumber(), false)));
        clearPersonList.addAll(testPersonRepository.findAllByNameStartingWithAndLastnameStartingWithAndBirthNumberStartingWith(nameRegex, lastnameRegex, dateCorrection(testPerson.getBirthNumber(), true)));
      }
      case null -> clearPersonList.add(null);
    }

    List<TestPerson> personList = new ArrayList<>(clearPersonList);
    List<TestPersonRecord> outputList = new ArrayList<>();


    for (TestPerson person : personList) {
      invalidBirthNumber(person.getBirthNumber(), person.getGender());
      outputList.add(new TestPersonRecord(person.getName(), person.getLastname(), person.getBirthNumber()));
    }

    return outputList;
  }

  /**
   * Updates information about a person based on the provided ID and new details.
   *
   * @param id - Identifier of the person being updated.
   * @param testPerson - New information about the person.
   * @exception TestPersonNotFoundException - Thrown if a person with the given ID is not found.
   * @return TestPersonRecord - A record containing the updated name, surname, and birth number of the person.
   */
  @Override
  public TestPersonRecord update(@NonNull Integer id,
                           TestPerson testPerson) throws JMeterException {

    log.info("Updating ID " + id + " with new details.");

    TestPerson updatingPerson = testPersonRepository.findById(id).orElseThrow(() -> new TestPersonNotFoundException(id + " not found!"));

    invalidBirthNumber(updatingPerson.getBirthNumber(), testPerson.getGender());

    if (testPerson.getName() != null && testPerson.getLastname() == null) {
      updatingPerson.setName(testPerson.getName());
    } else if (testPerson.getName() == null && testPerson.getLastname() != null) {
      updatingPerson.setLastname(testPerson.getLastname());
    } else {
      updatingPerson.setName(testPerson.getName());
      updatingPerson.setLastname(testPerson.getLastname());
    }

    testPersonRepository.save(updatingPerson);

    return new TestPersonRecord(updatingPerson.getName(), updatingPerson.getLastname(), updatingPerson.getBirthNumber());
  }

  /**
   * Validates the birth number based on gender and data structure.
   *
   * @param birthNumber - Birth number of the person.
   * @param gender - Gender of the person.
   * @throws JMeterException - Thrown for general errors in JMeter or invalid birth numbers.
   */
  private void invalidBirthNumber(String birthNumber,
                                  Gender gender) throws JMeterException {

    log.info("Validate birth number " + birthNumber);

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

    log.info("Validate old birth number before 1954.");

    if (year > 53) throw new BirthNumberException("Incorrect year of birth!");


    try {
      format.parse(String.format("%02d%02d%02d", year, month, day));
    } catch (ParseException e) {
      throw new BirthNumberException("Incorrect birth number!");
    }
  }

  private void parseNewBirthNumber(int day,
                                   int month,
                                   int year,
                                   SimpleDateFormat format,
                                   Long birthNumber) throws JMeterException {
    log.info("Validate new version of birth number after 1954");

    if (birthNumber % 11 != 0) throw new BirthNumberException("Birth number is incorrect! Not dividable with 11!");

    try {
      format.parse(String.format("%02d%02d%02d", year, month, day));
    } catch (ParseException e) {
      throw new BirthNumberException(e.getMessage());
    }
  }

  /**
   * Modifies the date for better searching in the search method.
   *
   * @param birthNumber - Birth number of the person.
   * @param isFemale - Flag indicating whether the person is female.
   * @return String - Modified birth number for searching.
   */
  private String dateCorrection(String birthNumber,
                                boolean isFemale) {

    log.info("Clear date from '-' for better searching in search method. " + birthNumber);

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

  /**
   * Determines the type of search query to be used based on the provided criteria.
   *
   * @param testPerson - A TestPerson object with search criteria.
   * @return SearchType - The search type according to the SearchType enumeration.
   */
  private SearchType getSearchType(TestPerson testPerson) {

    log.info("Find which type of searching query will be used and return it.");

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

