package domain.exceptions;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentRulesViolationException extends Exception {
	private List<String> errors;
	public EnrollmentRulesViolationException(ArrayList<String> errors){
		this.errors = errors;
	}

}