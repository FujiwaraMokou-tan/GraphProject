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
 *
 * @author PC
 */
public class Alerts extends Exception {

    public Alerts(String message, String title){
        super(message);
        display(message, title);
    }

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
