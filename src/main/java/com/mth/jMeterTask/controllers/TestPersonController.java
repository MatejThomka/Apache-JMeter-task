package com.mth.jMeterTask.controllers;

import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;
import com.mth.jMeterTask.services.TestPersonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class TestPersonController {

  private final TestPersonService testPersonService;

  @GetMapping("/detail")
  ResponseEntity<TestPerson> detail(@RequestParam Long id) throws JMeterException {
    return ResponseEntity.ok().body(testPersonService.detail(id));
  }

  @PostMapping("/search")
  ResponseEntity<List<TestPerson>> search(@RequestBody TestPerson testPerson)
      throws JMeterException {
    return ResponseEntity.ok().body(testPersonService.search(testPerson));
  }

  @PatchMapping("{id}/update")
  ResponseEntity<TestPerson> update(@PathVariable Long id, @RequestBody TestPerson testPerson)
      throws JMeterException {
    return ResponseEntity.ok().body(testPersonService.update(id, testPerson));
  }
}
