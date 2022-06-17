package domain;

import java.util.List;

import domain.exceptions.EnrollmentRulesViolationException;

import static domain.CourseOffering.getUnitsRequested;

public class EnrollmentController {
    public static void enroll(Student student, List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        Transcript transcript = student.getTranscript();
        checkAlreadyPassedCourses(courseOfferings, transcript);
        checkRequirements(courseOfferings, transcript);
        checkDuplicateEnrollRequest(courseOfferings);
        checkExamTimeConflicts(courseOfferings);
        checkUnitsLimit(student, courseOfferings);
        for (CourseOffering courseOffering : courseOfferings)
            student.takeCourse(courseOffering.getCourse(), courseOffering.getSection());
    }

    private static void checkExamTimeConflicts(List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        for (CourseOffering courseOffering : courseOfferings) {
            for (CourseOffering courseOffering1 : courseOfferings) {
                if (courseOffering == courseOffering1)
                    continue;
                if (courseOffering.hasExamTimeConflict(courseOffering1))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", courseOffering, courseOffering1));
            }
        }
    }

    private static void checkRequirements(List<CourseOffering> courseOfferings, Transcript transcript) throws EnrollmentRulesViolationException {
        for (CourseOffering courseOffering : courseOfferings) {
            List<Course> prereqs = courseOffering.getCourse().getPrerequisites();
            for (Course pre : prereqs)
                if(!transcript.hasPassed(pre))
                    throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), courseOffering.getCourse().getName()));
        }
    }

    private static void checkAlreadyPassedCourses(List<CourseOffering> courseOfferings, Transcript transcript) throws EnrollmentRulesViolationException {
        for (CourseOffering courseOffering : courseOfferings)
            if (transcript.hasPassed(courseOffering.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", courseOffering.getCourse().getName()));

    }

    private static void checkDuplicateEnrollRequest(List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        for (CourseOffering courseOffering : courseOfferings) {
            for (CourseOffering courseOffering1 : courseOfferings) {
                if (courseOffering == courseOffering1)
                    continue;
                if (courseOffering.getCourse().equals(courseOffering1.getCourse()))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", courseOffering.getCourse().getName()));
            }
        }
    }

    private static void checkUnitsLimit(Student student, List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        if ((student.getGPA() < Constants.MIN_GPA && getUnitsRequested(courseOfferings) > Constants.MIN_UNITS) ||
                (student.getGPA() < Constants.MID_GPA && getUnitsRequested(courseOfferings) > Constants.MID_UNITS) ||
                (getUnitsRequested(courseOfferings) > Constants.MAX_UNITS))
            throw new EnrollmentRulesViolationException(
                    String.format("Number of units (%d) requested does not match GPA of %f",
                            getUnitsRequested(courseOfferings), student.getGPA()));
    }
}