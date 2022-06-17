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

    public int getUnits() {
        int units = 0;
        for (TakenCourse takenCourse : takenCourses)
            if (takenCourse.status == TakenCourseStatus.GRADED)
                units += takenCourse.getCourse().getUnits();

        return units;
    }

    public double getWeightedSumOfGrades() {
        double sum = 0;
        for (TakenCourse takenCourse : takenCourses)
            if (takenCourse.status == TakenCourseStatus.GRADED)
                sum += takenCourse.getGrade() * takenCourse.getCourse().getUnits();

        return sum;
    }

    public boolean passedCourse(Course course) {
        for (TakenCourse takenCourse : takenCourses)
            if(takenCourse.getCourse().equals(course) && takenCourse.getGrade() >= 10)
                return true;

        return false;
    }
}