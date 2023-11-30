package com.mth.jMeterTask.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mth.jMeterTask.models.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "test_person")
public class TestPerson {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @Column(name = "name")
  String name;
  @Column(name = "lastname")
  String lastname;
  @Column(name = "birth_number")
  String birthNumber;
  @JsonIgnore
  @Column(name = "gender")
  Gender gender;

  public TestPerson(String name, String lastname, String birthNumber, Gender gender) {
    this.setName(name);
    this.setLastname(lastname);
    this.setBirthNumber(birthNumber);
    this.setGender(gender);
  }
}
