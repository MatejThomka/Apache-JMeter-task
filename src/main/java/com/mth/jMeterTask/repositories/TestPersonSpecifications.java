package com.mth.jMeterTask.repositories;

import com.mth.jMeterTask.entities.TestPerson;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class TestPersonSpecifications {

  public static Specification<TestPerson> hasId(Integer id){
    return (root, query, builder) -> builder.equal(root.get("id"), id);
  }

  public static Specification<TestPerson> hasName(String name) {
    return (root, query, builder) -> builder.equal(root.get("name"), name);
  }

  public static Specification<TestPerson> hasNamePrefix(String namePrefix) {
    return (root, query, builder) -> builder.like(root.get("name"), namePrefix + "%");
  }

  public static Specification<TestPerson> hasNameSuffix(String nameSuffix) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + nameSuffix + "%");
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

  public static Specification<TestPerson> hasLastname(String lastname) {
    return (root, query, builder) -> builder.equal(root.get("lastname"), lastname);
  }

  public static Specification<TestPerson> hasLastnamePrefix(String lastnamePrefix) {
    return (root, query, builder) -> builder.like(root.get("lastname"), lastnamePrefix + "%");
  }

  public static Specification<TestPerson> hasLastnameSuffix(String lastnameSuffix) {
    return (root, query, builder) -> builder.like(root.get("lastname"), "%" + lastnameSuffix + "%");
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

  public static Specification<TestPerson> hasYear(int year) {
    return (root, query, builder) -> {
      LocalDate startDate = LocalDate.of(year, 1, 1);
      LocalDate endDate = LocalDate.of(year, 12, 31);
      return builder.between(root.get("dateOfBirth").as(String.class), startDate.toString(), endDate.toString());
    };
  }

  public static Specification<TestPerson> hasYearAndMonth(int year,
                                                          int month) {
    return (root, query, builder) -> {
      LocalDate startDate = LocalDate.of(year, month, 1);
      LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
      return builder.between(root.get("dateOfBirth").as(String.class), startDate.toString(), endDate.toString());
    };
  }

  public static Specification<TestPerson> hasFullDateOfBirth(int year,
                                                             int month,
                                                             int day) {
    return (root, query, builder) -> {
      LocalDate date = LocalDate.of(year, month, day);
      return builder.equal(root.get("dateOfBirth").as(String.class), date.toString());
    };
  }

  public static Specification<TestPerson> searchByDateOfBirth(String dateOfBirth) {
    if (dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
      return hasFullDateOfBirth(Integer.parseInt(dateOfBirth.substring(0, 4)), Integer.parseInt(dateOfBirth.substring(5, 7)), Integer.parseInt(dateOfBirth.substring(8, 10)));
    } else if (dateOfBirth.matches("\\d{4}-\\d{2}")) {
      return hasYearAndMonth(Integer.parseInt(dateOfBirth.substring(0, 4)), Integer.parseInt(dateOfBirth.substring(5, 7)));
    } else if (dateOfBirth.matches("\\d{4}")) {
      return hasYear(Integer.parseInt(dateOfBirth.substring(0, 4)));
    } else {
      throw new IllegalArgumentException("Wrong date format!" + dateOfBirth);
    }
  }

  public static Specification<TestPerson> hasBirthNumber(String birthNumber) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("birthNumber"), birthNumber);
  }
}
