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


    /**
     *This method is responsible for warnign the user about how to type a desired dataset
     * @param root Borderpane which will show the available elements set at the Top of the screen
     * @param hbox wrapper which will have a textfield inside
     */
    public void addTopContents(BorderPane root, HBox hbox) {
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #00B7E1;");
        Text text = new Text("Choose your Dataset: (correct sgb + desired route ex: sgb128 routes_1)\n If you wish to load the Edgelist then do: sgb128 routes_1 e");
        text.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 20));
        hbox.getChildren().add(text);
        hbox.setAlignment(Pos.CENTER);
        root.setTop(hbox);
    }

    /**
     *This method simply shows a textfield at the bottom so the user can type which dataset he wishes to open
     * @param root Borderpane which will show the available elements set at the bottom of the screen
     * @param obtainInput class filereader which will be responsable for reading the inputs typed by the user
     * @param stage this stage represents the first window of the app where the user will be able to type the desired dataset
     */
    public void addBottomContents(BorderPane root, ManipulateDatasets obtainInput, Stage stage) {
        TextField tf = new TextField();
        Button btn = new Button("Enter");
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

