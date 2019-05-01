
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
	private ArrayList<Choice> choiceList; // field to store an ArrayList of Choices for a Question
	private Image image; // field to store an image for a question
	private String question; // field to store the question String
	private String metaData; // field to store the meta data for a question
	private String topic; // the topic associated with this question
	private String imageName;
	private boolean multipleChoice; // field to store whether the question is multiple choice

	/**
	 * This is a constructor with parameters of ArrayList<Choice> choice, and String
	 * q.
	 * 
	 * @param choice ArrayList of Choices for a Question in the quiz
	 * @param q      String the represents the question
	 */
	public Question(ArrayList<Choice> choice, String q, String md, String t) {
		choiceList = choice; // initialize choiceList
		question = q; // initialize question
		metaData = md; // initialize metaData
		topic = t; // initialize topic
		imageName = "none";
		image = null;
		isMultipleChoice(); // call helper method isMultipleChoice to initialize multipleChoice field
	}

	/**
	 * This is a constructor with parameters of ArrayList<Choice> choice, ImageView
	 * image, and String q.
	 * 
	 * @param choice ArrayList of Choices for a Question in the quiz
	 * @param img    Image that is in the Question
	 * @param q      String the represents the question
	 */
	public Question(ArrayList<Choice> choice, String img, String q, String md, String t) {
		choiceList = choice; // initialize choiceList
		image = new Image(img); //initialize image //////BROKEN


		question = q; // initialize question
		metaData = md; // initialize metaData
		imageName = img;
		topic = t; // initialize topic
		isMultipleChoice(); // call helper method isMultipleChoice to initialize multipleChoice field
	}

	/**
	 * This is a private helper method used to determine whether the choices in a
	 * question contain multiple true choice options. If there are multiple choices
	 * that are true, multipleChoice field is set to true, otherwise multipleChoice
	 * field is set to false.
	 */
	private void isMultipleChoice() {
		int cnt = 0; // counter variable to keep track of number of true choices
		for (int i = 0; i < choiceList.size(); i++) { // loop to iterate through the choice list
			if (choiceList.get(i).getChoiceValidity())
				cnt++; // if choice is true, increment the counter
			if (cnt > 1) {
				multipleChoice = true; // if there are more than one true choice, update multipleChoice
				return; // and exit loop
			}
		}
		multipleChoice = false;
	}
	

	/**
	 * Accessor method for topic field
	 * 
	 * @return topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Mutator method for topic field
	 * 
	 * @param topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	/**
	 * Accessor method for imageName field
	 * 
	 * @return imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * Mutator method for imageName field
	 * 
	 * @param imageName
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * Accessor method for choiceList field
	 * 
	 * @return choiceList
	 */
	public ArrayList<Choice> getChoiceList() {
		return choiceList;
	}

	/**
	 * Mutator method for choice field
	 * 
	 * @param choiceList
	 */
	void setChoiceList(ArrayList<Choice> choiceList) {
		this.choiceList = choiceList;
	}

	/**
	 * Accessor method image field
	 * 
	 * @return image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Mutator method for image field
	 * @param image
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * Accessor method for question field
	 * 
	 * @return question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Mutator method for question field
	 * 
	 * @param question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Accessor method for multipleChoice field
	 * 
	 * @return multipleChoice
	 */
	boolean getMultipleChoice() {
		return multipleChoice;
	}

	/**
	 * Mutator method for multipleChoice field
	 * 
	 * @param multipleChoice
	 */
	public void setMultipleChoice(boolean multipleChoice) {
		this.multipleChoice = multipleChoice;
	}

	/**
	 * Accessor method for metaData field
	 * 
	 * @return metaData
	 */
	public String getMetaData() {
		return metaData;
	}

	/**
	 * Mutator method for metaData field
	 * 
	 * @param multipleChoice
	 */
	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}
}
