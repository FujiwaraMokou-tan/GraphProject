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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.Observer.Observable;
import pt.pa.Observer.Observer;
import pt.pa.graph.GraphController;
import pt.pa.graph.Vertex;
import pt.pa.model.GraphModel;
import pt.pa.model.Hub;
import pt.pa.model.Route;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GraphView implements Observer {
    private SmartGraphPanel<Hub, Route> graphView;
    private Stage stage = new Stage(StageStyle.DECORATED);
    private GraphModel model;
    private Button btAddRelationship;
    private Button btRemoveRelationship;
    private Button dijkstra;
    private Button undo;
    private Button save;
    private Button bfs;
    private ComboBox<String> cbVertexId0;
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

    @Override
    public void update(Observable subject, Object arg) {
        graphView.update();
        cbVertexId0.getItems().clear();
        cbVertexId1.getItems().clear();
        cbVertexId2.getItems().clear();
        cbVertexId3.getItems().clear();

        List<Vertex<Hub>> list = new ArrayList<>(model.getGraph().vertices());
        for (Vertex<Hub> vert: list) {
            cbVertexId0.getItems().add(String.valueOf(vert.element().getName()));
            cbVertexId1.getItems().add(String.valueOf(vert.element().getName()));
            cbVertexId2.getItems().add(String.valueOf(vert.element().getName()));
            cbVertexId3.getItems().add(String.valueOf(vert.element().getName()));
        }
    }

    public void designGraph(){
        String customProps = "edge.label = true" + "\n" + "edge.arrow = false";
        SmartGraphProperties properties = new SmartGraphProperties(customProps);
        graphView = new SmartGraphPanel<>(model.getGraph(), properties, new SmartCircularSortedPlacementStrategy());
        bPane.setCenter(new SmartGraphDemoContainer(graphView));
        bPane.setRight(createSidePanel());
        Scene scene = new Scene(bPane, 1480, 920);
        stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph pt.pa.model.City Distances");
        stage.setMinHeight(1020);
        stage.setMinWidth(1620);
        stage.setScene(scene);
        stage.show();
        graphView.init();
        for (Vertex<Hub> temp: model.getGraph().vertices()) {
           insertVertexPosition(temp);
        }
    }

    public void insertVertexPosition(Vertex<Hub> vert){
        graphView.setVertexPosition(vert, vert.element().getPosition().getX(), vert.element().getPosition().getY());
    }

    public void doDisplayAdjacency(){
        Stage window = new Stage();
        BorderPane root = new BorderPane();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Adjacency List:");
        window.setMinHeight(1000);
        window.setMinWidth(1000);
        ObservableList<Text> obsList = FXCollections.observableArrayList();
        ListView<Text> listView = new ListView<>();
        listView.setPrefSize(250, 920);
        listView.setStyle("-fx-control-inner-background: lightgrey;");
        listView.setItems(obsList);
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

    public void doBarChart(HashMap<Vertex<Hub>, Integer> map, int numEdges, int numVert) {
        Stage window = new Stage();
        BorderPane root = new BorderPane();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Adjacency List:");
        window.setMinHeight(1000);
        window.setMinWidth(1000);
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
        listView.setPrefSize(250, 920);
        listView.setStyle("-fx-control-inner-background: lightgrey;");
        listView.setItems(obsList);
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

    public void doDisplayBFS(String path){
        Stage window = new Stage();
        BorderPane root = new BorderPane();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Adjacency List:");
        window.setMinHeight(1000);
        window.setMinWidth(1000);
        ObservableList<Text> obsList = FXCollections.observableArrayList();
        ListView<Text> listView = new ListView<>();
        listView.setPrefSize(250, 920);
        listView.setStyle("-fx-control-inner-background: lightgrey;");
        listView.setItems(obsList);
        Text text = new Text("Vertexes found:\n\t" + path);
        text.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 14));
        obsList.add(text);
        root.setCenter(listView);
        Scene scene = new Scene(root, 800, 800);
        window.setScene(scene);
        window.showAndWait();
    }

    public void doDisplayDijkstra(double cost, String path){
        Stage window = new Stage();
        BorderPane root = new BorderPane();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Adjacency List:");
        window.setMinHeight(1000);
        window.setMinWidth(1000);
        ObservableList<Text> obsList = FXCollections.observableArrayList();
        ListView<Text> listView = new ListView<>();
        listView.setPrefSize(250, 920);
        listView.setStyle("-fx-control-inner-background: lightgrey;");
        listView.setItems(obsList);
        Text text = new Text("Cost: " + cost + "\nPath:\n\t" + path);
        text.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 14));
        obsList.add(text);
        root.setCenter(listView);
        Scene scene = new Scene(root, 800, 800);
        window.setScene(scene);
        window.showAndWait();
    }


    public void setTriggers(GraphController controller){
        chart.setOnAction(e -> controller.barChart());
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

    private VBox createSidePanel() {
        cbVertexId0 = new ComboBox<>();
        cbVertexId0.setMaxWidth(Double.MAX_VALUE);
        cbVertexId1 = new ComboBox<>();
        cbVertexId1.setMaxWidth(Double.MAX_VALUE);
        cbVertexId2 = new ComboBox<>();
        cbVertexId2.setMaxWidth(Double.MAX_VALUE);
        cbVertexId3 = new ComboBox<>();
        cbVertexId3.setMaxWidth(Double.MAX_VALUE);
        txtIteration = new TextField("");
        GridPane edgePane = new GridPane();
        txtEdgeDistance = new TextField("");
        btAddRelationship = new Button("Create");
        btRemoveRelationship = new Button("Remove");
        adjacency = new Button("Adjacency List");
        dijkstra = new Button("Dijkstra");
        dijkstra.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        //dijkstra.setMaxSize(120, 50);
        dijkstra.setFont(new Font(20));
        btRemoveRelationship.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        undo = new Button("Undo");
        //undo.setMinSize(200, 80);
        undo.setFont(new Font(20));
        undo.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        save = new Button("Save");
        //save.setMinSize(200, 80);
        save.setFont(new Font(20));
        save.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        edgePane.setAlignment(Pos.CENTER);
        edgePane.setHgap(5);
        edgePane.setVgap(5);
        edgePane.setPadding(new Insets(10,10,10,10));
        Label relationHub = new Label("Create/Delete Relationship:");
        relationHub.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 22));
        edgePane.add(relationHub, 0, 1);
        Label hub1 = new Label("Hub1:");
        hub1.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        edgePane.add(hub1, 0, 3);
        Label hub2 = new Label("Hub2:");
        hub2.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        edgePane.add(hub2, 0, 4);
        Label distance = new Label("Distance:");
        distance.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        edgePane.add(distance, 0, 5);
        edgePane.add(cbVertexId1, 1, 3);
        edgePane.add(cbVertexId2, 1, 4);
        edgePane.add(txtEdgeDistance, 1, 5);
        edgePane.add(btAddRelationship, 1, 6);
        edgePane.add(btRemoveRelationship, 2, 6);
        edgePane.add(undo, 1, 11);
        edgePane.add(save, 2, 11);
        edgePane.add(dijkstra, 1, 12);
        Label hubBfs = new Label("Hub for BFS:");
        hubBfs.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        bfs = new Button("BFS");
        dijkstra.setMinSize(200, 80);
        bfs.setFont(new Font(26));
        bfs.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        Label hub3 = new Label("Hub:");
        hub3.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        edgePane.add(hub3, 0, 17);
        edgePane.add(cbVertexId3, 1, 17);
        Label numIterations = new Label("Iterations:");
        numIterations.setFont(Font.font("Century Schoolbook", FontWeight.BOLD, 16));
        edgePane.add(numIterations,0,18);
        edgePane.add(txtIteration,1,18);
        edgePane.add(bfs,1,20);
        adjacency.setMinSize(200, 80);
        adjacency.setFont(new Font(26));
        edgePane.add(adjacency, 1,21);
        chart = new Button("Bar Chart");
        chart.setMinSize(200, 80);
        chart.setFont(new Font(26));
        edgePane.add(chart, 1,22);
        panel.getChildren().add(edgePane);
        return panel;
    }

    public String getDistance(){return txtEdgeDistance.getText();}
    public String getIteration(){return txtIteration.getText();}
    public String getRelationshipFirst(){return cbVertexId1.getSelectionModel().getSelectedItem(); }
    public String getRelationshipSecond(){return cbVertexId2.getSelectionModel().getSelectedItem(); }
    public String getBfsVert(){return cbVertexId3.getSelectionModel().getSelectedItem(); }
}
