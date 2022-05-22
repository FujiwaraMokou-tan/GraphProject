package pt.pa.model;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.GraphController;
import pt.pa.graph.Vertex;
import pt.pa.graphicInterface.Alerts;
import pt.pa.graphicInterface.GraphicalUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ManipulateInputs {
    private final String  commonPath = "dataset/";
    private boolean receivedDataset = false;
    private DatasetReader readFiles = new DatasetReader();
    private LogisticNetwork logisticNetwork;
    private String input;
    GraphController controller = new GraphController();

    public void readInput(TextField tf, Stage stage) throws Alerts {
        input = tf.getText();
        String[] split = input.split("\\s+");
        if(!receivedDataset) {
            obtainDataset(split);
            controller.PopulateGraph(logisticNetwork);
            receivedDataset = true;
            controller.addButtons();
            stage.close();
        }
    }

    /**
     *
     * @param split The splitted input of the User
     * @throws Alerts
     */
    public void obtainDataset(String[] split) throws Alerts{
        String type;
        if(split.length==2)
            type = "Adjacency";
        else
            type = split[2];
        if(split[0].substring(0, 3).equalsIgnoreCase("sgb")){
            String[] names = readFiles.readNameFile(commonPath + split[0], Integer.parseInt(split[0].substring(3)));
            int[] weights = readFiles.readWeightFile(commonPath + split[0], Integer.parseInt(split[0].substring(3)));
            logisticNetwork = new LogisticNetwork(Integer.parseInt(split[0].substring(3)));
            Positions[] positions = readFiles.readXY(commonPath + split[0], Integer.parseInt(split[0].substring(3)));
            int [][] routes = readFiles.readRoutesFile(commonPath + split[0] + "/" + split[1] + ".txt", Integer.parseInt(split[0].substring(3)));
            createLogisticNetwork(names, weights, positions,routes, split[1] + ".txt", type);
        }else
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
    }

    /**
     *
     * @param names Names of the Hubs
     * @param weights Weight of the Hubs
     * @param positions Their positions on the graph
     * @param routes The routes of each Hub
     * @param routeName The name of the route file.txt
     * @param type The type of Edge choosen to display the graph
     */
    public void createLogisticNetwork(String[] names, int[] weights, Positions[] positions, int [][] routes, String routeName, String type){
        logisticNetwork.createNetwork(names, weights, positions, routes, routeName, type);
    }

    /**
     *
     * @param network our current logistic network, its needed so we can access our old matrix of routes
     * @param distances the current graph after being added or removed edges
     * @throws Alerts Throws an Alert popup displaying the error of whats happening
     */
    public void createRoute(LogisticNetwork network ,Graph<Hub, Route> distances) throws Alerts {
        File file = new File("dataset/sgb" + distances.numVertices() + "/" + network.getRouteTxt());

        try{
        FileWriter writer = new FileWriter(file);
        int [][] routeArray = populateArray(network, distances);

        String textToFile = "";
        for (int y = 0; y<distances.numVertices(); y++){
            for (int x = 0; x<distances.numVertices(); x++){
                if(x==distances.numVertices()-1)
                    textToFile += routeArray[x][y];
                else
                    textToFile += routeArray[x][y] + " ";
            }
            textToFile += "\n";
        }
        writer.write(textToFile);
        writer.close();
        }catch (IOException e){
            throw new Alerts("Could not find File in the Dataset", "File Missing");
        }

    }

    /**
     * Populates a new matrix with the new values/ new edges that were changed while the app was running
     * @param network our current logistic network, its needed so we can access our old matrix of routes
     * @param distances the current graph after being added or removed edges
     * @return returns the new updated Matrix
     */
    public int[][] populateArray(LogisticNetwork network ,Graph<Hub, Route> distances){
        int [][] routeArray = new int[distances.numVertices()][distances.numVertices()];
        ArrayList<Vertex<Hub>> vertexHubs = new ArrayList<>(distances.vertices());
        Map<Vertex<Hub>, Integer> pos = new HashMap<>();
        for (int y = 0; y<distances.numVertices(); y++){
            for (Vertex<Hub> temp: vertexHubs) {
                if(temp.element().getName().equalsIgnoreCase(network.getHub(y).getName())){
                    pos.put(temp, y);
                }
            }
        }

        for (Vertex<Hub> vert:distances.vertices()) {
            for (Edge<Route, Hub> edge: distances.incidentEdges(vert)) {
                routeArray[pos.get(vert)][pos.get(distances.opposite(vert, edge))] = edge.element().getRoute();
            }
        }
        return routeArray;
    }
}
