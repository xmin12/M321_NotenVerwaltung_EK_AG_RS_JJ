-- Delete all existing data from the tables in the correct order (child table first)
DELETE FROM class_students;
DELETE FROM classes;

-- Reset the primary key sequence for the 'classes' table so it starts from 1 again.
-- This is important for PostgreSQL to avoid primary key conflicts.
ALTER SEQUENCE classes_id_seq RESTART WITH 1;

INSERT INTO classes (name, teacher_id) VALUES
('Mathematics 101', 2),          -- Mr. Smith
('History of Art', 3),           -- Ms. Johnson
('Introduction to Programming', 4);  -- Mr. Brown

-- -----------------------------
-- Assign Students to Classes
-- -----------------------------

-- Students for Mathematics 101 (class_id = 1)
INSERT INTO class_students (class_id, student_id) VALUES
(1, 5), (1, 6), (1, 9), (1, 12); -- Alice, Bob, Ella, Henry

-- Students for History of Art (class_id = 2)
INSERT INTO class_students (class_id, student_id) VALUES
(2, 5), (2, 7), (2, 8), (2, 11); -- Alice, Clara, Daniel, Greta

-- Students for Introduction to Programming (class_id = 3)
INSERT INTO class_students (class_id, student_id) VALUES
(3, 6), (3, 9), (3, 10), (3, 13), (3, 14); -- Bob, Ella, Finn, Isabella, Jonas