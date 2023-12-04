# Apache-JMeter-task
Task about testing code with Apache JMeter

Zadanie prosim robte cele v Java 17 alebo Java 21.
Odovzdat tar/zip gitoveho repozitara.

Adresare "BE" pre cast 1.) a "test" pre cast 2.)

1.) Pomocou Spring Boot, Spring Data alebo Hibernate nachystat neautentifikovany REST+JSON API endpoint.

Aplikacia bude mat interny zoznam evidovanych osob (typ class TestPerson) ulozenych v databaze.
- TestPerson
  - id,
  - meno,
  - priezvisko,
  - validne rodne cislo

Navrhnite si niekolko osob (aspon 20) a ulozte ich do databazy.
Mozete predpokladat, ze v mene cloveka sa nenachadza ciarka.
Mozete predpokladat, ze validacny proces na rodne cislo je zhodny s validacnym procesom bezne pouzivanych rodnych cisiel.

Endpoint bude podporovat 3 rozhrania

1.1) detail (HTTP GET)
- podla IDcka usera

1.2) search (HTTP POST)
- podla atributov (vsetky su optional)
  - id
  - substring meno
  - substring priezvisko
  - rok (YYYY)
  - rok-mesiac (YYYY-MM)
  - rok-mesiac-den (YYYY-MM-DD)
- pri hladani sa pouziju len tie, ktore maju neprazdnu hodnotu a medzi sebou sa spracuju ako and
  - napr. (meno "Pet*" and rok-mesiac "1968-08")

1.3) update (HTTP PATCH)
- podla uvedeneho id opatchovat (vsetky su optional)
  - meno
  - priezvisko
- vramci jedneho volania mozete patchnut kombinacie
  - meno,
  - priezvisko,
  - meno a priezvisko

2.) Napisat test pomocou Apache jMeter (https://jmeter.apache.org/)

2.1) vyhladat pouzivatelov ktori sa volaju "Iva*", ci uz na mieste mena alebo priezviska

2.2) premenovat osoby z bodu 2.1) tak, ze nahradime string "Iva" stringom "Niva"

2.3) otestovat userov (pomocou 1.1), ktori boli aktualizovani pomocou volania z 2.2) a v pripade, ze sa ich detaily nezmenili, pomocou Assert reportovat problem s updatom

!!Complete!