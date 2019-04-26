package application;


import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
public class Main extends Application {

	Scene exportScene;
	Scene readScene;
	Scene mainMenu;

	@Override
	/**
	*This is the method that starts our GUI 	
       	*/
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			//TODO:Check Specifications for the scene
			Scene scene = new Scene(root, 700, 700, Color.BLACK);
			this.mainMenu = scene;
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//This line may not be neccessary
		
			mainMenu(primaryStage, root);
			//Populating the main menu
			primaryStage.setTitle("Quiz Generator");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (Exception e) {
			//TODO: Check to see if we need deal with a finally block
			e.printStackTrace();
		}
	}

	public void mainMenu(Stage primaryStage, BorderPane root) {
		
		HBox box = new HBox();
		VBox vertical = new VBox();
		
		//This will handle the exiting of the program 
		Button exitProgramButton = new Button("Exit");
		exitProgramButton.setMaxWidth(150);
		exitProgramButton.setOnAction(e -> System.exit(0));
		//Handles the switching to the export screen
		Button exportButton = new Button("Export File");
		exportButton.setMaxWidth(150);

		exportButton.setOnAction(e -> exportFileScene(primaryStage, root));
		//Sets up read in screen
		Button readButton = new Button("Read File");
		readButton.setMaxWidth(150);
		box.getChildren().addAll(exitProgramButton, readButton, exportButton);
		readButton.setOnAction(e -> readFileScene(primaryStage, root));
		root.setTop(box);
		TextField numQuestions = new TextField("# of Q's");
		Button addQuestion = new Button("Add Question");
		
		//Center Hortizonton box
		HBox center = new HBox();
		center.getChildren().addAll(new Label("Enter number of questions: "), numQuestions);
		root.setAlignment(center, Pos.BOTTOM_CENTER);
		root.setCenter(center);
		
		
		//TODO: Set action for button
		//addQuestion.setAction();
		box.getChildren().add(addQuestion);
		ListView<String> topics = new ListView<String>();
		ObservableList<String> topic = FXCollections.observableArrayList();
		topic.add("CS 400");
		topic.add("CS 252");
		topic.add("CS 354");
		//We added a new vbox to the left pane for the topics
		VBox vbox = new VBox();
		root.setLeft(vbox);
		topics.setItems(topic);
		Label label = new Label ("Topic List. Click to select topics");
		vbox.getChildren().addAll(label,topics);
		root.setMargin(vbox, new Insets(50, 30, 30, 30));
	
		
		Button startQuiz = new Button("Start Quiz ");
		startQuiz.setMaxWidth(150);
        root.setAlignment(startQuiz, Pos.CENTER);
        root.setBottom(startQuiz);
        
        primaryStage.setScene(this.mainMenu);

	}
	
	public void readFileScene(Stage primaryStage, BorderPane root) {
		BorderPane pane = new BorderPane();
		this.readScene = new Scene(pane, 500, 500);
		
	    Button button  = new Button ("Back");
	    button.setMaxWidth(150);
	    pane.setBottom(button);
	    button.setOnAction(e -> mainMenu(primaryStage, root));
	    
	    primaryStage.setScene(this.readScene);	
	}
	
	
	
	public void exportFileScene(Stage primaryStage, BorderPane root){
		BorderPane pane = new BorderPane();
		this.exportScene = new Scene(pane, 500, 500);
		Button exportButton = new Button("Export Quiz");
		exportButton.setMaxWidth(150);
		pane.setBottom(exportButton);
		exportButton.setOnAction(e -> mainMenu(primaryStage, root));
		primaryStage.setScene(this.exportScene);

	}

	public static void main(String[] args) {
		launch(args);
	}

}
