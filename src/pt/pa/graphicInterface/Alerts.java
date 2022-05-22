package pt.pa.graphicInterface;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is responsible into sending alert messaged to the user via pop-ups
 */
public class Alerts extends Exception {

    public Alerts(String message, String title){
        super(message);
        display(message, title);
    }

    /**
     *This method is responsible for creating a pop-up message to warn the user, this pop-up is completely
     * seperated from the main window so it can be closed without problems
     * @param message string variable containing the message to tell the user
     * @param title string variable containing the title that will be on top of the pop-up to tell the user
     */
    public static void display(String message, String title){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(450);
        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> window.close());
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, closeButton);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();
    }
}
