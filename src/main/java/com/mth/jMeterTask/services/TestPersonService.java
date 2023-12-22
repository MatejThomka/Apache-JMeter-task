package com.mth.jMeterTask.services;

import com.mth.jMeterTask.entities.records.TestPersonRecord;
import com.mth.jMeterTask.exceptions.BirthNumberException;
import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.entities.TestPerson;
import com.mth.jMeterTask.entities.enums.Gender;
import com.mth.jMeterTask.exceptions.TestPersonAlreadyExistException;
import com.mth.jMeterTask.exceptions.TestPersonNotFoundException;
import com.mth.jMeterTask.repositories.TestPersonRepository;
import com.mth.jMeterTask.repositories.TestPersonSpecifications;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestPersonService {

  private final TestPersonRepository testPersonRepository;

  /**
   * Creates a new TestPersonRecord based on the provided TestPerson.
   *
   * @param testPerson - The TestPerson object containing information for creating the record.
   * @return - A TestPersonRecord representing the newly created record.
   * @throws JMeterException - If there is an issue related to JMeter.
   * @throws TestPersonAlreadyExistException - If a TestPerson with the same details already exists.
   */
  public TestPersonRecord create(TestPerson testPerson) throws JMeterException {

    log.info("Creating new TestPerson");

    Specification<TestPerson> spec = Specification.where(null);

    checkBirthNumber(testPerson.getBirthNumber(), testPerson.getGender());

    if (testPerson.getName() != null) spec = spec.and(TestPersonSpecifications.hasName(testPerson.getName()));
    if (testPerson.getLastname() != null) spec = spec.and(TestPersonSpecifications.hasLastname(testPerson.getLastname()));
    if (testPerson.getBirthNumber() != null) spec = spec.and(TestPersonSpecifications.hasBirthNumber(testPerson.getBirthNumber()));

    if (!testPersonRepository.findAll(spec).isEmpty()) throw new TestPersonAlreadyExistException("This person already exist! " + testPerson.getName() + " " + testPerson.getLastname() + " " + testPerson.getBirthNumber());

    TestPerson newPerson = new TestPerson();
    newPerson.setName(testPerson.getName());
    newPerson.setLastname(testPerson.getLastname());
    newPerson.setBirthNumber(testPerson.getBirthNumber());
    newPerson.setDateOfBirth(extractDateOfBirth(testPerson.getBirthNumber(), testPerson.getGender()));
    newPerson.setGender(testPerson.getGender());

    testPersonRepository.save(newPerson);

    return new TestPersonRecord(newPerson.getId(), newPerson.getName(), newPerson.getLastname(), newPerson.getBirthNumber());
  }

  /**
   * Retrieves details about a person based on the provided ID.
   *
   * @param id - Identifier of the searched person.
   * @exception TestPersonNotFoundException - Thrown if a person with the given ID is not found.
   * @return TestPersonRecord - A record containing the name, surname, and birth number of the person.
   */

  public TestPersonRecord detail(@NonNull Integer id) {

    log.info("Getting detail from ID: " + id);

    TestPerson testPerson = testPersonRepository.findById(id).orElseThrow(() -> new TestPersonNotFoundException(id + " not found!"));

    return new TestPersonRecord(testPerson.getId(), testPerson.getName(), testPerson.getLastname(), testPerson.getBirthNumber());
  }

  /**
   * Searches and returns a list of persons based on the criteria specified in TestPerson.
   *
   * @param testPerson - An object containing search criteria.
   * @return List<TestPersonRecord> - A list of records containing the name, surname, and birth number of the found persons.
   */

  public List<TestPersonRecord> search(TestPerson testPerson) {

    log.info("Search users and list them by searching options.");

    Specification<TestPerson> spec = Specification.where(null);

    if (testPerson.getId() != null) {
      spec = spec.and(TestPersonSpecifications.hasId(testPerson.getId()));
    } else {
      if (testPerson.getName() != null) spec = spec.and(TestPersonSpecifications.searchByName(testPerson.getName()));
      if (testPerson.getLastname() != null) spec = spec.and(TestPersonSpecifications.searchByLastname(testPerson.getLastname()));
      if (testPerson.getDateOfBirth() != null) spec = spec.and(TestPersonSpecifications.searchByDateOfBirth(testPerson.getDateOfBirth()));
    }

    Set<TestPerson> persons = testPersonRepository.findAll(spec);

    if (persons.isEmpty()) {
      throw new TestPersonNotFoundException("There is nothing!");
    }

    List<TestPersonRecord> outputList = new ArrayList<>();

    for (TestPerson person : persons) {
      outputList.add(new TestPersonRecord(person.getId(), person.getName(), person.getLastname(), person.getBirthNumber()));
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

  public TestPersonRecord update(@NonNull Integer id,
                           TestPerson testPerson) {

    log.info("Updating ID " + id + " with new details.");

    TestPerson updatingPerson = testPersonRepository.findById(id).orElseThrow(() -> new TestPersonNotFoundException(id + " not found!"));

    if (testPerson.getName() != null) {
      updatingPerson.setName(testPerson.getName());
    }

    if (testPerson.getLastname() != null) {
      updatingPerson.setLastname(testPerson.getLastname());
    }

    testPersonRepository.save(updatingPerson);

    return new TestPersonRecord(updatingPerson.getId(), updatingPerson.getName(), updatingPerson.getLastname(), updatingPerson.getBirthNumber());
  }

  /**
   * Deletes a TestPerson with the specified ID if the provided birth number matches.
   *
   * @param id - The ID of the TestPerson to be deleted.
   * @param testPerson - The TestPerson object containing the birth number for validation.
   * @return - The deleted TestPerson.
   * @throws BirthNumberException - If there is an issue related to the birth number.
   * @throws TestPersonNotFoundException - If the TestPerson with the specified ID is not found.
   */
  public TestPerson delete(Integer id,
                           TestPerson testPerson) throws BirthNumberException {

    log.info("Deleting id " + id + "!");

    TestPerson removingPerson = testPersonRepository.findById(id).orElseThrow(() -> new TestPersonNotFoundException(id + " not found!"));

    if (Objects.equals(testPerson.getBirthNumber(), removingPerson.getBirthNumber())) {
      testPersonRepository.delete(removingPerson);
    } else {
      throw new BirthNumberException("Birth number is not correct!");
    }

    return removingPerson;
  }

  /**
   * Validates the birth number based on gender and data structure.
   *
   * @param birthNumber - Birth number of the person.
   * @param gender - Gender of the person.
   * @throws JMeterException - Thrown for general errors in JMeter or invalid birth numbers.
   */
  private void checkBirthNumber(String birthNumber, Gender gender) throws JMeterException {

    log.info("Validate birth number " + birthNumber);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    dateFormat.setLenient(false);

    Long clearNumber = Long.parseLong(birthNumber.replaceAll("/", ""));

    int day = Integer.parseInt(birthNumber.substring(4, 6));
    int month = Integer.parseInt(birthNumber.substring(2, 4));
    int year = Integer.parseInt(birthNumber.substring(0, 2));

    if (gender == Gender.FEMALE) {
      month -= 50;
    }

    if (birthNumber.length() == 10) {
      parseOldBirthNumber(day, month, year, dateFormat);
    } else {
      parseNewBirthNumber(day, month, year, dateFormat, clearNumber);
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
   * Extracts the date of birth from a given birth number, considering the gender.
   *
   * @param birthNumber - The birth number from which to extract the date of birth.
   * @param gender - The gender associated with the birth number.
   * @return - A formatted string representing the date of birth (yyyy-MM-dd).
   */
  private String extractDateOfBirth(String birthNumber, Gender gender) {

    int month = Integer.parseInt(birthNumber.substring(2, 4));

    StringBuilder stringBuilder = new StringBuilder();
    int currentYear = Calendar.YEAR % 100;

    if (gender == Gender.FEMALE) {
      month -= 50;
    }



    if (Integer.parseInt(birthNumber.substring(0, 2)) > currentYear) {
      stringBuilder.append("19").append(birthNumber, 0, 2).append("-")
          .append(month).append("-").append(birthNumber, 4, 6);
    } else {
      stringBuilder.append("20").append(birthNumber, 0, 2).append("-")
          .append(month).append("-").append(birthNumber, 4, 6);
    }

    return stringBuilder.toString();
  }
}

