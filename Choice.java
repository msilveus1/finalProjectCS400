//
// Title: (Final Project QuizGenerator: Choice Class)
// Files: (Question.java, GUI.java, Main.java, QuizGenerator.java)
// Course: (CS400, Semester 2, and 2019)
//
// Author: (Rei Bezat, Viknesh Ravichandar, Matthew Silveus, Siyu Cai)
// Email: (bezat@wisc.edu, vravichandar@wisc.edu, msilveus@wisc.edu, siyu.cai@wisc.edu)
// Lecturer's Name: (Andy Kuemmel)
// Lecture Number: Lec 004
// Due Date: 5/2/2019
//



/**
 * This class represents a single choice within a question in the quiz
 *
 */
public class Choice {
	private String choiceString; //field to store the choice of the question
	private boolean choiceValidity; //field to store the validity of the choice
	
	/**
	 * Constructor initializes choiceString and choiceValidity fields
	 * @param String choice string
	 * @param boolean correct or incorrect
	 */
	public Choice(String cs, boolean cv) {
		choiceString = cs;
		choiceValidity = cv;
	}

	/**
	 * Accessor method for choiceString field
	 * @return choiceString
	 */
	public String getChoiceString() {
		return choiceString;
	}

	
	/**
	 * Accessor method for getChoiceValidity field
	 * @return choiceValidity
	 */
	public boolean getChoiceValidity() {
		return choiceValidity; 
	}

	
}
