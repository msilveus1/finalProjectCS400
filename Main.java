package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.json.simple.parser.ParseException;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application {

	private Scene exportScene; // This field stores the scene when user wants to export a file
	private Scene readScene; // This field stores the scene when the user wants to read a file
	private Scene mainMenu; // This field stores the scene for the main menu
	private Scene addQuestionScene; // this field stores the scene for the add question scene
	private Map<String, ArrayList<Question>> questionMap; // this field stores the questions database
	private Reader reader; // this field stores the reader that will be used to read a JSON file
	private Stage stage; // this field stores the primary stage
	private BorderPane mainPane; // this field stores the root borderpane
	private Writer writer; // this field stores the writer that will be used to write out a JSON file
	private Scene questionScene; // this field stores the scene that when the quiz starts
	private String correctChoice; // this field stores the correct choice for each question in the quiz
	private int answered; // this field stores the number of answered questions in a quiz
	private int correct; // this field stores the numberof correct answered questions in a quiz

	@Override
	/**
	 * This is the method that starts our GUI
	 */
	public void start(Stage primaryStage) {
		try {
			questionMap = new TreeMap<String, ArrayList<Question>>();
			reader = new Reader(questionMap);
			writer = new Writer(questionMap);
			BorderPane root = new BorderPane();
			// TODO:Check Specifications for the scene
			Scene scene = new Scene(root, 700, 700, Color.BLACK);
			this.mainMenu = scene;
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// This line may not be neccessary
			// Set close
			primaryStage.setOnCloseRequest(e -> {
				if (!this.questionMap.isEmpty())
					promptSaveHelper();
				else
					System.exit(0);
			});
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
		HBox box = new HBox(5);
		VBox vertical = new VBox(10);

		// This will handle the exiting of the program. Prompts user option to save file if database is not empty
		Button exitProgramButton = new Button("Exit");
		exitProgramButton.setMaxWidth(150);
		exitProgramButton.setOnAction(e -> {
			if (!this.questionMap.isEmpty())
				promptSaveHelper();
			else
				System.exit(0);		
		});
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
		TextField numQuestions = new TextField();
		Button addQuestion = new Button("Add Question");
		addQuestion.setOnAction(e -> addQuestionScreen(primaryStage, root));

		// Center Hortizontal box
		VBox center = new VBox(10);
		HBox promptNumQ = new HBox();
		promptNumQ.getChildren().addAll(new Label("Enter number of questions: "), numQuestions);
		center.getChildren().addAll(new Label("Total number of questions in the database: " + this.countNumQuestion()),
				promptNumQ);
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
		Label label = new Label(
				"Topic List. Click to select topics\n If there are no topics, add a question or import a file.");
		vbox.getChildren().addAll(label, topics);
		root.setMargin(vbox, new Insets(50, 30, 30, 30));

		Button startQuiz = new Button("Start Quiz ");
		startQuiz.setMaxWidth(150);
		root.setAlignment(startQuiz, Pos.CENTER);
		root.setBottom(startQuiz);
		startQuiz.setOnAction(e -> startQuiz(numQuestions, topics));
		primaryStage.setScene(this.mainMenu);

	}

	/**
	 * This is the scene that will be displayed when the user wants to read in a
	 * JSON file
	 * 
	 * @param primaryStage
	 * @param root
	 */
	public void readFileScene(Stage primaryStage, BorderPane root) {
		BorderPane pane = new BorderPane();
		this.readScene = new Scene(pane, 500, 500);
		// Set close
		primaryStage.setOnCloseRequest(e -> {
			if (!this.questionMap.isEmpty())
				promptSaveHelper();
			else
				System.exit(0);
		});
		HBox bottom = new HBox(10);
		Button button = new Button("Back");
		button.setMaxWidth(150);
		TextField text = new TextField();
		VBox center = new VBox(7);

		text.setMaxWidth(250);
		text.setMinWidth(100);
		Label prompt = new Label("Please enter the JSON file name: \nPress Submit to read JSON file.");
		center.getChildren().add(prompt);
		center.getChildren().add(text);
		Button submitButton = new Button("Submit");
		submitButton.setAlignment(Pos.BOTTOM_RIGHT);
		// Stylization
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0);
		ds.setOffsetX(3.0);
		ds.setColor(Color.LIGHTGRAY);
		button.setEffect(ds);
		submitButton.setEffect(ds);
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
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(
						"There was an error in parsing your JSON File. Please make sure that your JSON File conforms to JSON formatting guidelines.");
				alert.show();
				mainMenu(primaryStage, root);
			} catch (JSONInputException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(e1.getMessage()
						+ " Any correctly formatted questions have been successfully added to the question bank.");
				alert.show();
				mainMenu(primaryStage, root);
			} catch (NullPointerException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(
						"The JSON File is missing some of the required content to run the quiz. Make sure that the JSON File contains: questionsArray, meta-data, questionText, topic, image, choiceArray, isCorrect, and choice objects.");
				alert.show();
				mainMenu(primaryStage, root);
			}
		});

		primaryStage.setScene(this.readScene);
	}

	/**
	 * This is the scene when a file is desired to be written.
	 * 
	 * @param primaryStage
	 * @param root
	 */
	public void exportFileScene(Stage primaryStage, BorderPane root) {
		// Set close
		primaryStage.setOnCloseRequest(e -> {
			if (!this.questionMap.isEmpty())
				promptSaveHelper();
			else
				System.exit(0);
		});

		// Setting up the scene for export
		BorderPane pane = new BorderPane();
		this.exportScene = new Scene(pane, 500, 500);
		// This is for the buttons
		HBox bottom = new HBox(20);
		// This is for the label and the text field
		VBox center = new VBox(7);
		// Setting up the buttons
		Button button = new Button("Back");
		button.setOnAction(e -> this.mainMenu(this.stage, this.mainPane));
		button.setMaxWidth(150);
		TextField text = new TextField();
		// Stylization

		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0);
		ds.setOffsetX(3.0);
		ds.setColor(Color.GRAY);
		// Creating the text field parameters
		text.setMaxWidth(250);
		text.setMinWidth(100);
		button.setEffect(ds);

		// Adding about a new label
		Label prompt = new Label("Please enter the JSON file name and hit submit when done:");
		center.getChildren().add(prompt);
		center.getChildren().add(text);
		FileChooser dc = new FileChooser();
		dc.setInitialDirectory(new File("."));
		Button chooseDirectory = new Button("Export to File");
		// it works
		// This is for essentially the submit button
		chooseDirectory.setOnAction(e -> {
			try {
				writer.output(dc.getInitialDirectory().getAbsolutePath(), text.getText());
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("File exported sucessfully to local project folder ");
				alert.show();
				this.mainMenu(this.stage, this.mainPane);

			} catch (IOException e2) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Warning: File could not be read. Please edit your file and try again.");
				alert.show();
			}
		});
		chooseDirectory.setEffect(ds);
		// Creation of a new writer
		Writer k = new Writer(questionMap);
		// Export button function set
		bottom.setPadding(new Insets(20, 20, 20, 20));
		chooseDirectory.setAlignment(Pos.BOTTOM_RIGHT);
		bottom.getChildren().addAll(button, chooseDirectory);
		center.setPadding(new Insets(160, 0, 0, 130));
		pane.setBottom(bottom);
		pane.setCenter(center);
		primaryStage.setScene(this.exportScene);

	}

	/**
	 * This is the screen displayed when the user presses add button
	 * 
	 * @param primaryStage
	 * @param root
	 */
	public void addQuestionScreen(Stage primaryStage, BorderPane root) {
		// This sets up the GUI
		BorderPane pane = new BorderPane();
		this.addQuestionScene = new Scene(pane, 500, 500);
		// Set close action
		primaryStage.setOnCloseRequest(e -> {
			if (!this.questionMap.isEmpty())
				promptSaveHelper();
			else
				System.exit(0);
		});
		// Creates the box for the questions
		VBox box = new VBox(10);
		box.setPadding(new Insets(20, 20, 20, 20));
		// Adding the new label
		box.getChildren().add(new Label("Enter the Topic:"));
		TextField topicField = new TextField();
		box.getChildren().add(topicField);
		// Adding the next label and text field for question
		box.getChildren().add(new Label("Enter the Question: "));
		TextField questionField = new TextField();
		box.getChildren().add(questionField);
		// setting up the text field for number of choices
		box.getChildren().add(new Label("Enter number of choices for question: "));
		TextField choiceText = new TextField();
		box.getChildren().add(choiceText);
		// Adding meta data
		box.getChildren().add(new Label("(Optional) Enter meta-data for question: "));
		TextField metaData = new TextField();
		box.getChildren().add(metaData);
		// adding the image setting the label and text field
		box.getChildren().add(new Label("(Optional) Enter image file for the question: "));
		TextField image = new TextField();
		box.getChildren().add(image);
		// This sets up more hbox for the buttons
		HBox buttonBox = new HBox(10);
		Button submit = new Button("Submit");
		Button back = new Button("Back");
		// Stylization
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0);
		ds.setOffsetX(3.0);
		ds.setColor(Color.LIGHTGREY);
		back.setEffect(ds);
		submit.setEffect(ds);
		buttonBox.getChildren().addAll(back, submit);
		// Setting padding for the buttons
		buttonBox.setPadding(new Insets(10, 10, 10, 10));
		submit.setOnAction(e -> checkInput(choiceText, questionField, topicField, image, metaData));
		back.setOnAction(e -> this.mainMenu(this.stage, this.mainPane));

		pane.setBottom(buttonBox);
		// sets up the pane
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
				"Enter choice and choice answers. For choice answers, enter 'T' to indicate true and 'F' to indicate false \n \n Warning : All Progress will be lost if you press back without adding question. \n"));

		vbox.getChildren().add(new Label("Press Confirm to add your question \n \n")); // add instructions to vBox
		HBox menu = new HBox(45); // Create a new horizontal box
		Label choiceInstruction = new Label("Enter answer choices: "); // add instructions to horizontal box
		Label choiceAnswerInstruction = new Label(
				"Enter 'T' for correct and 'F' for incorrect: \n Only one correct answer is allowed");// add
																										// instructions
																										// to horizontal
																										// box
		menu.getChildren().addAll(choiceInstruction, choiceAnswerInstruction);
		vbox.getChildren().add(menu);
		// Setting padding for the buttons
		buttonBox.setPadding(new Insets(10, 10, 10, 10));
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
		int tcnt = 0;
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
					tcnt++;
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
		if (tcnt <= 0) {
			Alert alert = new Alert(AlertType.ERROR); // confirmation message
			alert.setContentText( // display that question added sucessfully
					"Please make sure that there is at least one correct answer to the question.");
			alert.show();
			return;
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
				this.questionMap.get(t.getText()).add(question); // if map has topic, add to question list
				Alert alert = new Alert(AlertType.CONFIRMATION); // display confirmation message
				alert.setContentText(
						"Question addition sucessful. Please enter another question or press back to exit");
				alert.show();
				this.mainMenu(this.stage, this.mainPane);
			}
		}
	}

	/**
	 * This is an accessor method that returns the map that represents the questions
	 * database
	 * 
	 * @return
	 */
	private Map<String, ArrayList<Question>> getMap() {
		return this.questionMap; // returns the questions database map
	}

	/**
	 * helps get the total number of questions in data base
	 * 
	 * @return
	 */
	private int countNumQuestion() {
		Iterator<ArrayList<Question>> itr = this.questionMap.values().iterator();
		int numQ = 0;
		for (; itr.hasNext();) {
			numQ += itr.next().size();
		}
		return numQ;
	}

	/**
	 * Extracting number of questions entered by the user in the main menu text
	 * field
	 * 
	 * @param TF This is a text field that is being passed in
	 * @param LV List of all topics selected
	 */
	private void startQuiz(TextField TF, ListView<CheckBox> LV) {
		try {
			// Creating a list of quizTopics
			ArrayList<String> quizTopics = new ArrayList<String>();
			// getting the list of check boxes
			ObservableList<CheckBox> topicList = LV.getItems();
			// Checks if the check box is selected
			for (int i = 0; i < topicList.size(); i++) {
				if (topicList.get(i).isSelected())
					// Adds the topic to the list if selected
					quizTopics.add(topicList.get(i).getText());
			}
			// Sets up an array for quiz question
			ArrayList<Question> quizQuestions;
			if (quizTopics.size() == 0) {
				// If no topic is selected throw illegal argument exception
				throw new IllegalArgumentException();
			}
			// Gets the integer from the the text field
			int quizQNum = Integer.parseInt(TF.getText());
			if (quizQNum < 1) {
				// Entered number must be positive integer
				throw new NumberFormatException();
			}
			// if all input is valid then it calls quiz questions method
			quizQuestions = getQuestionHelper(quizQNum, quizTopics);
			// Sets up all the numbers needed for the counters
			int counter = 1;
			this.answered = 0;
			this.correct = 0;
			// Calls the quiz scene with the right numbers
			quizScene(this.stage, this.mainPane, counter, quizQuestions, answered, correct);
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Please enter a positive integer for the number of questions wanted");
			alert.show(); // display error message and exit
			return;
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(
					"No topic selected. Please select a topic. If there are no topics available, read in a JSON file or add in a topic manually");
			alert.show(); // display error message and exit
			return;
		}

	}

	/**
	 * This is the class that has the algorithm to get the random question.
	 * 
	 * @param numQ       number of questions selected by the user
	 * @param quizTopics a list of all quiz topics selected
	 * @return a randomized list of questions.
	 */
	private ArrayList<Question> getQuestionHelper(int numQ, ArrayList<String> quizTopics) {
		// These are setting up the lists of questions from our topics
		ArrayList<Question> dataBase = new ArrayList<Question>();
		ArrayList<Question> toReturn = new ArrayList<Question>();
		// We are adding all the question topics to the database
		for (int i = 0; i < quizTopics.size(); i++) {
			dataBase.addAll(this.questionMap.get(quizTopics.get(i)));
		}
		// if the number of questions is larger that the data bas size we reset it to be
		// the right size
		if (numQ > dataBase.size())
			numQ = dataBase.size();
		// this generating a random index from the data base size and then we get
		// whatever is in
		// random index and add it to question list
		for (int i = 0; i < numQ; i++) {
			int index = (int) (Math.random() * (dataBase.size()));
			toReturn.add(dataBase.get(index));
			dataBase.remove(index);
		}
		// This returns the list of questions.
		return toReturn;
	}

	/**
	 * This is the method that sets up the quiz scene
	 * 
	 * @param primaryStage this is passed in from main
	 * @param root         this is the pane from the last scene
	 * @param counter      this counts all the questions that we have gone through
	 * @param quizQuestion
	 * @param answered
	 * @param correct
	 */
	private void quizScene(Stage primaryStage, BorderPane root, int counter, ArrayList<Question> quizQuestion,
			int answered, int correct) {
		// Sets up new Scene and border pane
		BorderPane pane = new BorderPane();
		Scene scene;

		// Setting up close request
		primaryStage.setOnCloseRequest(e -> {
			if (!this.questionMap.isEmpty())
				promptSaveHelper();
			else
				System.exit(0);
		});
		if (counter > 1 && counter < quizQuestion.size()) {
			pane = new BorderPane();
			scene = new Scene(pane, 1000, 800);
			Label questionLabel = new Label(counter + ".)  " + quizQuestion.get(counter - 1).getQuestion());
			questionLabel.setMaxHeight(100);
			// Sets the choice and question box padding
			VBox questionAndChoiceBox = new VBox(10);
			questionAndChoiceBox.setPadding(new Insets(10, 10, 10, 10));
			questionAndChoiceBox.getChildren()
					.add(new Label("Select a choice and press Submit to view the results of the question."));
			questionAndChoiceBox.getChildren().add(new Label(
					"INSTRUCTIONS: You must submit the question before moving on to the next question \nOnce submitted your result will be displayed at the bottom right "));
			questionAndChoiceBox.getChildren().add(questionLabel);

			HBox buttonBox = new HBox(10);
			Button nextButton = new Button("Next Question");
			Button backButton = new Button("Back ");
			Button exit = new Button("Exit");
			Button finish = new Button("Finish");
			Button submit = new Button("Submit Question");
			// We are setting the button box padding
			buttonBox.setPadding(new Insets(10, 10, 10, 10));
			int next = counter + 1;
			int back = counter - 1;
			// Set Exit to transition to main menu
			exit.setOnAction(e -> mainMenu(this.stage, this.mainPane));
			// This is the selection of and displaying of the questions in the quiz.
			// Gets the current question
			Question currentQuestion = quizQuestion.get(counter - 1);
			String correctChoice = null;
			// Sets a size for the choice pane
			VBox choicePane = new VBox(10);
			choicePane.setPadding(new Insets(10, 10, 10, 10));

			ToggleGroup choiceButton = new ToggleGroup();
			ArrayList<Choice> choiceList = currentQuestion.getChoiceList();
			// This is checking the current question has multiple choices are correct
			// The Radio button for a single correct answer
			for (int i = 0; i < choiceList.size(); i++) {
				// Getting the Choice
				Choice tempChoice = choiceList.get(i);
				// Creation of the radio button
				RadioButton tempRadButton = new RadioButton(tempChoice.getChoiceString());
				if (tempChoice.getChoiceValidity() == true) {
					this.correctChoice = tempChoice.getChoiceString();
				}
				tempRadButton.setToggleGroup(choiceButton);
				choicePane.getChildren().add(tempRadButton);
			}

			if (currentQuestion.getImage() != null) {
				ImageView tempImage = new ImageView(currentQuestion.getImage());
				tempImage.setFitHeight(250);
				tempImage.setFitWidth(250);
				pane.setCenter(tempImage);
			}

			VBox right = new VBox(10);
			right.setPadding(new Insets(10, 10, 10, 10));
			right.getChildren().add(new Label("Total questions in quiz: " + quizQuestion.size()));
			right.getChildren().add(new Label("Current question: " + counter));
			right.setAlignment(Pos.BOTTOM_RIGHT);
			// Setting next button action
			nextButton.setOnAction(
					e -> quizScene(this.stage, this.mainPane, next, quizQuestion, this.answered, this.correct));
			backButton.setOnAction(
					e -> quizScene(this.stage, this.mainPane, back, quizQuestion, this.answered, this.correct));
			buttonBox.getChildren().addAll(exit, submit);

			// Transition effect
			FadeTransition fade = new FadeTransition();
			fade.setDuration(Duration.millis(1000));
			fade.setFromValue(0);
			fade.setToValue(10);
			fade.setAutoReverse(true);
			fade.setNode(pane);
			fade.play();

			submit.setOnAction(e -> {
				RadioButton tempRadio = (RadioButton) choiceButton.getSelectedToggle();
				if (tempRadio == null) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setContentText("Warning. No choice selected");
					alert.show();
				} else {
					this.answered++;
					if (tempRadio.getText().equals(this.correctChoice)) {
						this.correct++;
						right.getChildren().add(new Label("Correct Answer"));
					} else {
						right.getChildren().add(new Label("Wrong Answer"));
					}
					buttonBox.getChildren().add(nextButton);
					buttonBox.getChildren().remove(1);

				}
			});

			pane.setLeft(choicePane);
			pane.setTop(questionAndChoiceBox);
			pane.setRight(right);
			pane.setBottom(buttonBox);
			this.stage.setScene(scene);
		}

		if (counter == 1) {
			pane = new BorderPane();
			scene = new Scene(pane, 1000, 800);

			Label questionLabel = new Label(counter + ".)  " + quizQuestion.get(counter - 1).getQuestion());
			questionLabel.setMaxHeight(100);
			HBox buttonBox = new HBox(10);
			// We are setting the button box padding
			buttonBox.setPadding(new Insets(10, 10, 10, 10));
			// Setting up the question and choice box for left side of the screen
			VBox questionAndChoiceBox = new VBox(10);
			questionAndChoiceBox.setPadding(new Insets(10, 10, 10, 10));
			// Adding the question instruction labels
			questionAndChoiceBox.getChildren()
					.add(new Label("Select a choice and press Submit to view the results of the question."));
			questionAndChoiceBox.getChildren().add(new Label(
					"INSTRUCTIONS: You must submit the question before moving on to the next question \nOnce submitted your result will be displayed at the bottom right "));
			questionAndChoiceBox.getChildren().add(questionLabel);
			// Creating the various buttons that we need
			Button nextButton = new Button("Next Question");
			Button exit = new Button("Exit");
			Button finish = new Button("Finish");
			Button submit = new Button("Submit Question");
			// Setting up the various needed parametes for
			int next = counter + 1;
			int back = counter - 1;
			// Setting up action for next button
			nextButton.setOnAction(
					e -> quizScene(this.stage, this.mainPane, next, quizQuestion, this.answered, this.correct));

			// Set Exit to transition to main menu
			exit.setOnAction(e -> mainMenu(this.stage, this.mainPane));
			// This is the selection of and displaying of the questions in the quiz.
			Question currentQuestion = quizQuestion.get(counter - 1);
			// Creating pane for the choices
			VBox choicePane = new VBox(10);
			choicePane.setPadding(new Insets(10, 10, 10, 10));
			// Setting up the button for the choices
			ToggleGroup choiceButton = new ToggleGroup();
			ArrayList<Choice> choiceList = quizQuestion.get(counter - 1).getChoiceList();

			// The Radio button for a single correct answer
			for (int i = 0; i < choiceList.size(); i++) {
				// Getting the Choice
				Choice tempChoice = choiceList.get(i);
				if (tempChoice.getChoiceValidity() == true) {
					this.correctChoice = tempChoice.getChoiceString();
				}
				// Creation of the radio button
				RadioButton tempRadButton = new RadioButton(tempChoice.getChoiceString());
				// We are making it so there is only a single answer that can be selected
				tempRadButton.setToggleGroup(choiceButton);
				choicePane.getChildren().add(tempRadButton);
			}
			// Checking for an image
			if (currentQuestion.getImage() != null) {
				ImageView tempImage = new ImageView(currentQuestion.getImage());
				// Setting the image max height and width
				tempImage.setFitHeight(250);
				tempImage.setFitWidth(250);
				// Adds it to the pane
				pane.setCenter(tempImage);
			}
			// Setting up the Right side of the screen question counter and feedback
			VBox right = new VBox(10);
			right.setPadding(new Insets(10, 10, 10, 10));
			right.getChildren().add(new Label("Total questions in quiz: " + quizQuestion.size()));
			right.getChildren().add(new Label("Current question: " + counter));
			right.setAlignment(Pos.BOTTOM_RIGHT);
			// Setting up alignment

			// Sets up submit button action
			submit.setOnAction(e -> {
				RadioButton tempRadio = (RadioButton) choiceButton.getSelectedToggle();
				if (tempRadio == null) {
					// Alert for no answer selected
					Alert alert = new Alert(AlertType.WARNING);
					alert.setContentText("Warning. No choice selected");
					alert.show();
				} else {
					// When answer is selected
					this.answered++;
					if (tempRadio.getText().equals(this.correctChoice)) {
						this.correct++;
						// Increments the number of correct
						right.getChildren().add(new Label("Correct Answer"));
					} else {
						right.getChildren().add(new Label("Wrong Answer"));
					}
					// This is removing submit from the button box
					buttonBox.getChildren().remove(1);
					// Adding the next button
					buttonBox.getChildren().add(nextButton);
				}
			});
			// Transition effect
			FadeTransition fade = new FadeTransition();
			fade.setDuration(Duration.millis(1000));
			fade.setFromValue(0);
			fade.setToValue(10);
			fade.setAutoReverse(true);
			fade.setNode(pane);
			fade.play();
			// Adding all the remaining buttons
			buttonBox.getChildren().addAll(exit, submit);
			// Set Exit to transition to main menu
			exit.setOnAction(e -> mainMenu(this.stage, this.mainPane));
			// Sets up scene with all the the buttons
			pane.setRight(right);
			pane.setLeft(choicePane);
			pane.setTop(questionAndChoiceBox);
			pane.setBottom(buttonBox);
			this.stage.setScene(scene);
		}
		// The case for the last Question
		if (counter == quizQuestion.size()) {
			// Setting up the new pane
			pane = new BorderPane();
			scene = new Scene(pane, 1000, 800);

			// Gets the label for the question
			Label questionLabel = new Label(counter + ".)  " + quizQuestion.get(counter - 1).getQuestion());
			questionLabel.setMaxHeight(100);
			HBox buttonBox = new HBox(10);
			// Creates a Vbox for left side of the screen
			VBox questionAndChoiceBox = new VBox(10);
			// This sets up the instuctions for the quiz screen
			questionAndChoiceBox.setPadding(new Insets(10, 10, 10, 10));
			questionAndChoiceBox.getChildren().add(new Label(
					"INSTRUCTIONS: Select a choice and press Submit to view the results of the question. \nOnce submitted your result will be displayed at the bottom right "));
			questionAndChoiceBox.getChildren().add(new Label("Final question. Press Finish Quiz to view results"));
			questionAndChoiceBox.getChildren().add(questionLabel);
			// Creation of the finish quiz button
			Button exit = new Button("Exit");
			Button finishQuiz = new Button("Finish Quiz");
			// Creating the Submit button
			Button submit = new Button("Submit Question");

			// We are setting the button box padding
			buttonBox.setPadding(new Insets(10, 10, 10, 10));
			int next = counter + 1;
			int back = counter - 1;

			// This is the selection of and displaying of the questions in the quiz.
			ToggleGroup choiceButton = new ToggleGroup();
			// String correctChoice = null;
			Question currentQuestion = quizQuestion.get(counter - 1);
			// Creation of the choice pane to be set to left when added
			VBox choicePane = new VBox(10);
			choicePane.setPadding(new Insets(10, 10, 10, 10));
			ArrayList<Choice> choiceList = currentQuestion.getChoiceList();
			// This is checking the current question has multiple choices are correct
			// The Radio button for a single correct answer
			for (int i = 0; i < choiceList.size(); i++) {
				// Getting the Choice
				Choice tempChoice = choiceList.get(i);
				if (tempChoice.getChoiceValidity() == true) {
					this.correctChoice = tempChoice.getChoiceString();
				}
				// Creation of the radio button
				RadioButton tempRadButton = new RadioButton(tempChoice.getChoiceString());
				tempRadButton.setToggleGroup(choiceButton);
				choicePane.getChildren().add(tempRadButton);
			}
			// Set Exit to transition to main menu
			exit.setOnAction(e -> mainMenu(this.stage, this.mainPane));
			if (currentQuestion.getImage() != null) {
				// Setting up of image if existent
				ImageView tempImage = new ImageView(currentQuestion.getImage());
				tempImage.setFitHeight(250);
				tempImage.setFitWidth(250);
				pane.setCenter(tempImage);
			}
			// Setting up the vbox for the right side of the screen with the info
			VBox right = new VBox(10);
			right.setPadding(new Insets(10, 10, 10, 10));
			right.getChildren().add(new Label("Total questions in quiz: " + quizQuestion.size()));
			right.getChildren().add(new Label("Current question: " + counter));
			right.setAlignment(Pos.BOTTOM_RIGHT);
			// Sets up choice button action
			submit.setOnAction(e -> {
				// Setting up the list of radio buttons for question
				RadioButton tempRadio = (RadioButton) choiceButton.getSelectedToggle();
				if (tempRadio == null) {
					// Checking if choice is selected states a warning
					Alert alert = new Alert(AlertType.WARNING);
					alert.setContentText("Warning. No choice selected");
					alert.show();
				} else {
					// Check adding correct answers when correct is selected
					this.answered++;
					if (tempRadio.getText().equals(this.correctChoice)) {
						this.correct++;
						// Updates the left side if answer is correct
						right.getChildren().add(new Label("Correct Answer"));
					} else {
						// Updates if the answer is incorrect
						right.getChildren().add(new Label("Wrong Answer"));
					}
					// add the finished quiz button
					buttonBox.getChildren().add(finishQuiz);
					// Removing next question when quiz is finished
					buttonBox.getChildren().remove(1);
				}
			});

			// Transition effect
			FadeTransition fade = new FadeTransition();
			fade.setDuration(Duration.millis(1000));
			fade.setFromValue(0);
			fade.setToValue(10);
			fade.setAutoReverse(true);
			fade.setNode(pane);
			fade.play();
			// Sets the action for the finish quiz to transition into the last scene
			finishQuiz.setOnAction(
					e -> checkResults(this.stage, this.mainPane, this.correct, this.answered, quizQuestion));
			// Adding all the Buttons to the HBox at the bottom of the screen
			buttonBox.getChildren().addAll(exit, submit);
			// Adding all the boxes and items to the scene
			pane.setLeft(choicePane);
			pane.setRight(right);
			pane.setTop(questionAndChoiceBox);
			pane.setBottom(buttonBox);
			// Setting the scene
			this.stage.setScene(scene);
		}

	}

	/**
	 * This is the last scene in the quiz and will transition user back to the and
	 * will transition user back to the main menu after showing results
	 * 
	 * @param primaryStage the stage being used overall
	 * @param pane         the ordering for the scene
	 * @param correct      the number of correct answers
	 * @param answered     the number that is answered
	 */
	private void checkResults(Stage primaryStage, BorderPane pane, int correct, int answered,
			ArrayList<Question> questionList) {
		// Creating new Scene and layout
		BorderPane pane1 = new BorderPane();
		Scene checkResultsScene = new Scene(pane1, 700, 700);
		// Creation of buttons
		Button returnToMenuButton = new Button("Return to Menu");
		VBox rightAns = new VBox();
		rightAns.getChildren().add(new Label("Correct choices for quiz questions. \n \n"));
		// Loops through the questions and adds them to a VBOX
		for (int i = 0; i < questionList.size(); i++) {
			try {
				Choice tempChoice = questionList.get(i).getCorrectChoice();
				// Question number is always index plus 1
				int quesNumb = i + 1;
				// This gets the correct answer for a given question and makes a label
				Label tempLabel = new Label(quesNumb + ".) " + tempChoice.getChoiceString());
				rightAns.getChildren().add(tempLabel);
			} catch (NoCorrectAnswerException e) {
				// Catches if there is no correct answer
				int quesNumb = i + 1;
				Label tempLabel = new Label(quesNumb + ".) No Correct Answer");
				rightAns.getChildren().add(tempLabel);
			}
		}
		// Transition effect
		FadeTransition fade = new FadeTransition();
		fade.setDuration(Duration.millis(700));
		fade.setFromValue(0);
		fade.setToValue(10);
		fade.setAutoReverse(true);
		fade.setNode(pane1);
		fade.play();
		returnToMenuButton.setOnAction(e -> mainMenu(this.stage, this.mainPane));
		// Label to be mean can be removed later
		VBox rightBox = new VBox();

		// Getting the correct number of answers
		Label numbCorrect = new Label("Number Correct: " + correct + "/" + questionList.size());
		// Getting the percent score
		double score = ((double) correct / (double) questionList.size()) * 100.0;
		// Setting the fun label up
		Label funLabel;
		if (score == 100.0) {
			funLabel = new Label("Your performance is AVERAGE");
			funLabel.setTextFill(Color.GREEN);
		} else {
			funLabel = new Label("Your performance is DISAPPOINTING!!");
			funLabel.setTextFill(Color.RED);
		}

		Label scoreLabel = new Label("Score: " + score + "%");
		// Adding the labels to the box.
		rightBox.getChildren().addAll(numbCorrect, scoreLabel, funLabel);
		// Now set padding on all the lists
		HBox buttonBox = new HBox();
		// Adding the button to a box first
		buttonBox.getChildren().add(returnToMenuButton);
		buttonBox.setPadding(new Insets(10, 10, 10, 10));
		rightBox.setPadding(new Insets(30, 10, 10, 0));
		rightAns.setPadding(new Insets(30, 0, 30, 10));

		// Adding everything to the pane
		pane1.setLeft(rightAns);
		pane1.setRight(rightBox);
		pane1.setBottom(buttonBox);
		Label scoreSummary = new Label("Score Summary");
		HBox scoreTop = new HBox();
		scoreTop.getChildren().add(scoreSummary);
		scoreTop.setPadding(new Insets(20, 100, 0, 100));
		pane1.setTop(scoreTop);
		stage.setScene(checkResultsScene);
	}

	/**
	 * This is the a helper method for setting the close action.
	 */
	private void promptSaveHelper() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Save your questions before leaving");
		alert.setHeaderText("Do you want to save all your questions before leaving?");
		alert.setContentText("Press OK to save your questions \n Hit Cancel to leave without saving");

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			TextInputDialog dialog = new TextInputDialog("fileName");
			dialog.setTitle("Saving your quiz");
			dialog.setHeaderText("Save your quiz to a json file\nHit cancel to leave without saving");
			dialog.setContentText("Enter the desired file name:");
			// dialog.show();
			Optional<String> result1 = dialog.showAndWait();

			result1.ifPresent(name -> {
				try {
					writer.output(".", name);
					Alert alert1 = new Alert(AlertType.CONFIRMATION);
					alert1.setContentText("File exported sucessfully to local project folder ");
					alert1.show();
					System.exit(0);

				} catch (IOException e2) {
					Alert alert1 = new Alert(AlertType.ERROR);
					alert1.setContentText("Warning: File could not be read. Please edit your file and try again.");
					alert1.show();
				}
			});

		} else {
		}
		System.exit(0);

	}

	public static void main(String[] args) {
		launch(args);
	}

}
