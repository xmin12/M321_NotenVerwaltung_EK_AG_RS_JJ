DELETE FROM users;

-- Admin
INSERT INTO users (name, email, password, role)
VALUES ('Admin User', 'admin@school.com', 'admin123', 'ADMIN');

-- Teachers
INSERT INTO users (name, email, password, role)
VALUES ('Mr. Smith', 'smith@school.com', 'teacher123', 'TEACHER'),
       ('Ms. Johnson', 'johnson@school.com', 'teacher456', 'TEACHER'),
       ('Mr. Brown', 'brown@school.com', 'teacher789', 'TEACHER');

-- Students
INSERT INTO users (name, email, password, role)
VALUES ('Alice Müller', 'alice@student.com', 'student001', 'STUDENT'),
       ('Bob Schneider', 'bob@student.com', 'student002', 'STUDENT'),
       ('Clara Becker', 'clara@student.com', 'student003', 'STUDENT'),
       ('Daniel Wagner', 'daniel@student.com', 'student004', 'STUDENT'),
       ('Ella Fischer', 'ella@student.com', 'student005', 'STUDENT'),
       ('Finn Hoffmann', 'finn@student.com', 'student006', 'STUDENT'),
       ('Greta Weber', 'greta@student.com', 'student007', 'STUDENT'),
       ('Henry Koch', 'henry@student.com', 'student008', 'STUDENT'),
       ('Isabella Schäfer', 'isabella@student.com', 'student009', 'STUDENT'),
       ('Jonas Bauer', 'jonas@student.com', 'student010', 'STUDENT'),
       ('Klara Klein', 'klara@student.com', 'student011', 'STUDENT'),
       ('Lukas Wolf', 'lukas@student.com', 'student012', 'STUDENT'),
       ('Mia Zimmermann', 'mia@student.com', 'student013', 'STUDENT'),
       ('Noah Schneider', 'noah@student.com', 'student014', 'STUDENT'),
       ('Olivia Richter', 'olivia@student.com', 'student015', 'STUDENT'),
       ('Paul Neumann', 'paul@student.com', 'student016', 'STUDENT'),
       ('Quinn Braun', 'quinn@student.com', 'student017', 'STUDENT'),
       ('Rosa Krüger', 'rosa@student.com', 'student018', 'STUDENT'),
       ('Samuel Schmid', 'samuel@student.com', 'student019', 'STUDENT'),
       ('Tina Meier', 'tina@student.com', 'student020', 'STUDENT');
