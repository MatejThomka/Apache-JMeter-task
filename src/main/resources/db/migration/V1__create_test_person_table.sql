-- db/migration/V1__create_test_person_table.sql

CREATE TABLE test_person (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255),
                            lastname VARCHAR(255),
                            birth_number VARCHAR(255),
                            gender TINYINT
);