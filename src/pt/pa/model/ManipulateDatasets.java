package pt.pa.model;


import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.GraphController;
import pt.pa.graph.Vertex;
import pt.pa.graphicInterface.Alerts;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ManipulateDatasets {
    private final String commonPath = "dataset/";
    private boolean receivedDataset = false;
    private LogisticNetwork logisticNetwork;

    /**
     * This method will be responsable for preparing the user input and coordinate other methods into being able to
     * read the dataset and create a graph
     * @param userInput this paramether contains what the user types in order to obtain the correct dataset and type of graph we want
     * @param stage The stage we are using to obtain the early user input, if we successfully load a dataset then this stage will be closed
     * @throws Alerts If something goes wetong with one of the auxiliary methods this method calls then it will return an error as a popup to the user explaining
     * what happenned depending on which method it triggered
     */
    public void readInput(TextField userInput, Stage stage) throws Alerts {
        String input = userInput.getText();
        String[] split = input.split("\\s+");
        if (!receivedDataset) {
            obtainDataset(split);
            GraphController controller = new GraphController(logisticNetwork);
            receivedDataset = true;
            controller.addButtons();
            stage.close();
        }
    }

    /**
     * This method servers to Obtain the right dataset the user chose
     * @param split The splitted input of the User so we can use each part where its needed
     * @throws Alerts in case it fails to find a dataset
     */
    public void obtainDataset(String[] split) throws Alerts {
        DatasetReader readFiles = new DatasetReader();
        String type = TypeOfGraph(split);
        if (split[0].substring(0, 3).equalsIgnoreCase("sgb")) {
            String[] names = readFiles.readNameFile(commonPath + split[0], Integer.parseInt(split[0].substring(3)));
            int[] weights = readFiles.readWeightFile(commonPath + split[0], Integer.parseInt(split[0].substring(3)));
            logisticNetwork = new LogisticNetwork(Integer.parseInt(split[0].substring(3)));
            Positions[] positions = readFiles.readXY(commonPath + split[0], Integer.parseInt(split[0].substring(3)));
            int[][] routes = readFiles.readRoutesFile(commonPath + split[0] + "/" + split[1] + ".txt", Integer.parseInt(split[0].substring(3)));
            logisticNetwork.createNetwork(names, weights, positions, routes, split[1] + ".txt", type);
        } else
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
    }

    /**
     *This method serves to obtain the type of graph the user chose
     * @param split paramether containg all the inputs of the user properly seperated
     * @return returns the type of graph the user wants in string format
     */
    public String TypeOfGraph(String[] split) {
        if (split.length == 2)
            return "Adjacency";
        return split[2];
    }

    /**
     *This method serves to create a new route file so that we can update our dataset
     * @param network   our current logistic network, its needed so we can access our old matrix of routes
     * @param distances the current graph after being added or removed edges
     * @throws Alerts Throws an Alert popup displaying the error of whats happening(cant find file)
     */
    public void createRoute(LogisticNetwork network, Graph<Hub, Route> distances) throws Alerts {
        File file = new File("dataset/sgb" + distances.numVertices() + "/" + network.getRouteTxt());
        try {
            FileWriter writer = new FileWriter(file);
            int[][] routeArray = populateArray(network, distances);
            String textToFile = "";
            for (int y = 0; y < distances.numVertices(); y++) {
                for (int x = 0; x < distances.numVertices(); x++) {
                    if (x == distances.numVertices() - 1)
                        textToFile += routeArray[x][y] + "\n";
                    else
                        textToFile += routeArray[x][y] + " ";
                }
            }
            writer.write(textToFile);
            writer.close();
        } catch (IOException e) {
            throw new Alerts("Could not find File in the Dataset", "File Missing");
        }
    }

    /**
     * Populates a new matrix with the new values/ new edges that were changed while the app was running
     * @param network   our current logistic network, its needed so we can access our old matrix of routes
     * @param distances the current graph after being added or removed edges
     * @return returns the new updated Matrix
     */
    public int[][] populateArray(LogisticNetwork network, Graph<Hub, Route> distances) {
        int[][] routeArray = new int[distances.numVertices()][distances.numVertices()];
        ArrayList<Vertex<Hub>> vertexHubs = new ArrayList<>(distances.vertices());
        Map<Vertex<Hub>, Integer> pos = new HashMap<>();
        for (int y = 0; y < distances.numVertices(); y++) {
            for (Vertex<Hub> temp : vertexHubs) {
                if (temp.element().getName().equalsIgnoreCase(network.getHub(y).getName())) {
                    pos.put(temp, y);
                }
            }
        }
        for (Vertex<Hub> vert : distances.vertices()) {
            for (Edge<Route, Hub> edge : distances.incidentEdges(vert)) {
                routeArray[pos.get(vert)][pos.get(distances.opposite(vert, edge))] = edge.element().getRoute();
            }
        }
        return routeArray;
    }
}
