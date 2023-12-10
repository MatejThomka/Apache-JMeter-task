-- db/migration/V2__insert_random_test_persons.sql

-- Insert 20 random users into the test_person table
INSERT INTO test_person (gender, date_of_birth, birth_number, lastname, name)
VALUES
    (0, 1994-11-22, '941122/8134', 'Koala', 'Martin'),
    (0, 1998-12-25, '981225/8742', 'Vevericka', 'Milan'),
    (0, 1952-11-26, '521126/118', 'Suchy', 'Peter'),
    (0, 1993-05-14, '930514/8017', 'Tenky', 'Ivan'),
    (0, (STR_TO_DATE('16-03-1999', '%d-%m-%Y')), '990316/8847', 'Kolac', 'Michal'),
    (1, (STR_TO_DATE('14-09-1988', '%d-%m-%Y')), '885914/7858', 'Kralova', 'Eva'),
    (1, (STR_TO_DATE('07-12-1994', '%d-%m-%Y')), '946207/8142', 'Koalova', 'Martina'),
    (0, (STR_TO_DATE('25-09-1977', '%d-%m-%Y')), '770925/4234', 'Ivanov', 'Ladislav'),
    (0, (STR_TO_DATE('20-04-1953', '%d-%m-%Y')), '530420/156', 'Jezko', 'Ondrej'),
    (0, (STR_TO_DATE('10-03-1984', '%d-%m-%Y')), '840310/7515', 'Finsky', 'Ivanko'),
    (1, (STR_TO_DATE('15-07-2003', '%d-%m-%Y')), '035715/9121', 'Pomala', 'Zuzana'),
    (1, (STR_TO_DATE('23-05-2000', '%d-%m-%Y')), '005523/9019', 'Rychla', 'Kristina'),
    (1, (STR_TO_DATE('17-02-1997', '%d-%m-%Y')), '975217/8447', 'Hruba', 'Kristina'),
    (0, (STR_TO_DATE('05-10-1982', '%d-%m-%Y')), '821005/7537', 'Mrkva', 'Tibor'),
    (1, (STR_TO_DATE('28-12-2005', '%d-%m-%Y')), '056228/9519', 'Ivanovova', 'Dominika'),
    (0, (STR_TO_DATE('11-04-1994', '%d-%m-%Y')), '940411/8064', 'Dreveny', 'Ivan'),
    (0, (STR_TO_DATE('14-05-1971', '%d-%m-%Y')), '710514/4123', 'Kamen', 'Lukas'),
    (0, (STR_TO_DATE('22-08-1962', '%d-%m-%Y')), '620822/1250', 'Lokomotiva', 'Kamil'),
    (0, (STR_TO_DATE('05-10-1966', '%d-%m-%Y')), '661005/1536', 'Doska', 'Peter'),
    (0, (STR_TO_DATE('25-07-2010', '%d-%m-%Y')), '100725/9847', 'Mrak', 'Michal');
