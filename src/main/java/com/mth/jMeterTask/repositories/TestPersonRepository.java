package com.mth.jMeterTask.repositories;

import com.mth.jMeterTask.models.TestPerson;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPersonRepository extends JpaRepository<TestPerson, Integer> {

  TestPerson findById(Long id);
  List<TestPerson> findAllByNameStartingWith(String name);
  List<TestPerson> findAllByLastnameStartingWith(String lastname);
  List<TestPerson> findAllByNameStartingWithAndLastnameStartingWith(String name, String lastname);
  List<TestPerson> findAllByBirthNumberContaining(String yearMonthDay);
}

