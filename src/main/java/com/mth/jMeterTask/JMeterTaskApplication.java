package com.mth.jMeterTask;

import static com.mth.jMeterTask.entities.enums.Gender.FEMALE;
import static com.mth.jMeterTask.entities.enums.Gender.MALE;

import com.mth.jMeterTask.exceptions.JMeterException;
import com.mth.jMeterTask.entities.TestPerson;
import com.mth.jMeterTask.services.TestPersonService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class JMeterTaskApplication implements CommandLineRunner {

  private final TestPersonService testPersonService;

  public static void main(String[] args) {
    SpringApplication.run(JMeterTaskApplication.class, args);
  }

  @Override
  public void run(String... args) throws JMeterException {
    List<TestPerson> listOfPerson = new ArrayList<>();
    TestPerson testPerson = new TestPerson("Martin", "Koala", "941122/8140", MALE);
    TestPerson testPerson1 = new TestPerson("Milan", "Vevericka", "981225/8740", MALE);
    TestPerson testPerson2 = new TestPerson("Peter", "Suchy", "521126/118", MALE);
    TestPerson testPerson3 = new TestPerson("Marian", "Tenky", "930514/8020", MALE);
    TestPerson testPerson4 = new TestPerson("Michal", "Kolac", "990316/8851", MALE);
    TestPerson testPerson5 = new TestPerson("Eva", "Kralova", "885914/7856", FEMALE);
    TestPerson testPerson6 = new TestPerson("Martina", "Koalova", "946207/8148", FEMALE);
    TestPerson testPerson7 = new TestPerson("Ladislav", "Koral", "770925/4235", MALE);
    TestPerson testPerson8 = new TestPerson("Ondrej", "Jezko", "550420/156", MALE);
    TestPerson testPerson9 = new TestPerson("Bohuslav", "Finsky", "840310/7512", MALE);
    TestPerson testPerson10 = new TestPerson("Zuzana", "Pomala", "035715/9125", FEMALE);
    TestPerson testPerson11 = new TestPerson("Kristina", "Rychla", "005523/9017", FEMALE);
    TestPerson testPerson12 = new TestPerson("Kristina", "Hruba", "975217/8447", FEMALE);
    TestPerson testPerson13 = new TestPerson("Tibor", "Mrkva", "821005/7534", MALE);
    TestPerson testPerson14 = new TestPerson("Dominika", "Mrkvova", "056228/9520", FEMALE);
    TestPerson testPerson15 = new TestPerson("Marcel", "Dreveny", "940411/8059", MALE);
    TestPerson testPerson16 = new TestPerson("Lukas", "Kamen", "710514/4125", MALE);
    TestPerson testPerson17 = new TestPerson("Kamil", "Lokomotiva", "620822/1254", MALE);
    TestPerson testPerson18 = new TestPerson("Peter", "Doska", "661005/1536", MALE);
    TestPerson testPerson19 = new TestPerson("Michal", "Mrak", "100725/9851", MALE);

    listingPerson(listOfPerson, testPerson, testPerson1, testPerson2, testPerson3, testPerson4,
        testPerson5, testPerson6, testPerson7, testPerson8, testPerson9);
    listingPerson(listOfPerson, testPerson10, testPerson11, testPerson12, testPerson13,
        testPerson14, testPerson15, testPerson16, testPerson17, testPerson18, testPerson19);

    for (TestPerson ofPerson : listOfPerson) {
      testPersonService.create(ofPerson.getName(), ofPerson.getLastname(),
          ofPerson.getBirthNumber(), ofPerson.getGender());
    }
  }

  private void listingPerson(List<TestPerson> listOfPerson, TestPerson testPerson,
                             TestPerson testPerson1, TestPerson testPerson2, TestPerson testPerson3,
                             TestPerson testPerson4, TestPerson testPerson5, TestPerson testPerson6,
                             TestPerson testPerson7, TestPerson testPerson8,
                             TestPerson testPerson9) {
    listOfPerson.add(testPerson);
    listOfPerson.add(testPerson1);
    listOfPerson.add(testPerson2);
    listOfPerson.add(testPerson3);
    listOfPerson.add(testPerson4);
    listOfPerson.add(testPerson5);
    listOfPerson.add(testPerson6);
    listOfPerson.add(testPerson7);
    listOfPerson.add(testPerson8);
    listOfPerson.add(testPerson9);
  }

}
