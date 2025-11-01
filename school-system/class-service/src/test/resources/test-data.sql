-- This is the data script specifically for tests.

DELETE FROM class_students;
DELETE FROM classes;

INSERT INTO classes (id, name, teacher_id) VALUES
(1, 'Test Class A', 100),
(2, 'Test Class B', 200);

INSERT INTO class_students (class_id, student_id) VALUES
(1, 101),
(1, 102),
(2, 201);