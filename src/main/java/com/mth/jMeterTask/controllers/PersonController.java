package com.mth.jMeterTask.controllers;

import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.models.TestPerson;
import com.mth.jMeterTask.services.PersonService;
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
public class PersonController {

  private final PersonService personService;

  @GetMapping("/detail")
  ResponseEntity<TestPerson> detail(@RequestParam Long id) throws JMeterException {
    return ResponseEntity.ok().body(personService.detail(id));
  }

  @PostMapping("/search")
  ResponseEntity<TestPerson> search(@RequestBody(required = false) Long id, @RequestBody(required = false) String namePrefix, @RequestBody(required = false) String lastnamePrefix, @RequestBody(required = false) String date)
      throws JMeterException {
    return ResponseEntity.ok().body(personService.search(id, namePrefix, lastnamePrefix, date));
  }

  @PatchMapping("{id}/update")
  ResponseEntity<TestPerson> update(@PathVariable Long id, @RequestBody(required = false) String name, @RequestBody(required = false) String lastname)
      throws JMeterException {
    return ResponseEntity.ok().body(personService.update(id, name, lastname));
  }
}
