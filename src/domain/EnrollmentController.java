package domain;

import java.util.List;

import domain.exceptions.EnrollmentRulesViolationException;

import static domain.CourseOffering.getUnitsRequested;

public class EnrollmentController {
    public static void enroll(Student s, List<CourseOffering> courses) throws EnrollmentRulesViolationException {
        Transcript transcript = s.getTranscript();
        checkAlreadyPassedCourses(courses, transcript);
        checkRequirements(courses, transcript);
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

    private static void checkRequirements(List<CourseOffering> courses, Transcript transcript) throws EnrollmentRulesViolationException {
        for (CourseOffering o : courses) {
            List<Course> prereqs = o.getCourse().getPrerequisites();
            for (Course pre : prereqs)
                if(!transcript.hasPassed(pre))
                    throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), o.getCourse().getName()));
        }
    }

    private static void checkAlreadyPassedCourses(List<CourseOffering> courses, Transcript transcript) throws EnrollmentRulesViolationException {
        for (CourseOffering o : courses)
            if (transcript.hasPassed(o.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));

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