package com.mth.jMeterTask.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
  Long id;
  @Column(name = "name")
  String name;
  @Column(name = "lastname")
  String lastname;
  @Column(name = "birth_number")
  String birthNumber;

}
