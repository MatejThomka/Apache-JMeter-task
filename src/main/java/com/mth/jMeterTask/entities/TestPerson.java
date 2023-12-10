package com.mth.jMeterTask.entities;

import com.mth.jMeterTask.entities.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestPerson {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;
  String name;
  String lastname;
  String birthNumber;
  String dateOfBirth;
  Gender gender;

  public TestPerson(String name, String lastname, String birthNumber, Gender gender, String dateOfBirth) {
    this.setName(name);
    this.setLastname(lastname);
    this.setBirthNumber(birthNumber);
    this.setDateOfBirth(dateOfBirth);
    this.setGender(gender);
  }
}
