package domain;


public class TakenCourse {
    private Course course;
    private int section;

    TakenCourse(Course course, int section) {
        this.course = course;
        this.section = section;
    }

    public Course getCourse() {
        return course;
    }
}