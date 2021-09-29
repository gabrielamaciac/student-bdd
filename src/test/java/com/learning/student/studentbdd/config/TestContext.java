package com.learning.student.studentbdd.config;

import com.learning.student.studentbdd.payload.Student;
import com.learning.student.studentbdd.payload.StudentSearch;

import java.util.HashMap;
import java.util.Map;

public enum TestContext {

    CONTEXT;

    private static final String STUDENT = "student";
    private static final String STUDENT_SEARCH = "student-search";
    private final Map<String, Object> testContextsMap = new HashMap<>();

    public <T> T get(String name) {
        return (T) testContextsMap.get(name);
    }

    public <T> T set(String name, T object) {
        testContextsMap.put(name, object);
        return object;
    }

    public Student getStudent() {
        return get(STUDENT);
    }

    public void setStudent(Student student) {
        set(STUDENT, student);
    }

    public StudentSearch getStudentSearch() {
        return get(STUDENT_SEARCH);
    }

    public void setStudentSearch(StudentSearch student) {
        set(STUDENT_SEARCH, student);
    }

    public void reset() {
        testContextsMap.clear();
    }
}