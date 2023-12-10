package com.mth.jMeterTask.repositories;

import com.mth.jMeterTask.entities.TestPerson;
import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPersonRepository extends JpaRepository<TestPerson, Integer> {

  @NonNull Optional<TestPerson> findById(@NonNull Integer id);

  Set<TestPerson> findAll(Specification<TestPerson> spec);

}

