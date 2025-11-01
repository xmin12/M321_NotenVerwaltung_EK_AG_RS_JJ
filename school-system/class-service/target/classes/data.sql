-- Delete all existing data from the tables in the correct order (child table first)
DELETE FROM class_students;
DELETE FROM classes;

-- Reset the primary key sequence for the 'classes' table so it starts from 1 again.
-- This is important for PostgreSQL to avoid primary key conflicts.
ALTER SEQUENCE classes_id_seq RESTART WITH 1;

-- Insert three sample classes into the 'classes' table
INSERT INTO classes (name, teacher_id) VALUES
('Mathematics 101', 10),
('History of Art', 12),
('Introduction to Programming', 15);

-- Insert student IDs into the 'class_students' join table
-- The class_id corresponds to the IDs generated above (1, 2, 3)

-- Students for Mathematics 101 (class_id = 1)
INSERT INTO class_students (class_id, student_id) VALUES
(1, 101), (1, 102), (1, 105), (1, 108);

-- Students for History of Art (class_id = 2)
INSERT INTO class_students (class_id, student_id) VALUES
(2, 101), (2, 103), (2, 104), (2, 107);

-- Students for Introduction to Programming (class_id = 3)
INSERT INTO class_students (class_id, student_id) VALUES
(3, 102), (3, 105), (3, 106), (3, 109), (3, 110);