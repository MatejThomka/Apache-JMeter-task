-- db/migration/V2__insert_random_test_persons.sql

-- Insert 20 random users into the test_person table
INSERT INTO test_person (gender, birth_number, lastname, name)
VALUES
    (0, '941122/8134', 'Koala', 'Martin'),
    (0, '981225/8742', 'Vevericka', 'Milan'),
    (0, '521126/118', 'Suchy', 'Peter'),
    (0, '930514/8017', 'Tenky', 'Marian'),
    (0, '990316/8847', 'Kolac', 'Michal'),
    (1, '885914/7858', 'Kralova', 'Eva'),
    (1, '946207/8142', 'Koalova', 'Martina'),
    (0, '770925/4234', 'Koral', 'Ladislav'),
    (0, '530420/156', 'Jezko', 'Ondrej'),
    (0, '840310/7515', 'Finsky', 'Bohuslav'),
    (1, '035715/9121', 'Pomala', 'Zuzana'),
    (1, '005523/9019', 'Rychla', 'Kristina'),
    (1, '975217/8447', 'Hruba', 'Kristina'),
    (0, '821005/7537', 'Mrkva', 'Tibor'),
    (1, '056228/9519', 'Mrkvova', 'Dominika'),
    (0, '940411/8064', 'Dreveny', 'Marcel'),
    (0, '710514/4123', 'Kamen', 'Lukas'),
    (0, '620822/1250', 'Lokomotiva', 'Kamil'),
    (0, '661005/1536', 'Doska', 'Peter'),
    (0, '100725/9847', 'Mrak', 'Michal');
