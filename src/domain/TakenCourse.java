package domain;


public class TakenCourse {
    private Course course;
    private int section;

    TakenCourseStatus status;
    double grade;

    TakenCourse(Course course, int section) {
        this.course = course;
        this.section = section;
        this.status = TakenCourseStatus.NOTGRADED;
        this.grade = 0;
    }

    TakenCourse(Course course, double grade) {
        this.course = course;
        this.section = 0;
        this.status = TakenCourseStatus.GRADED;
        this.grade = grade;
    }

    public Course getCourse() {
        return course;
    }

    public double getGrade() {
        return grade;
    }

    public boolean hasPassed() {
        return status == TakenCourseStatus.GRADED && grade >= Course.MINIMUM_GRADE_TO_PASS;
    }
}