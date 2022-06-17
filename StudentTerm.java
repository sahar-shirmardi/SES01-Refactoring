package domain;

import java.util.ArrayList;
import java.util.List;

public class StudentTerm {
    private List<TakenCourse> takenCourses;

    StudentTerm() {
        takenCourses = new ArrayList<TakenCourse>();
    }

    public void addTakenCourse(TakenCourse takenCourse) {
        takenCourses.add(takenCourse);
    }

    public List<TakenCourse> getTakenCourses() {
        return takenCourses;
    }
}
