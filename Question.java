
//
// Title: (Final Project QuizGenerator: Question Class)
// Files: (Choice.java, GUI.java, Main.java, QuizGenerator.java)
// Course: (CS400, Semester 2, and 2019)
//
// Author: (Rei Bezat, Viknesh Ravichandar, Matthew Silveus, Siyu Cai)
// Email: (bezat@wisc.edu, vravichandar@wisc.edu, msilveus@wisc.edu, siyu.cai@wisc.edu)
// Lecturer's Name: (Andy Kuemmel)
// Lecture Number: Lec 004
// Due Date: 5/2/2019
//



import java.io.File;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents a single question within the quiz
 *
 */
public class Question {
	ArrayList<Choice> choiceList; //field to store an ArrayList of Choices for a Question
	ImageView image; //field to store an image for a question
	String question; //field to store the question String
	String metaData; //field to store the meta data for a question
	boolean multipleChoice; //field to store whether the question is multiple choice 
	
	/**
	 * This is a constructor with parameters of ArrayList<Choice> choice,
	 * and String q.
	 * @param choice ArrayList of Choices for a Question in the quiz
	 * @param q  String the represents the question
	 */
	public Question(ArrayList<Choice> choice, String q, String md) {
		choiceList = choice; //initialize choiceList
		question = q; //initialize question
		metaData = md; //initialize metaData
		image = null; 
		isMultipleChoice(); //call helper method isMultipleChoice to initialize multipleChoice field
	}

	/**
	 * This is a constructor with parameters of ArrayList<Choice> choice, 
	 * ImageView image, and String q.
	 * @param choice ArrayList of Choices for a Question in the quiz
	 * @param img  Image that is in the Question
	 * @param q  String the represents the question
	 */
	public Question(ArrayList<Choice> choice, String img, String q, String md) {
		choiceList = choice; //initialize choiceList
		image = new ImageView(new Image(img)); //initialize image //////BROKEN
		question = q; //initialize question
		metaData = md; //initialize metaData
		isMultipleChoice(); //call helper method isMultipleChoice to initialize multipleChoice field
	}

	/**
	 * This is a private helper method used to determine whether the choices in a question
	 * contain multiple true choice options. If there are multiple choices that are true,
	 * multipleChoice field is set to true, otherwise multipleChoice field is set to false.
	 */
	private void isMultipleChoice() {
		int cnt = 0; //counter variable to keep track of number of true choices
		for(int i = 0; i<choiceList.size();i++) { //loop to iterate through the choice list
			if(choiceList.get(i).getChoiceValidity())
				cnt++; //if choice is true, increment the counter
			if(cnt>1) {
				multipleChoice = true; //if there are more than one true choice, update multipleChoice
				return;                //and exit loop
				
			}
		}
		multipleChoice = false;
	}
	
	/**
	 * Accessor method for choiceList field
	 * @return  choiceList
	 */
	public ArrayList<Choice> getChoiceList() {
		return choiceList;
	}

	
	/**
	 * Accessor method image field
	 * @return image
	 */
	public ImageView getImage() {
		return image;
	}

	

	/**
	 * Accessor method for question field
	 * @return question
	 */
	public String getQuestion() {
		return question;
	}

	

	/**
	 * Accessor method for multipleChoice field
	 * @return multipleChoice 
	 */
    boolean getMultipleChoice() {
		return multipleChoice;
	}

	
	
	/**
	 * Accessor method for metaData field
	 * @return metaData
	 */
	public String getMetaData() {
		return metaData;
	}

	
}
