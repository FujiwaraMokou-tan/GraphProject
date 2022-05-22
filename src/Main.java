
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.graphicInterface.GraphicalUI;
import pt.pa.model.ManipulateDatasets;

public class Main extends Application {
    private BorderPane root = new BorderPane();
    private GraphicalUI graphics = new GraphicalUI();
    private ManipulateDatasets obtainInput = new ManipulateDatasets();
    private HBox hbox = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        graphics.addTopContents(root, hbox);
        Scene scene = new Scene(root, 1400, 908);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Projeto PA - Logistics Network");
        stage.setMinHeight(768);
        stage.setMinWidth(1200);
        stage.setScene(scene);
        stage.show();
        graphics.addBottomContents(root, obtainInput, stage);
    }
}
