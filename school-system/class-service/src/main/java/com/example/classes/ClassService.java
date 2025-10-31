package com.example.classes;

import com.example.classes.ClassEntity;
import com.example.classes.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }

    public Optional<ClassEntity> getClassById(Long id) {
        return classRepository.findById(id);
    }

    public ClassEntity createClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    public ClassEntity updateClass(Long id, ClassEntity classDetails) {
        ClassEntity classEntity = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));

        classEntity.setName(classDetails.getName());
        classEntity.setTeacherId(classDetails.getTeacherId());
        classEntity.setStudentIds(classDetails.getStudentIds());
        return classRepository.save(classEntity);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    public ClassEntity addStudentToClass(Long classId, Long studentId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        if (!classEntity.getStudentIds().contains(studentId)) {
            classEntity.getStudentIds().add(studentId);
        }
        return classRepository.save(classEntity);
    }

    public ClassEntity removeStudentFromClass(Long classId, Long studentId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        classEntity.getStudentIds().remove(studentId);
        return classRepository.save(classEntity);
    }

    public ClassEntity changeTeacher(Long classId, Long teacherId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        classEntity.setTeacherId(teacherId);
        return classRepository.save(classEntity);
    }
}