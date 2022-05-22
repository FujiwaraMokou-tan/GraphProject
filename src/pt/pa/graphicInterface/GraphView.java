package pt.pa.graphicInterface;
import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.Observer.Observer;
import pt.pa.graph.GraphController;
import pt.pa.graph.Vertex;
import pt.pa.model.GraphModel;
import pt.pa.model.Hub;
import pt.pa.model.Route;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GraphView implements Observer {
    private SmartGraphPanel<Hub, Route> graphView;
    private GraphModel  model;
    private Button btAddRelationship;
    private Button btRemoveRelationship;
    private Button dijkstra;
    private Button undo;
    private Button save;
    private Button bfs;
    private Label relationHub;
    private Label hub1;
    private Label hub2;
    private Label distance;
    private Label hubBfs;
    private Label hub3;
    private Label numIterations;
    private HBox label1PlusCbVertexId1;
    private HBox hboxCreateAndRemove;
    private HBox label1PlushCbVertexId2;
    private HBox hboxUndoAndSave;
    private HBox distancePlusTextField;
    private HBox hboxDijkstra;
    private HBox hboxHub3AndCbVertexId3;
    private HBox hboxIterations;
    private HBox hboxAdjacencyBFSChart;
    private ComboBox<String> cbVertexId1;
    private ComboBox<String> cbVertexId2;
    private ComboBox<String> cbVertexId3;
    private TextField txtIteration;
    private BorderPane bPane = new BorderPane();
    private VBox panel = new VBox();
    private TextField txtEdgeDistance;
    private Button adjacency;
    private Button chart;


    public GraphView(GraphModel model){
        this.model = model;
    }

    /**
     * This method is responsible for updating our view, receiving the changes from the model(subject class which is extended by the model) and updating them
     */
    @Override
    public void update() {
        graphView.update();
        cbVertexId1.getItems().clear();
        cbVertexId2.getItems().clear();
        cbVertexId3.getItems().clear();

        List<Vertex<Hub>> list = new ArrayList<>(model.getGraph().vertices());
        for (Vertex<Hub> vert: list) {
            cbVertexId1.getItems().add(String.valueOf(vert.element().getName()));
            cbVertexId2.getItems().add(String.valueOf(vert.element().getName()));
            cbVertexId3.getItems().add(String.valueOf(vert.element().getName()));
        }
    }

    /**
     * This method is responsible for designing our main window, implementing the graph into the SmartGraphPanel as well as set an appropriate size for the window
     */
    public void designGraph(){
        String customProps = "edge.label = true" + "\n" + "edge.arrow = false";
        SmartGraphProperties properties = new SmartGraphProperties(customProps);
        graphView = new SmartGraphPanel<>(model.getGraph(), properties, new SmartCircularSortedPlacementStrategy());
        bPane.setCenter(new SmartGraphDemoContainer(graphView));
        bPane.setRight(createSidePanel());
        Scene scene = new Scene(bPane, 1480, 920);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph pt.pa.model.City Distances");
        stage.setMinHeight(1020);
        stage.setMinWidth(1620);
        stage.setScene(scene);
        stage.show();
        graphView.init();
        for (Vertex<Hub> vert: model.getGraph().vertices()) {
            graphView.setVertexPosition(vert, vert.element().getPosition().getX(), vert.element().getPosition().getY());
        }
    }

    /**
     * Method responsible for setting our secondary window which is designed to show the data/information to the user
     * @param window the Stage of our secondary window
     * @param title The Title of our secondary window
     */
    public void secondaryWindow(Stage window, String title){
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(1000);
        window.setMinWidth(1000);
    }

    /**
     * Method responsible for setting our listview which is designed to show the data/information to the user in text format
     * @param listView the list view that will be displayed
     * @param obsList our observable list where the text will be added before its set on the listview
     */
    public void setUpListview(ListView<Text> listView ,ObservableList<Text> obsList){
        listView.setPrefSize(250, 920);
        listView.setStyle("-fx-control-inner-background: lightgrey;");
        listView.setItems(obsList);
    }

    /**
     * This method is responsible for displaying our adjacency list, showing all our vertexes and edges
     */
    public void doDisplayAdjacency(){
        Stage window = new Stage();
        secondaryWindow(window, "Adjacency List:");
        BorderPane root = new BorderPane();
        ObservableList<Text> obsList = FXCollections.observableArrayList();
        ListView<Text> listView = new ListView<>();
        setUpListview(listView, obsList);
        String lines[] = model.getGraph().toString().split("\\r?\\n");
        Text[] text = new Text[lines.length];
        for(int i = 0; i < lines.length; i++){
            text[i] = new Text("");
            text[i].setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 14));
            text[i].setText(lines[i]);
            obsList.add(text[i]);
        }
        obsList.add(text[text.length-1]);
        root.setCenter(listView);
        Scene scene = new Scene(root, 800, 800);
        window.setScene(scene);
        window.showAndWait();
    }


    /**
     * This method is responsible for doing all the required metric for our project
     * @param map our map with the vertexes and it's edges fully organized in decrescent order
     * @param numEdges the total number of edges
     * @param numVert the total number of vertexes
     */
    public void doMetrics(HashMap<Vertex<Hub>, Integer> map, int numEdges, int numVert) {
        Stage window = new Stage();
        secondaryWindow(window, "Metrics:");
        BorderPane root = new BorderPane();
        VBox center = new VBox();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Locations");
        yAxis.setLabel("Value");
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Top 5 Hubs");
        int counter = 0;
        for (Vertex<Hub> vert: map.keySet()) {
            if(counter == 5)
                break;
            series1.getData().add(new XYChart.Data(vert.element().getName(), map.get(vert)));
            counter++;
        }

        ObservableList<Text> obsList = FXCollections.observableArrayList();
        ListView<Text> listView = new ListView<>();
        setUpListview(listView, obsList);
        Text text = null;
        for (Vertex<Hub> vert: map.keySet()) {
            text = new Text("");
            text.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 14));
            text.setText(vert.element().getName() + "and the number of edges is: " + map.get(vert));
            obsList.add(text);
        }
        text.setText("The total number of edges is: " + numEdges + "\nThe total number of Vertexes is:" + numVert +
                "\nThe total number of subgraphs is: " + model.numberOfSubGraphs());
        obsList.add(text);
        center.getChildren().add(listView);
        center.getChildren().add(bc);
        bc.getData().addAll(series1);
        root.setCenter(center);
        Scene scene = new Scene(root, 800, 800);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * This method is responsible for displaying the Result of running the BFS algorithm, showing all the vertexes till a certain lvl chosen by the user
     * @param path the name of all vertexes/hubs will a certain depth
     */
    public void doDisplayBFS(String path){
        Stage window = new Stage();
        secondaryWindow(window, "BFS:");
        BorderPane root = new BorderPane();
        ObservableList<Text> obsList = FXCollections.observableArrayList();
        ListView<Text> listView = new ListView<>();
        setUpListview(listView, obsList);
        Text text = new Text("Vertexes found:\n\t" + path);
        text.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 14));
        obsList.add(text);
        root.setCenter(listView);
        Scene scene = new Scene(root, 800, 800);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     *This method is responsible for displaying the Result of running the Djkstra's algorithm, showing the minimum cost and the correct path
     * @param cost the cost going from origin vertex to destination
     * @param path contains all the vertexes needed to reach from one point to another
     */
    public void doDisplayDijkstra(double cost, String path){
        Stage window = new Stage();
        secondaryWindow(window, "Dijkstra:");
        BorderPane root = new BorderPane();
        ObservableList<Text> obsList = FXCollections.observableArrayList();
        ListView<Text> listView = new ListView<>();
        setUpListview(listView, obsList);
        Text text = new Text("Cost: " + cost + "\nPath:\n\t" + path);
        text.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 14));
        obsList.add(text);
        root.setCenter(listView);
        Scene scene = new Scene(root, 800, 800);
        window.setScene(scene);
        window.showAndWait();
    }


    /**
     *This method will be responsible for appointing what each task the controller must order/do upon a certain button being pressed
     * The controller will then obtain all the information from the user and use it with the logic data from the model to generate what is asked
     * @param controller The controller of out MVC pattern, who will be responsible in exchanging interactions between the view and the model
     */
    public void setTriggers(GraphController controller){
        chart.setOnAction(e -> controller.metrics());
        btAddRelationship.setOnAction(e -> {
            try {
                controller.addEdge();
            } catch (Alerts alerts) {
                alerts.printStackTrace();
            }
        });
        btRemoveRelationship.setOnAction(e -> {
            try {
                controller.removeEdge();
            } catch (Alerts alerts) {
                alerts.printStackTrace();
            }
        });
        undo.setOnAction(e -> {
            try {
                controller.undo();
            } catch (Alerts alerts) {
                alerts.printStackTrace();
            }
        });
        save.setOnAction(e -> {
            try {
                controller.save();
            } catch (Alerts ioException) {
                ioException.printStackTrace();
            }
        });
        dijkstra.setOnAction(e-> {
            try {
                controller.dijkstra();
            } catch (Alerts alerts) {
                alerts.printStackTrace();
            }
        });
        bfs.setOnAction(e-> {
            try {
                controller.applyBFS();
            } catch (Alerts alerts) {
                alerts.printStackTrace();
            }
        });
        adjacency.setOnAction(e -> controller.displayAdjacency());
    }

    /**
     * This method is focused on instancing all the comboBoxes
     */
    private void initiateComboBoxes(){
        cbVertexId1 = new ComboBox<>();
        cbVertexId1.setMaxWidth(Double.MAX_VALUE);
        cbVertexId2 = new ComboBox<>();
        cbVertexId2.setMaxWidth(Double.MAX_VALUE);
        cbVertexId3 = new ComboBox<>();
        cbVertexId3.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * This method is focused on instancing all the labels
     */
    private void initiateLabels(){
        relationHub = new Label("Create/Delete Relationship:");
        relationHub.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 22));
        hub1 = new Label("Hub1:");
        hub1.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        hub2 = new Label("Hub2:");
        hub2.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        distance = new Label("Distance:");
        distance.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        hubBfs = new Label("Hub for BFS:");
        hubBfs.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        hub3 = new Label("Hub:");
        hub3.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        txtIteration = new TextField("");
        txtEdgeDistance = new TextField("");
        numIterations = new Label("Iterations:");
        numIterations.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
    }

    /**
     * This method is focused on instancing all the buttons
     */
    private void initiateButtons(){
        btAddRelationship = new Button("Create");
        btAddRelationship.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
        btRemoveRelationship = new Button("Remove");
        btRemoveRelationship.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        adjacency = new Button("Adjacency List");
        adjacency.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
        undo = new Button("Undo");
        undo.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
        save = new Button("Save");
        save.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
        dijkstra = new Button("Dijkstra");
        dijkstra.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        bfs = new Button("BFS");
        bfs.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        chart = new Button("Metrics");
        chart.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
    }

    /**
     * This method is focused on instancing all the Hboxes and place the other elements(labels, textfields...) inside them
     */
    private void initiateHBoxes(){
        label1PlusCbVertexId1 = new HBox();
        label1PlusCbVertexId1.setSpacing(40);
        label1PlusCbVertexId1.getChildren().addAll(hub1, cbVertexId1);
        hboxCreateAndRemove = new HBox();
        hboxCreateAndRemove.setPadding(new Insets(7, 7, 7, 0));
        hboxCreateAndRemove.setSpacing(20);
        hboxCreateAndRemove.getChildren().addAll(btAddRelationship, btRemoveRelationship);
        hboxCreateAndRemove.setPadding(new Insets(0, 0, 0, 95));
        label1PlushCbVertexId2 = new HBox();
        label1PlushCbVertexId2.setSpacing(40);
        label1PlushCbVertexId2.getChildren().addAll(hub2, cbVertexId2);
        hboxUndoAndSave = new HBox();
        hboxUndoAndSave.setSpacing(20);
        hboxUndoAndSave.getChildren().addAll(undo, save);
        hboxUndoAndSave.setPadding(new Insets(0, 0, 0, 95));
        distancePlusTextField = new HBox();
        distancePlusTextField.setSpacing(13);
        distancePlusTextField.getChildren().addAll(distance, txtEdgeDistance);
        hboxDijkstra = new HBox();
        hboxDijkstra.getChildren().addAll(dijkstra);
        hboxDijkstra.setPadding(new Insets(0, 0, 0, 130));
        hboxHub3AndCbVertexId3 = new HBox();
        hboxHub3AndCbVertexId3.setSpacing(20);
        hboxHub3AndCbVertexId3.getChildren().addAll(hub3, cbVertexId3);
        hboxHub3AndCbVertexId3.setPadding(new Insets(0, 0, 0, 48));
        hboxIterations = new HBox();
        hboxIterations.setSpacing(20);
        hboxIterations.getChildren().addAll(numIterations, txtIteration);
        hboxAdjacencyBFSChart = new HBox();
        hboxAdjacencyBFSChart.setSpacing(20);
        hboxAdjacencyBFSChart.setPadding(new Insets(0, 20, 0, 48));
        hboxAdjacencyBFSChart.getChildren().addAll(bfs, adjacency, chart);
    }

    /**
     * This method serves to create all the interactables and its labels so that the user can interact with the application
     * which will then be set to the right of out graphPanel view
     * @return returns a Vbox with all the elements inside
     */
    private VBox createSidePanel() {
        initiateComboBoxes();
        initiateLabels();
        initiateButtons();
        initiateHBoxes();
        GridPane edgePane = new GridPane();
        edgePane.setAlignment(Pos.CENTER);
        edgePane.setHgap(1);
        edgePane.setVgap(5);
        edgePane.add(relationHub, 0, 1);
        edgePane.add(label1PlusCbVertexId1, 0, 3);
        edgePane.add(label1PlushCbVertexId2, 0, 4);
        edgePane.add(distancePlusTextField, 0, 5);
        edgePane.add(hboxCreateAndRemove, 0, 6);
        edgePane.add(hboxUndoAndSave, 0, 11);
        edgePane.add(hboxDijkstra, 0, 12);
        edgePane.add(hboxHub3AndCbVertexId3, 0, 17);
        edgePane.add(hboxIterations,0,18);
        edgePane.add(hboxAdjacencyBFSChart,0,20);
        panel.getChildren().add(edgePane);
        return panel;
    }

    /**
     * This method is responsible for obtaining the input of the user regarding the distance
     * @return returns a String of the received user Input
     */
    public String getDistance(){return txtEdgeDistance.getText();}

    /**
     * This method is responsible for obtaining the input of the user regarding the number of iterations/levels for BFS
     * @return returns a String of the received user Input
     */
    public String getIteration(){return txtIteration.getText();}

    /**
     * This method is responsible for obtaining the input of the user regarding the one of the vertexes we wish to use on create/remove edge
     * @return returns a String of the received user Input
     */
    public String getRelationshipFirst(){return cbVertexId1.getSelectionModel().getSelectedItem(); }

    /**
     * This method is responsible for obtaining the input of the user regarding the one of the vertexes we wish to use on create/remove edge
     * @return returns a String of the received user Input
     */
    public String getRelationshipSecond(){return cbVertexId2.getSelectionModel().getSelectedItem(); }

    /**
     * This method is responsible for obtaining the input of the user regarding the vertex we wish to use on BFS algorithm
     * @return returns a String of the received user Input
     */
    public String getBfsVert(){return cbVertexId3.getSelectionModel().getSelectedItem(); }
}
