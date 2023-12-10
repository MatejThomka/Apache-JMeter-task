package com.mth.jMeterTask.repositories;

import com.mth.jMeterTask.entities.TestPerson;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TestPersonSpecifications {

  // Searching with ID
  public static Specification<TestPerson> hasId(Long id){
    return ((root, query, builder) -> builder.equal(root.get("id"), id));
  }

  // All search with name or namePrefix or namePart
  public static Specification<TestPerson> hasName(String name) {
    return ((root, query, builder) -> builder.equal(root.get("name"), name));
  }
  public static Specification<TestPerson> hasNamePrefix(String namePrefix) {
    return ((root, query, builder) -> builder.like(root.get("name"), namePrefix + "%"));
  }
  public static Specification<TestPerson> hasNameSuffix(String nameSuffix) {
    return ((root, query, builder) -> builder.like(root.get("name"), "%" + nameSuffix + "%"));
  }
  public static Specification<TestPerson> searchByName(String name) {
    if (name.startsWith("*") && name.endsWith("*")) {
      return hasNameSuffix(name.replace("*", ""))
          .or(hasNamePrefix(name.replace("*", "")))
          .or(hasName(name.replace("*", "")));
    } else if (name.startsWith("*")) {
      return hasNameSuffix(name.replace("*", ""));
    } else if (name.endsWith("*")) {
      return hasNamePrefix(name.replace("*", ""));
    } else {
      return hasName(name);
    }
  }

  // All search with lastname or lastnamePrefix or lastnamePart
  public static Specification<TestPerson> hasLastname(String lastname) {
    return ((root, query, builder) -> builder.equal(root.get("lastname"), lastname));
  }
  public static Specification<TestPerson> hasLastnamePrefix(String lastnamePrefix) {
    return ((root, query, builder) -> builder.like(root.get("lastname"), lastnamePrefix + "%"));
  }
  public static Specification<TestPerson> hasLastnameSuffix(String lastnameSuffix) {
    return ((root, query, builder) -> builder.like(root.get("lastname"), "%" + lastnameSuffix + "%"));
  }
  public static Specification<TestPerson> searchByLastname(String lastname) {
    if (lastname.startsWith("*") && lastname.endsWith("*")) {
      return hasLastnameSuffix(lastname.replace("*", ""))
          .or(hasLastnamePrefix(lastname.replace("*", "")))
          .or(hasLastname(lastname.replace("*", "")));
    } else if (lastname.startsWith("*")) {
      return hasLastnameSuffix(lastname.replace("*", ""));
    } else if (lastname.endsWith("*")) {
      return hasLastnamePrefix(lastname.replace("*", ""));
    } else {
      return hasLastname(lastname);
    }
  }


  // All search with date: with year, with year and month, with year, month and day
  public static Specification<TestPerson> hasYear(int year) {
    return ((root, query, builder) -> {
      LocalDate startDate = LocalDate.of(year, 1, 1);
      LocalDate endDate = LocalDate.of(year, 12, 31);
      return builder.between(root.get("dateOfBirth"), startDate, endDate);
    });
  }
  public static Specification<TestPerson> hasYearAndMonth(int year,
                                                          int month) {
    return ((root, query, builder) -> {
      LocalDate startDate = LocalDate.of(year, month, 1);
      LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
      return builder.between(root.get("dateOfBirth"), startDate, endDate);
    });
  }
  public static Specification<TestPerson> hasFullDateOfBirth(LocalDate dateOfBirth) {
    return ((root, query, builder) -> builder.equal(root.get("dateOfBirth"), dateOfBirth));
  }

  public static Specification<TestPerson> searchByDateOfBirth(String dateOfBirth) {
    if (dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
      return hasFullDateOfBirth(LocalDate.parse(dateOfBirth));
    } else if (dateOfBirth.matches("\\d{4}-\\d{2}")) {
      return hasYearAndMonth(Integer.parseInt(dateOfBirth.substring(0, 4)), Integer.parseInt(dateOfBirth.substring(5, 7)));
    } else if (dateOfBirth.matches("\\d{4}")) {
      return hasYear(Integer.parseInt(dateOfBirth.substring(0, 4)));
    } else {
      throw new IllegalArgumentException("Wrong date format!" + dateOfBirth);
    }
  }

  public static Specification<TestPerson> searchByNameAndLastname(String name,
                                                                  String lastname) {

    Specification<TestPerson> nameSpec = searchByName(name);
    Specification<TestPerson> lastnameSpec = searchByLastname(lastname);

    return Specification.where(nameSpec.and(lastnameSpec));
  }

  public static Specification<TestPerson> searchByNameAndDateOfBirth(String name,
                                                                     String dateOfBirth) {

    Specification<TestPerson> nameSpec = searchByName(name);
    Specification<TestPerson> dateOfBirthSpec = searchByDateOfBirth(dateOfBirth);

    return Specification.where(nameSpec.and(dateOfBirthSpec));
  }

  public static Specification<TestPerson> searchByLastnameAndDateOfBirth(String lastname,
                                                                         String dateOfBirth) {

    Specification<TestPerson> lastnameSpec = searchByLastname(lastname);
    Specification<TestPerson> dateOfBirthSpec = searchByDateOfBirth(dateOfBirth);

    return Specification.where(lastnameSpec.and(dateOfBirthSpec));
  }

  public static Specification<TestPerson> searchByNameAndLastnameAndDateOfBirth(String name,
                                                                                String lastname,
                                                                                String dateOfBirth) {

    Specification<TestPerson> nameSpec = searchByName(name);
    Specification<TestPerson> lastnameSpec = searchByLastname(lastname);
    Specification<TestPerson> dateOfBirthSpec = searchByDateOfBirth(dateOfBirth);

    return Specification.where(nameSpec.and(lastnameSpec).and(dateOfBirthSpec));
  }
}
