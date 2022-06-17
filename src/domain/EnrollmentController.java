package domain;

import java.util.ArrayList;
import java.util.List;

import domain.exceptions.EnrollmentRulesViolationException;

import static domain.CourseOffering.getUnitsRequested;

public class EnrollmentController {
    public static void enroll(Student student, List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        ArrayList<String> errors = new ArrayList<>();
        Transcript transcript = student.getTranscript();
        errors.addAll(checkAlreadyPassedCourses(courseOfferings, transcript));
        errors.addAll(checkRequirements(courseOfferings, transcript));
        errors.addAll(checkDuplicateEnrollRequest(courseOfferings));
        errors.addAll(checkExamTimeConflicts(courseOfferings));
        errors.addAll(checkUnitsLimit(student, courseOfferings));
        if(errors.size() != 0 )
            throw new EnrollmentRulesViolationException(errors);
        for (CourseOffering courseOffering : courseOfferings)
            student.takeCourse(courseOffering.getCourse(), courseOffering.getSection());
    }

    private static ArrayList<String> checkExamTimeConflicts(List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        ArrayList<String> errors = new ArrayList<>();
        for (CourseOffering courseOffering : courseOfferings) {
            for (CourseOffering courseOffering1 : courseOfferings) {
                if (courseOffering == courseOffering1)
                    continue;
                if (courseOffering.hasExamTimeConflict(courseOffering1))
                    errors.add(String.format("Two offerings %s and %s have the same exam time", courseOffering, courseOffering1));
            }
        }
        return errors;
    }

    private static ArrayList<String> checkRequirements(List<CourseOffering> courseOfferings, Transcript transcript) throws EnrollmentRulesViolationException {
        ArrayList<String> errors = new ArrayList<>();
        for (CourseOffering courseOffering : courseOfferings) {
            List<Course> prereqs = courseOffering.getCourse().getPrerequisites();
            for (Course pre : prereqs)
                if(!transcript.hasPassed(pre))
                    errors.add(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), courseOffering.getCourse().getName()));
        }
        return errors;
    }

    private static ArrayList<String> checkAlreadyPassedCourses(List<CourseOffering> courseOfferings, Transcript transcript) throws EnrollmentRulesViolationException {
        ArrayList<String> errors = new ArrayList<>();
        for (CourseOffering courseOffering : courseOfferings)
            if (transcript.hasPassed(courseOffering.getCourse()))
                errors.add(String.format("The student has already passed %s", courseOffering.getCourse().getName()));
        return errors;
    }

    private static ArrayList<String> checkDuplicateEnrollRequest(List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        ArrayList<String> errors = new ArrayList<>();
        for (CourseOffering courseOffering : courseOfferings) {
            for (CourseOffering courseOffering1 : courseOfferings) {
                if (courseOffering == courseOffering1)
                    continue;
                if (courseOffering.getCourse().equals(courseOffering1.getCourse()))
                    errors.add(String.format("%s is requested to be taken twice", courseOffering.getCourse().getName()));
            }
        }
        return errors;
    }

    private static ArrayList<String> checkUnitsLimit(Student student, List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        ArrayList<String> errors = new ArrayList<>();
        if ((student.getGPA() < Constants.MIN_GPA && getUnitsRequested(courseOfferings) > Constants.MIN_UNITS) ||
                (student.getGPA() < Constants.MID_GPA && getUnitsRequested(courseOfferings) > Constants.MID_UNITS) ||
                (getUnitsRequested(courseOfferings) > Constants.MAX_UNITS))
            errors.add(
                    String.format("Number of units (%d) requested does not match GPA of %f",
                            getUnitsRequested(courseOfferings), student.getGPA()));
        return errors;
    }
}