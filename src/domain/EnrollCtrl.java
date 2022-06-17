package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

import static domain.CourseOffering.getUnitsRequested;

public class EnrollCtrl {
    public static void enroll(Student s, List<CourseOffering> courses) throws EnrollmentRulesViolationException {
        Map<Term, Map<Course, Double>> transcript = s.getTranscript();
        for (CourseOffering o : courses) {
            for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
                for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                    if (r.getKey().equals(o.getCourse()) && r.getValue() >= 10)
                        throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
                }
            }
            List<Course> prereqs = o.getCourse().getPrerequisites();
            nextPre:
            for (Course pre : prereqs) {
                for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
                    for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                        if (r.getKey().equals(pre) && r.getValue() >= 10)
                            continue nextPre;
                    }
                }
                throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), o.getCourse().getName()));
            }
        }
        checkDuplicateEnrollRequest(courses);
        for (CourseOffering o : courses) {
            for (CourseOffering o2 : courses) {
                if (o == o2)
                    continue;
                if (o.getExamTime().equals(o2.getExamTime()))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
            }
        }
        checkUnitsLimit(s, courses);
        for (CourseOffering o : courses)
            s.takeCourse(o.getCourse(), o.getSection());
    }

    private static void checkDuplicateEnrollRequest(List<CourseOffering> courses) throws EnrollmentRulesViolationException {
        for (CourseOffering o : courses) {
            for (CourseOffering o2 : courses) {
                if (o == o2)
                    continue;
                if (o.getCourse().equals(o2.getCourse()))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
            }
        }
    }

    private static void checkUnitsLimit(Student s, List<CourseOffering> courses) throws EnrollmentRulesViolationException {
        if ((s.getGPA() < 12 && getUnitsRequested(courses) > 14) ||
                (s.getGPA() < 16 && getUnitsRequested(courses) > 16) ||
                (getUnitsRequested(courses) > 20))
            throw new EnrollmentRulesViolationException(
                    String.format("Number of units (%d) requested does not match GPA of %f",
                            getUnitsRequested(courses), s.getGPA()));
    }
}
