package domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
	private String id;
	private String name;

	private Transcript transcript;
	private StudentTerm currentTerm;

	public Student(String id, String name) {
		this.id = id;
		this.name = name;
		this.transcript = new Transcript();
		this.currentTerm = new StudentTerm();
	}
	
	public void takeCourse(Course c, int section) {
		currentTerm.addTakenCourse(new TakenCourse(c, section));
	}

	public Transcript getTranscript() {
		return transcript;
	}

	public void addTranscriptRecord(Course course, Term term, double grade) {
		transcript.addTakenCourseToTerm(course, term, grade);
    }

    public double getGPA() {
		return transcript.getGPA();
	}

    public StudentTerm getCurrentTerm() {
        return currentTerm;
    }

    public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}