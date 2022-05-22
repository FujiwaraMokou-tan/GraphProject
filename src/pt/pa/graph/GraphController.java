package pt.pa.graph;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.pa.graphicInterface.Alerts;
import pt.pa.graphicInterface.GraphView;
import pt.pa.memento.Caretaker;
import pt.pa.model.*;

import java.io.IOException;
import java.util.List;

public class GraphController {
    private GraphModel model;
    private Caretaker caretaker;
    private GraphView view;
    private LogisticNetwork network;

    /**
     *The constructor of Controller class used in MVC pattern
     * @param logisticNetwork this paramether contains all the necessary data to create a graph
     */
    public GraphController(LogisticNetwork logisticNetwork){
        model = new GraphModel(logisticNetwork.isAdjancencyOrEdge());
        caretaker = new Caretaker(model);
        view = new GraphView(model);
        network = logisticNetwork;
        model.createVertexesFromDataset(network);
        model.createEdgesFromDataset(network);
        view.designGraph();
        this.model.addObserver(this.view);
    }

    /**
     * This method is responsible for setting all the triggers of the buttons which were added
     */
    public void addButtons(){
        view.setTriggers(this);
        model.notifyObservers(model);
    }

    /**
     * This method is responsible for showing all the metric calculations
     * Placing them all in a listview while also making a bar chart of the top 5 Hubs
     */
    public void metrics() {
        view.doMetrics(model.centrality(), model.numOfEdges(), model.numOfVertex());
    }

    /**
     * This method is responsible for displaying an adjacency list
     * everything is shown in a listview
     */
    public void displayAdjacency(){
        view.doDisplayAdjacency();
    }

    /**
     * This method is responsible in adding edges to our graph, calling upon the view to obtain the user inputs
     * and using them to create an edge in the model
     * @throws Alerts if the distance isn't a number or if the user hasn't selected 2 locations then the user will receive a pop-up
     */
    public void addEdge() throws Alerts {
        String name1 = view.getRelationshipFirst();
        String name2 = view.getRelationshipSecond();
        if(name1== null || name2 == null)
            throw new Alerts("You must select the two necessary Locations.", "Wrong Input");
        String distance = view.getDistance();
        try {
            int value = Integer.parseInt(distance);
            caretaker.saveState();
            model.createEdge(model.fetchVertex(name1), model.fetchVertex(name2), new Route(value));
            model.notifyObservers(model);
        }catch (Exception e){
            throw new Alerts("The distance must be a number", "Wrong Input");
        }

    }

    /**
     * This method is responsible in removing edges to our graph, calling ypon the view to obtain the user inputs
     * and using them to remove an edge in the model
     * @throws Alerts if the user hasnt selected 2 locations
     */
    public void removeEdge() throws Alerts {
        String name1 = view.getRelationshipFirst();
        String name2 = view.getRelationshipSecond();
        if(name1== null || name2 == null)
            throw new Alerts("You must select the two necessary Locations.", "Wrong Input");
        caretaker.saveState();
        model.removeEdge(model.fetchVertex(name1), model.fetchVertex(name2));
        model.notifyObservers(model);
    }

    /**
     * Undos an action, restoring into a previous state and notify the view that such change has occured
     * @throws Alerts This alert is thrown from the caretaker in case theres a failure in restoring a previous state
     */
    public void undo() throws Alerts {
        caretaker.restoreState();
        model.notifyObservers(model);
    }

    /**
     * This method will be used to save/overwrite the new route file with the current changes we applied to our graph
     * @throws Alerts in case one of the auxiliary methods triggers an exception then a pop-ip will show
     */
    public void save() throws Alerts {
        new ManipulateDatasets().createRoute(network, model.getGraph());
    }

    /**
     * This method is responsible in managing the dijkstra algorithm, it obtains the input of the user
     * and tries to apply the algorithm to both locations, finding then a result and displaying it in a listview
     * @throws Alerts If two locations haven't been chosen then an exception wil be triggered and a pop up will be displayed to the user
     */
    public void dijkstra() throws Alerts {
        String name1 = view.getRelationshipFirst();
        String name2 = view.getRelationshipSecond();
        if(name1== null || name2 == null)
            throw new Alerts("You must select the two necessary Locations.", "Wrong Input");
        DijkstraResult<Hub> result = model.dijkstra(model.fetchVertex(name1), model.fetchVertex(name2));
        String path ="";
        for (Vertex<Hub> vert:result.getPath()) {
            path+= " -> {" + vert.element().getName()+"}";
        }
        view.doDisplayDijkstra(result.getCost(), path);
    }

    /**
     * This method is responsible into managing the BFS algorithm, obtaining the input of the user
     * and try to apply it into the logical part of our MVC pattern (Model)
     * @throws Alerts if one of the inputs the user submitted is incorrect then a pop up will be displayed to the user
     */
    public void applyBFS() throws Alerts {
        String name = view.getBfsVert();
        String iteration = view.getIteration();
        if(iteration.isEmpty() || name==null)
            throw new Alerts("You must select a location and a number of iterations.", "Wrong Input");
        try{
            int value = Integer.parseInt(iteration);
            List<Vertex<Hub>> list = model.applyBFS(model.fetchVertex(name), value);
            String path ="";
            for (Vertex<Hub> vert:list) {
                path+= "  {" + vert.element().getName()+"}  ";
            }
            view.doDisplayBFS(path);
        }catch (Exception e){
            throw new Alerts("The iteration must be a number", "Wrong Input");
        }

    }
}
