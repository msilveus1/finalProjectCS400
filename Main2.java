//package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class Main2 extends Application {

	private Scene exportScene;
	private Scene readScene;
	private Scene mainMenu;
	private Scene addQuestionScene;
	private Map<String, ArrayList<Question>> questionMap;
	private Reader reader;
	private Stage stage;
	private BorderPane mainPane;
	// private Writer writer;

	@Override
	/**
	 * This is the method that starts our GUI
	 */
	public void start(Stage primaryStage) {
		try {
			questionMap = new TreeMap<String, ArrayList<Question>>();
			reader = new Reader(questionMap);
			BorderPane root = new BorderPane();
			// TODO:Check Specifications for the scene
			Scene scene = new Scene(root, 700, 700, Color.BLACK);
			this.mainMenu = scene;
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// This line may not be neccessary

			mainMenu(primaryStage, root);
			// Populating the main menu
			primaryStage.setTitle("Quiz Generator");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			// TODO: Check to see if we need deal with a finally block
			e.printStackTrace();
		}
	}

	public void mainMenu(Stage primaryStage, BorderPane root) {

		stage = primaryStage;
		this.mainPane = root;
		HBox box = new HBox();
		VBox vertical = new VBox();

		// This will handle the exiting of the program
		Button exitProgramButton = new Button("Exit");
		exitProgramButton.setMaxWidth(150);
		exitProgramButton.setOnAction(e -> System.exit(0));
		// Handles the switching to the export screen
		Button exportButton = new Button("Export File");
		exportButton.setMaxWidth(150);
		TextField text = new TextField();
		text.setMaxWidth(250);

		exportButton.setOnAction(e -> exportFileScene(primaryStage, root));
		// Sets up read in screen
		Button readButton = new Button("Read File");
		readButton.setMaxWidth(150);
		box.getChildren().addAll(exitProgramButton, readButton, exportButton);
		readButton.setOnAction(e -> readFileScene(primaryStage, root));
		root.setTop(box);
		
		
		// Start a quiz
        TextField numQuestions = new TextField("");
        Button addQuestion = new Button("Add Question");
        addQuestion.setOnAction(e -> addQuestionScreen(primaryStage, root));

        // Center Hortizontal box
        VBox center = new VBox(10);
        HBox promptNumQ = new HBox();
        promptNumQ.getChildren().addAll(new Label("Enter number of questions: "), numQuestions);
        center.getChildren().addAll(new Label("total number of questions in the database: " + this.countNumQuestion()), promptNumQ);
        root.setAlignment(center, Pos.BOTTOM_CENTER);
        root.setCenter(center);
		
		
		
		// TODO: Set action for button
		// addQuestion.setAction();
		box.getChildren().add(addQuestion);
		ListView<CheckBox> topics = new ListView<CheckBox>();
		ObservableList<CheckBox> topic = FXCollections.observableArrayList();
		if (!this.questionMap.isEmpty()) {
			Iterator<String> iterator = this.questionMap.keySet().iterator();
			while (iterator.hasNext())
				topic.add(new CheckBox(iterator.next()));
		}

		// We added a new vbox to the left pane for the topics
		VBox vbox = new VBox();
		root.setLeft(vbox);
		topics.setItems(topic);
		Label label = new Label("Topic List. Click to select topics");
		vbox.getChildren().addAll(label, topics);
		root.setMargin(vbox, new Insets(50, 30, 30, 30));

		Button startQuiz = new Button("Start Quiz ");
		startQuiz.setMaxWidth(150);
		root.setAlignment(startQuiz, Pos.CENTER);
		root.setBottom(startQuiz);
		startQuiz.setOnAction(e -> startQuiz(numQuestions, topics));
		primaryStage.setScene(this.mainMenu);

	}

	public void readFileScene(Stage primaryStage, BorderPane root) {
		BorderPane pane = new BorderPane();
		this.readScene = new Scene(pane, 500, 500);

		HBox bottom = new HBox(10);
		Button button = new Button("Back");
		button.setMaxWidth(150);
		TextField text = new TextField();
		VBox center = new VBox(7);

		text.setMaxWidth(250);
		text.setMinWidth(100);
		Label prompt = new Label("Please enter the json file name:");
		center.getChildren().add(prompt);
		center.getChildren().add(text);
		Button submitButton = new Button("Submit");
		submitButton.setAlignment(Pos.BOTTOM_RIGHT);
		bottom.getChildren().addAll(button, submitButton);

		HBox blank = new HBox(500);
		blank.getChildren().add(new Label());
		blank.getChildren().add(new Label());
		center.setPadding(new Insets(150, 150, 125, 125));
		pane.setTop(blank);

		pane.setCenter(center);
		pane.setBottom(bottom);

		button.setOnAction(e -> mainMenu(primaryStage, root));
		submitButton.setOnAction(e -> {
			try {
				reader.parseJSONFile(text.getText());
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("The JSON File Was Read Successfully.");
				alert.show();
				mainMenu(this.stage, this.mainPane);

			} catch (FileNotFoundException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(
						"Warning: Entered file not found. Please make sure file is contained within your project folder.");
				alert.show();
				readFileScene(primaryStage, root);

			} catch (IOException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Warning: File could not be read. Please edit your file and try again.");
				alert.show();
				readFileScene(primaryStage, root);

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONInputException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(
						"Warning: JSON File incorrectly formatted. Please edit your JSON File to match the correct file input, then try again.");
				alert.show();
				readFileScene(primaryStage, root);
			}
		});

		primaryStage.setScene(this.readScene);
	}

	public void exportFileScene(Stage primaryStage, BorderPane root) {
		BorderPane pane = new BorderPane();
		this.exportScene = new Scene(pane, 500, 500);
		Button exportButton = new Button("Export Quiz");
		exportButton.setMaxWidth(150);
		HBox bottom = new HBox(100);
		Button button = new Button("Back");
		button.setOnAction(e -> this.mainMenu(this.stage, this.mainPane));
		button.setMaxWidth(150);
		TextField text = new TextField();
		VBox center = new VBox(7);
		text.setMaxWidth(250);
		text.setMinWidth(100);
		Label prompt = new Label("Please enter the json file name:");
		center.getChildren().add(prompt);
		center.getChildren().add(text);
		DirectoryChooser dc = new DirectoryChooser();
		dc.setInitialDirectory(new File("."));
		File[] file = new File[1];
		Button chooseDirectory = new Button("Choose Directory");
		chooseDirectory.setOnAction(e-> {prepOutput(dc,file);});
		// Creation of a new writer
		Writer k = new Writer(questionMap);
	
		String output = text.getText();
		// Export button function set
//		exportButton.setOnAction(e -> {
//			try {
//				String path;
//				if (file != null) {
//					path = file[0].getAbsolutePath();
//					k.output(path, output);
//				} 
//				
//			} catch (IOException e1) {
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setContentText("Warning: File could not be read. Please edit your file and try again.");
//				alert.show();
//			}
//		});
		bottom.setPadding(new Insets(10, 10, 10, 10));
		chooseDirectory.setAlignment(Pos.BOTTOM_RIGHT);
		bottom.getChildren().addAll(chooseDirectory, exportButton);
		pane.setBottom(bottom);
		pane.setCenter(text);
		exportButton.setOnAction(e -> System.out.println(file + output));
		primaryStage.setScene(this.exportScene);

	}
	/**
	 * This is a private helper method to help prep for export
	 */
	private void prepOutput(DirectoryChooser dc, File[] file) {
		file[0] = dc.showDialog(stage);
		

	}

	/**
	 * This is the screen displayed when the user presses add button
	 * 
	 * @param primaryStage
	 * @param root
	 */
	public void addQuestionScreen(Stage primaryStage, BorderPane root) {

		BorderPane pane = new BorderPane();
		this.addQuestionScene = new Scene(pane, 500, 500);

		VBox box = new VBox(10);
		box.setPadding(new Insets(20, 20, 20, 20));
		box.getChildren().add(new Label("Enter the Topic:"));
		TextField topicField = new TextField();
		box.getChildren().add(topicField);

		box.getChildren().add(new Label("Enter the Question: "));
		TextField questionField = new TextField();
		box.getChildren().add(questionField);

		box.getChildren().add(new Label("Enter number of choices for question: "));
		TextField choiceText = new TextField();
		box.getChildren().add(choiceText);

		box.getChildren().add(new Label("(Optional) Enter meta-data for question: "));
		TextField metaData = new TextField();
		box.getChildren().add(metaData);

		box.getChildren().add(new Label("(Optional) Enter image file for the question: "));
		TextField image = new TextField();
		box.getChildren().add(image);

		HBox buttonBox = new HBox(10);
		Button submit = new Button("Submit");
		Button back = new Button("Back");
		buttonBox.getChildren().addAll(back, submit);
		submit.setOnAction(e -> checkInput(choiceText, questionField, topicField, image, metaData));
		back.setOnAction(e -> this.mainMenu(this.stage, this.mainPane));

		pane.setBottom(buttonBox);

		pane.setCenter(box);
		primaryStage.setScene(this.addQuestionScene);
	}

	/**
	 * This is a helper method used to check the input of the user when the user
	 * wishes to add a new question
	 * 
	 * @param c represents the string representation of the number of choices
	 * @param q represents the question text
	 * @param t represents the topic text
	 * @param i represents image text
	 * @param m represents the meta data
	 */
	private void checkInput(TextField c, TextField q, TextField t, TextField i, TextField m) {
		try {
			if (q.getText().equals("") || t.getText().equals("")) {
				throw new IllegalArgumentException(); // if question text or topic is not entered
			}
			Integer s = Integer.parseInt(c.getText()); // check if the choice input is a positive integer
			if (s < 2 || s > 5) {
				throw new NumberFormatException(); // if it's not a positive integer, throw exception
			}
			addChoices(s, q, t, i, m); // if all input is valid, call next scene to add choices

		} catch (NumberFormatException e) { // if user input for choices is not a positive integer
			Alert alert = new Alert(AlertType.ERROR); // display error message
			alert.setContentText(
					"Warning: Invalid input for number of choices. Please enter a positive integer between 2 and 5");
			alert.show(); // show error message and exit
			return;
		} catch (IllegalArgumentException e) { // if the user did not enter question text or topic
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Please enter information for all non-optional details");
			alert.show(); // display error message and exit
			return;
		}
	}

	/**
	 * This is a scene is used to prompt the user to enter choices
	 * 
	 * @param s Represents the question's number of choices
	 * @param q Represents the question's text
	 * @param t Represents the question's topic
	 * @param i Represents the question's image
	 * @param m Represents the question's metadata
	 */

	private void addChoices(Integer s, TextField q, TextField t, TextField i, TextField m) {
		BorderPane pane = new BorderPane(); // create new BorderPane
		Scene scene = new Scene(pane, 600, 600); // new scene
		HBox[] choiceArray = new HBox[s]; // Horizontal box array based on the number of choices
		HBox buttonBox = new HBox(10); // Horizontal box for buttons
		Button back = new Button("Back "); // Create back button
		Button submit = new Button("Confirm"); // Create Submit button
		buttonBox.getChildren().addAll(back, submit); // Add buttons to button horizontal box
		pane.setBottom(buttonBox); // set the button horizontal box at the bottom of the screen

		VBox vbox = new VBox(10); // create new vertical box
		vbox.setPadding(new Insets(30, 30, 30, 30));
		vbox.getChildren().add(new Label( // Add instructions to the vertical box
				"Enter choice and choice answers. For choice answers, enter 'T' to indicate true and 'F' to indicate false"));

		vbox.getChildren().add(new Label("Press Confirm to add your question")); // add instructions to vBox
		HBox menu = new HBox(45); // Create a new horizontal box
		Label choiceInstruction = new Label("Enter answer choices: "); // add instructions to horizontal box
		Label choiceAnswerInstruction = new Label("Enter choice validity: ");// add instructions to horizontal box
		menu.getChildren().addAll(choiceInstruction, choiceAnswerInstruction);
		vbox.getChildren().add(menu);

		for (int count = 0; count < s; count++) { // based on number of choices
			HBox temp = new HBox(15); // create a new horizontal box that contains a textfield and combobox
			temp.getChildren().add(new TextField());
			ComboBox<String> optionBox = new ComboBox<String>();
			optionBox.getItems().add("T"); // ComboBox of string contain two items "T" and "F"
			optionBox.getItems().add("F");
			temp.getChildren().add(optionBox);
			vbox.getChildren().add(temp); // add the horizontal box to the vertical box
			choiceArray[count] = temp; // update the horizontal box array of choices
		}
		submit.setOnAction(e -> finalizeChoice(choiceArray, q, t, i, m)); // call finalizeChoice helper method
		back.setOnAction(e -> addQuestionScreen(stage, mainPane)); // go back to menu if back is pressed
		pane.setCenter(vbox);
		stage.setScene(scene);

	}

	/**
	 * This is a helper method used to create the Choice and Question objects based
	 * on the new question that the user enters
	 * 
	 * @param box
	 * @param q   represents question text
	 * @param t   represents the question topic
	 * @param i   represents the question image
	 * @param m   represents the question metadata
	 */
	private void finalizeChoice(HBox[] box, TextField q, TextField t, TextField i, TextField m) {
		ArrayList<Choice> choice = new ArrayList<Choice>(); // ArrayList to store choice objects
		for (int count = 0; count < box.length; count++) { // loop through the HBox array
			try {
				boolean check = false; // check the choice validity
				TextField cTemp = (TextField) box[count].getChildren().get(0); // get the question text
				ComboBox<String> qTemp = (ComboBox<String>) box[count].getChildren().get(1); // get validity
				if (cTemp.getText().equals("") || qTemp.getValue().equals(null)) {
					throw new NullPointerException(); // if question text or validityis empty, throw exception
				}
				String answer = qTemp.getValue(); // get the choice validity
				if (answer.equalsIgnoreCase("T")) { // if its T set check variable to true
					check = true;
				} else {
					check = false; // else set it to false
				}
				Choice c = new Choice(cTemp.getText(), check); // create new choice Object
				choice.add(c); // add choice object to the choice ArrayList
			} catch (NullPointerException e) {
				Alert alert = new Alert(AlertType.ERROR); // print alert error message
				alert.setContentText("Insufficient information entered for choice: " + (count + 1)
						+ ". Please enter both answer choice and choice validity");
				alert.show();
				return; // exit method
			}
		}
		// Case if the user input a question with an image
		if (!i.getText().equals("")) {
			try { // if user didnt not enter image.
				Question question = new Question(choice, i.getText(), q.getText(), m.getText(), t.getText());
				if (!this.questionMap.containsKey(t.getText())) { // If map does not contains topic
					ArrayList<Question> newQuestionArray = new ArrayList<Question>();
					newQuestionArray.add(question);
					this.questionMap.put(t.getText(), newQuestionArray); // add new topic, and questions to map
					Alert alert = new Alert(AlertType.CONFIRMATION); // confirmation message
					alert.setContentText( // display that question added sucessfully
							"Question addition sucessful. Please enter another question or press back to exit");
					alert.show();
					this.mainMenu(this.stage, this.mainPane);
				} else {
					ArrayList<Question> checkArray = this.questionMap.get(t.getText()); // check the questionArray
					for (int index = 0; index < checkArray.size(); index++) { // loop through array to find duplicate
						if (checkArray.get(index).getQuestion().equals(q.getText())) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setContentText( // show error message and exit method if duplicate exists
									"Question already exists, duplicate questions are not allowed. Please enter another question or press back to exit");
							alert.show();
							this.addQuestionScreen(this.stage, this.mainPane); // return to add question screen if error
						}
					}
					this.questionMap.get(t.getText()).add(question); // if map has topic, add to question list
					Alert alert = new Alert(AlertType.CONFIRMATION); // show confirmation message
					alert.setContentText(
							"Question addition sucessful. Please enter another question or press back to exit");
					alert.show();
					this.mainMenu(this.stage, this.mainPane);
				}
			} catch (IllegalArgumentException e) { // catch illegal argument exception if image
				Alert alert = new Alert(AlertType.ERROR); // does not exist within project folder
				alert.setContentText( // display error message and exit method
						"Entered image does not exist within project folder. Please make sure image is within project before trying again. Press back to exit");
				alert.show();
				this.addQuestionScreen(this.stage, this.mainPane); // return to add question screen if error
			}
		} else { // Case if the user input a question with no image
			Question question = new Question(choice, q.getText(), m.getText(), t.getText());
			if (!this.questionMap.containsKey(t.getText())) { // If map does not contains topic
				ArrayList<Question> newQuestionArray = new ArrayList<Question>();
				newQuestionArray.add(question);
				this.questionMap.put(t.getText(), newQuestionArray); // add new topic, and questions to map
				Alert alert = new Alert(AlertType.CONFIRMATION); // confirmation message
				alert.setContentText(
						"Question addition sucessful. Please enter another question or press back to exit");
				alert.show();
				this.mainMenu(this.stage, this.mainPane);
			} else {
				ArrayList<Question> checkArray = this.questionMap.get(t.getText()); // get questions list
				for (int index = 0; index < checkArray.size(); index++) { // iterate to check for duplicate
					if (checkArray.get(index).getQuestion().equals(q.getText())) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText( // print error message if duplicate question is found
								"Question already exists, duplicate questions are not allowed. Please enter another question or press back to exit");
						alert.show();
						this.addQuestionScreen(this.stage, this.mainPane); // return to add question screen if error
					}
				}
				this.questionMap.get(t.getText()).add(question); // if map has topic, add to question list
				Alert alert = new Alert(AlertType.CONFIRMATION); // display confirmation message
				alert.setContentText(
						"Question addition sucessful. Please enter another question or press back to exit");
				alert.show();
				this.mainMenu(this.stage, this.mainPane);
			}
		}
	}

	private Map<String, ArrayList<Question>> getMap() {
		return this.questionMap;
	}

	/**
     * helps get the total number of questions in data base
     * @return
     */
    private int countNumQuestion() {
        Iterator<ArrayList<Question>> itr = this.questionMap.values().iterator();
        int numQ = 0;
        for(;itr.hasNext();) {
            numQ += itr.next().size();
        }
        return numQ;
    }
    
    
    
    private void startQuiz (TextField TF, ListView<CheckBox> LV) {
        try {
            int quizQNum = Integer.parseInt(TF.getText());
            ArrayList<String> quizTopics = new ArrayList<String>();
            ObservableList<CheckBox> topicList = LV.getItems();
            for(int i = 0; i < topicList.size(); i++) {
                if(topicList.get(i).isSelected())
                    quizTopics.add(topicList.get(i).getText());
            }
            ArrayList<Question> quizQuestions;
            quizQuestions = getQuestionHelper(quizQNum, quizTopics);
            System.out.print(quizQuestions);
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Please enter an integer for the number of questions wanted");
            alert.show(); // display error message and exit
            return;
        }
        
    }
    
    private ArrayList<Question> getQuestionHelper (int numQ, ArrayList<String> quizTopics){
        ArrayList<Question> dataBase = new ArrayList<Question>();
        for(int i = 0; i < quizTopics.size(); i++) {
            dataBase.addAll(this.questionMap.get(quizTopics.get(i)));
        }
        
        //this is a try of stream
        Random rand = new Random();
        ArrayList<Question> quizQuestion = (ArrayList<Question>) rand.
                ints(numQ, 0, dataBase.size()).
                mapToObj(i -> dataBase.get(i)).
                collect(Collectors.toList());
        return quizQuestion;
    }
    
    
	public static void main(String[] args) {
		launch(args);
	}

}