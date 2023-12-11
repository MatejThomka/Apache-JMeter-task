package com.mth.jMeterTask.controllers;

import com.mth.jMeterTask.entities.records.TestPersonRecord;
import com.mth.jMeterTask.entities.TestPerson;
import com.mth.jMeterTask.exceptions.TestPersonNotFoundException;
import com.mth.jMeterTask.services.TestPersonService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test_person")
@RequiredArgsConstructor
@Slf4j
public class TestPersonController {

  private final TestPersonService testPersonService;

  @GetMapping("/detail")
  ResponseEntity<?> detail(@RequestParam @NonNull Integer id) {
    TestPersonRecord testPersonRecord;

    try {
      testPersonRecord = testPersonService.detail(id);
    } catch (TestPersonNotFoundException e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(testPersonRecord, HttpStatus.OK);
  }

  @PostMapping("/search")
  ResponseEntity<?> search(@RequestBody TestPerson testPerson) {

    List<TestPersonRecord> testPersonRecord;

    try {
      testPersonRecord = testPersonService.search(testPerson);
    } catch (TestPersonNotFoundException e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(testPersonRecord, HttpStatus.OK);
  }

  @PatchMapping("{id}/update")
  ResponseEntity<?> update(@PathVariable @NonNull Integer id,
                                    @RequestBody TestPerson testPerson) {

    if (testPerson.getName() == null && testPerson.getLastname() == null) {
      log.warn("Provide minimum one parameter!");
      return new ResponseEntity<>("Provide minimum one parameter!", HttpStatus.NOT_ACCEPTABLE);
    }

    TestPersonRecord testPersonRecord;

    try {
      testPersonRecord = testPersonService.update(id, testPerson);
    } catch (TestPersonNotFoundException e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(testPersonRecord, HttpStatus.OK);
  }
}
