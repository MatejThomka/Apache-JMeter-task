package com.mth.jMeterTask.repositories;

import com.mth.jMeterTask.entities.TestPerson;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPersonRepository extends JpaRepository<TestPerson, Integer> {

  @NonNull Optional<TestPerson> findById(@NonNull Integer id);

  List<TestPerson> findAllByNameStartingWith(String name);

  List<TestPerson> findAllByLastnameStartingWith(String lastname);

  List<TestPerson> findAllByNameStartingWithAndLastnameStartingWith(String name, String lastname);

  List<TestPerson> findAllByBirthNumberStartingWith(String yearMonthDay);

  List<TestPerson> findAllByNameStartingWithAndBirthNumberStartingWith(String name,
                                                                       String yearMonthDay);

  List<TestPerson> findAllByLastnameStartingWithAndBirthNumberStartingWith(String lastname,
                                                                           String yearMonthDay);

  List<TestPerson> findAllByNameStartingWithAndLastnameStartingWithAndBirthNumberStartingWith(
      String name, String lastname, String yearMonthDay);
}

