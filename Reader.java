

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

	Map<String, ArrayList<Question>> questionMap;
	String filePath;

	public Reader(String fileName, Map<String, ArrayList<Question>> map) {
		questionMap = map;
		filePath = fileName;
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
	 */
	public void parseJSONFile() throws FileNotFoundException, IOException, ParseException, JSONInputException {
		File file = new File(filePath);
		if (file.exists() && !file.isDirectory()) {
			if (file.canRead()) {
				Object obj = new JSONParser().parse(new FileReader(filePath));
				JSONObject jo = (JSONObject) obj;
				JSONArray questionArray = (JSONArray) jo.get("questionArray");
				for (int i = 0; i < questionArray.size(); i++) {
					JSONObject currentJSON = (JSONObject) questionArray.get(i);
					String metaData = (String) currentJSON.get("meta-data");
					String questionText = (String) currentJSON.get("questionText");
					String topic = (String) currentJSON.get("topic");
					if (!questionMap.containsKey(topic)) {
						System.out.println(topic);
						questionMap.put(topic, new ArrayList<Question>());
					}
					String image = (String) currentJSON.get("image");
					JSONArray choiceArray = (JSONArray) currentJSON.get("choiceArray");
					ArrayList<Choice> choiceList = new ArrayList<Choice>();
					for (int j = 0; j < choiceArray.size(); j++) {
						JSONObject currentChoiceJSON = (JSONObject) choiceArray.get(j);
						boolean isCorrect = false;
						String temp = (String) currentChoiceJSON.get("isCorrect");
						if (temp.contentEquals("T"))
							isCorrect = true;
						else if (!temp.contentEquals("F"))
							throw new JSONInputException("Invalid Choice Validity in JSON File"); // change later
						String choice = (String) currentChoiceJSON.get("choice");
						Choice tempChoice = new Choice(choice, isCorrect);
						choiceList.add(tempChoice);
					}
					Question question;
					if (image.contentEquals("none"))
						question = new Question(choiceList, questionText, metaData);
					else
						question = new Question(choiceList, image, questionText, metaData);
					System.out.println("print twice");
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

	public static void main(String[] args) {
		Reader r = new Reader("error.json", new HashMap<String, ArrayList<Question>>());
		try {
			r.parseJSONFile();
			Map<String, ArrayList<Question>> temp = r.getQuestionMap();
			ArrayList<Question> test = temp.get("set");
			for (int i = 0; i < test.size(); i++) {
				Question question = test.get(i);
				ArrayList<Choice> temp2 = question.getChoiceList();
				for (int j = 0; j < temp2.size(); j++) {
					Choice choice = temp2.get(j);
					System.out.println(choice.getChoiceString());
				}
			}
		} catch (JSONInputException e) {
			System.out.println("JIException");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
