//package application;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Writer {

	Map<String, ArrayList<Question>> map;

	/**
	 * This is the constructor for the writer object
	 * 
	 * @param map      Takes in the map class from the quiz generator class
	 * @param metaData this takes the metadata
	 */
	public Writer(Map<String, ArrayList<Question>> map) {
		// This initially sets the map to
		this.map = map;

	}

	/**
	 * This method updates the map that we have
	 */
	protected void updateMap(Map<String, ArrayList<Question>> map) {
		this.map = map;
	}

	/**
	 * This method writes the Question to a JSON files along with the Meta Data
	 * 
	 * @param filePath This is the file path for the file desired
	 * @param fileName This is the name of the file the desire uses
	 * @throws IOException
	 */
	protected void output(String filePath, String fileName) throws IOException {
		try {
			// Some check for the file path being valid
		} catch (Exception e) {
			// It might throw a custom exception if the file path is not
			// Valid Maybe an illegal
		}
		// This is the original object that we are adding all the things to
		JSONObject obj = new JSONObject();
		// This is the array list for all the topics

		// This is the array list that will hold the array list of all Question
		// For given topic
		ArrayList<ArrayList<Question>> Question = new ArrayList<ArrayList<Question>>();
		getQuestion(Question);
		// Adds all the topics to an array list
		JSONArray questionArray = new JSONArray();
		Iterator<ArrayList<Question>> k = Question.iterator();
		while (k.hasNext()) {
			JSONObject data = new JSONObject();
			// This creates a new data JSON object
			ArrayList<Question> currentQuesList = k.next();
			Iterator<Question> l = currentQuesList.iterator();
			while (l.hasNext()) {
				// Gets the next question to be read
				Question currentQues = l.next();
				data.put("meta-data", currentQues.getMetaData());
				// This put the info into the json object
				data.put("questionText", (String) currentQues.getQuestion());
				data.put("topic", currentQues.getTopic());
				data.put("image", currentQues.getImageName());
				// Makes a JSON array for the data input
				JSONArray choices = new JSONArray();
				ArrayList<Choice> choiceArray = currentQues.getChoiceList();
				JSONArray choiceArrays = new JSONArray();
				// Gets the array list of Questions
				for (int i = 0; i < choiceArray.size(); i++) {
					// This adds all the choices to an array
					// TODO:Need a to String method and validation for each
					JSONObject choice = new JSONObject();
					if(choiceArray.get(i).getChoiceValidity())
						choice.put("isCorrect", "T");
					else
						choice.put("isCorrect","F");
					choice.put("choice", choiceArray.get(i).getChoiceString());
					choiceArrays.add(choice);
					//choices.add(choiceArray.get(i));
				}
				// Adds the current json array to data
				//data.put("choiceArray", choices);
				data.put("choiceArray", choiceArrays);
				questionArray.add(data);
			}
		}
		// Adds the data block to the greater array.
		obj.put("questionArray", questionArray);
		FileWriter file = new FileWriter(filePath.substring(0,filePath.length()-1) + fileName);
		file.write(obj.toJSONString());
		file.close();
	}
	
	private void getQuestion(ArrayList<ArrayList<Question>> Question) {
		 // Gets the set of array list of Question
		 Set<Map.Entry<String, ArrayList<Question>>> questionArrays = map.entrySet();
		 // We retrieve the iterator for the Set
		 Iterator k = questionArrays.iterator();
		 while (k.hasNext()) {
		  Map.Entry<String, ArrayList<Question>> entry = (Entry<String, ArrayList<Question>>) k.next();
		  // Now we put all of the String arraylist into a seperate arraylist
		  Question.add(entry.getValue());
		 }
		}
}
