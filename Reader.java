package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class is used to read a json file that will be used in
 * QuizGenerator.java
 *
 */
public class Reader {

	Map<String, ArrayList<Question>> questionMap; // questionMap to hold all of the questions and topics

	public Reader(Map<String, ArrayList<Question>> map) {
		questionMap = map;
	}

	/**
	 * Takes in a file path for a json file and builds the package dependency graph
	 * from it.
	 * 
	 * @param jsonFilepath the name of json data file with package dependency
	 *                     information
	 * @throws FileNotFoundException if file path is incorrect
	 * @throws IOException           if the give file cannot be read
	 * @throws ParseException        if the given json cannot be parsed
	 * @throws JSONInputException    if the image added is invalid, or if the choice
	 *                               validity is invalid
	 * @throws NullPointerException  if the json file does not contain all essential
	 *                               JSON objects
	 */
	public void parseJSONFile(String fileName)
			throws FileNotFoundException, IOException, ParseException, JSONInputException, NullPointerException {
		File file = new File(fileName); // create file object based on image name
		if (file.exists() && !file.isDirectory()) { // check to see if image exists, else throw exception
			if (file.canRead()) { // check if file is valid else throw exception
				Object obj = new JSONParser().parse(new FileReader(fileName));
				JSONObject jo = (JSONObject) obj;
				// creates a JSON array utilizing the questionArray object from the JSON file
				JSONArray questionArray = (JSONArray) jo.get("questionArray");
				for (int i = 0; i < questionArray.size(); i++) { // loop through each JSON Object in questionArray
					JSONObject currentJSON = (JSONObject) questionArray.get(i);
					// get all essential data to create a Question object from the current JSON
					// object in the JSON array
					String metaData = (String) currentJSON.get("meta-data");
					String questionText = (String) currentJSON.get("questionText");
					String topic = (String) currentJSON.get("topic");
					String image = (String) currentJSON.get("image");
					JSONArray choiceArray = (JSONArray) currentJSON.get("choiceArray");
					ArrayList<Choice> choiceList = new ArrayList<Choice>();
					for (int j = 0; j < choiceArray.size(); j++) { // loop through each JSON Object in choiceArray
						JSONObject currentChoiceJSON = (JSONObject) choiceArray.get(j);
						// get all essential data to create a Choice object from the current JSON
						// object in the JSON array
						boolean isCorrect = false;
						String temp = (String) currentChoiceJSON.get("isCorrect");
						if (temp.contentEquals("T")) // Conditionals check to see if validity is valid i.e. T or F
							isCorrect = true;
						else if (!temp.contentEquals("F"))
							throw new JSONInputException(
									"The reader could not determine if one or more of the questions were true or false. Please update your JSON file and be sure to use T or F for choice validity."); // change
																																																		// later
						String choice = (String) currentChoiceJSON.get("choice");
						Choice tempChoice = new Choice(choice, isCorrect);
						choiceList.add(tempChoice);
					}
					Question question;
					if (image.contentEquals("none")) // create a new question object to add to the questionMap
						question = new Question(choiceList, questionText, metaData, topic);
					else {
						try {
							question = new Question(choiceList, image, questionText, metaData, topic);
						} catch (IllegalArgumentException e1) { // checks to see if image exists, else throw exception
							throw new JSONInputException(
									"Error: Not all of the required images were found in your directory. Please upload the correct image files to your directory and try again.");
						}
					}
					// add topic to topic list here to ensure no exceptions will be thrown that will
					// create a topic with no question
					if (!questionMap.containsKey(topic)) { // if the topic isn't in the topiclist, add it to questionMap
						questionMap.put(topic, new ArrayList<Question>());
					}
					questionMap.get(topic).add(question);

				}
			} else {
				throw new IOException();
			}
		} else {
			throw new FileNotFoundException();
		}
	}

	/**
	 * This is an accessor method that returns questionMap
	 * 
	 * @return questionMap
	 */
	public Map<String, ArrayList<Question>> getQuestionMap() {
		return this.questionMap;
	}
}
