package pt.pa.graphicInterface;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import pt.pa.model.*;
import java.io.FileNotFoundException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GraphicalUI {
    Button btn = new Button("Enter");

    /**
     *
     * @param content String value which will be displayed as the title/tooltip for the user
     * @param root Borderpane which will show the available elements set at the Top of the screen
     * @param hbox wrapper which will have a textfield inside
     */
    public void addTopContents(String content, BorderPane root, HBox hbox) {
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #00B7E1;");
        Text text = new Text(content);
        text.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 20));
        hbox.getChildren().add(text);
        hbox.setAlignment(Pos.CENTER);
        root.setTop(hbox);
    }

    /**
     *
     * @param root Borderpane which will show the available elements set at the bottom of the screen
     * @param obtainInput class filereader which will be responsable for reading the inputs typed by the user
     */
    public void addBottomContents(BorderPane root, ManipulateInputs obtainInput, Stage stage) {
        TextField tf = new TextField();
        VBox vbox = new VBox();
        tf.setMinSize(12, 10);
        btn.setPrefWidth(1400);
        btn.setPrefHeight(55);
        btn.setStyle("-fx-font-size: 2em; ");
        btn.setOnAction(e -> {
            try {
                obtainInput.readInput(tf, stage);
            } catch (Alerts fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        vbox.getChildren().add(tf);
        vbox.getChildren().add(btn);
        root.setBottom(vbox);
    }
}

