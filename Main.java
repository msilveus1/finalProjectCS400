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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Main extends Application {

	Scene exportScene;
	Scene readScene;
	Scene mainMenu;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 700, 700, Color.BLACK);
			this.mainMenu = scene;
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
		
			mainMenu(primaryStage, root);
			
			primaryStage.setTitle("Quiz Generator");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mainMenu(Stage primaryStage, BorderPane root) {
		
		HBox box = new HBox();
		VBox vertical = new VBox();
		

		Button exitButton = new Button("Exit");
		exitButton.setMaxWidth(150);
		exitButton.setOnAction(e -> System.exit(0));

		Button exportButton = new Button("Export File");
		exportButton.setMaxWidth(150);

		Button readButton = new Button("Read File");
		readButton.setMaxWidth(150);
		box.getChildren().addAll(exitButton, readButton, exportButton);
		readButton.setOnAction(e -> readFileScene(primaryStage, root));
		root.setTop(box);

		
		ListView<String> topics = new ListView<String>();
		ObservableList<String> topic = FXCollections.observableArrayList();
		topic.add("CS 400");
		topic.add("CS 252");
		topic.add("CS 354");

		topics.setItems(topic);
		root.setLeft(topics);
		root.setMargin(topics, new Insets(50, 30, 30, 30));
		root.setLeft(topics);
		
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

	public static void main(String[] args) {
		launch(args);
	}

}
