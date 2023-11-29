package com.mth.jMeterTask.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
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

  public TestPerson(String name, String lastname, String birthNumber) {
    this.setName(name);
    this.setLastname(lastname);
    this.setBirthNumber(birthNumber);
  }
}
