package ru.sergst.aminoasidscore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Amimo asid score");
        primaryStage.setScene(new Scene(root, 954, 410));

        Controller controller = loader.getController();
        controller.setMain(this);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
