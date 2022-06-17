package domain;

import java.util.HashMap;
import java.util.Map;

public class Transcript {
    final static double DEFAULT_GPA = 0;

    private Map<Term, StudentTerm> transcript;

    Transcript() {
        this.transcript = new HashMap<>();
    }

    public Map<Term, StudentTerm> getTerms() {
        return transcript;
    }

    public void addTakenCourseToTerm(Course course, Term term, double grade) {
        if (!transcript.containsKey(term))
            transcript.put(term, new StudentTerm());
        transcript.get(term).addTakenCourse(new TakenCourse(course, grade));
    }

    public double getGPA() {
        double points = 0;
        int totalsUnits = 0;
        for (Map.Entry<Term, StudentTerm> term : transcript.entrySet()) {
            points += term.getValue().getWeightedSumOfGrades();
            totalsUnits += term.getValue().getUnits();
        }
        if (totalsUnits == 0)
            return DEFAULT_GPA;

        return points / totalsUnits;
    }

    public boolean hasPassed(Course course) {
        for (Map.Entry<Term, StudentTerm> term : transcript.entrySet())
            if (term.getValue().passedCourse(course))
                return true;

        return false;
    }
}