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
    LogisticNetwork network;

    public void PopulateGraph(LogisticNetwork logisticNetwork){
        model = new GraphModel(logisticNetwork.isAdjancencyOrEdge());
        caretaker = new Caretaker(model);
        view = new GraphView(model);
        network = logisticNetwork;
        model.createVertexesFromDataset(network);
        model.createEdgesFromDataset(network);
        view.designGraph();
    }

    public void addButtons(){
        view.setTriggers(this);
        this.model.addObserver(this.view);
        model.sendNotification();
    }

    public void barChart() {
        view.doBarChart(model.centrality(), model.numOfEdges(), model.numOfVertex());
    }

    public void displayAdjacency(){
        view.doDisplayAdjacency();
    }

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
            model.sendNotification();
        }catch (Exception e){
            throw new Alerts("The iteration must be a number", "Wrong Input");
        }

    }

    public void removeEdge() throws Alerts {
        String name1 = view.getRelationshipFirst();
        String name2 = view.getRelationshipSecond();
        if(name1== null || name2 == null)
            throw new Alerts("You must select the two necessary Locations.", "Wrong Input");
        caretaker.saveState();
        model.removeEdge(model.fetchVertex(name1), model.fetchVertex(name2));
        model.sendNotification();
    }

    public void undo() throws Alerts {
        caretaker.restoreState();
        model.sendNotification();
    }

    public void save() throws Alerts {
        ManipulateInputs inputs = new ManipulateInputs();
        inputs.createRoute(network, model.getDistances());
    }

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
