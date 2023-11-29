package com.mth.jMeterTask.repositories;

import com.mth.jMeterTask.models.TestPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<TestPerson, Integer> {

  TestPerson findById(Long id);
}
