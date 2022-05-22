package pt.pa.model;

import pt.pa.Observer.Subject;
import pt.pa.graph.*;
import pt.pa.graphicInterface.Alerts;
import pt.pa.memento.Memento;
import pt.pa.memento.Originator;

import java.util.HashMap;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * The model of our MVC pattern, it contains all the logical data required and necessary
 */
public class GraphModel extends Subject implements Originator {
    Graph<Hub, Route> distances;

    /**
     * Constructor of our class it simply initializes our graph
     * @param type refers to the type of graph we want to create (Strategy pattern)
     */
    public GraphModel(boolean type){
        if(type)
            distances = new GraphAdjacencyList<>();
        else
            distances = new GraphEdgeList<>();
    }

    public Graph<Hub, Route> getGraph(){
        return distances;
    }

    /**
     *This method inserts the data(related to the amount of vertexes) we obtained from reading our dataset into the distances graph
     * @param logisticNetwork contains all the necessary information to create the graph's vertexes
     */
    public void createVertexesFromDataset(LogisticNetwork logisticNetwork){
        for (Hub temp: logisticNetwork.getAllHubs()) {
            distances.insertVertex(temp);
        }
    }

    /**
     *This method inserts the data(related to the amount of Edges) we obtained from reading our dataset into the distances graph
     * @param logisticNetwork contains all the necessary information to create the graph's edges
     */
    public void createEdgesFromDataset(LogisticNetwork logisticNetwork){
        Route route;
        for(int y = 0; y < logisticNetwork.getSize(); y++){
            for(int x = 0; x < logisticNetwork.getSize(); x++){
                if(logisticNetwork.getRoute(x, y) != 0 && !existConnection(logisticNetwork.getHub(x), logisticNetwork.getHub(y), distances) ){
                    route = new Route (logisticNetwork.getRoute(y, x));
                    distances.insertEdge(logisticNetwork.getHub(x), logisticNetwork.getHub(y), route);
                }
            }
        }
    }

    /**
     *
     * @param sourceHub one of the hubs that will be used in the comparison to check if the edge already exists
     * @param destinationHub second hubs that will be used in the comparison to check if the edge already exists
     * @param distances graph of our dataset
     * @return a boolean value depending if the connection exists or not
     * @throws InvalidVertexException throws an Alert
     */
    private boolean existConnection(Hub sourceHub, Hub destinationHub, Graph<Hub, Route> distances) throws InvalidVertexException {
        Vertex<Hub> vertex1 = null;
        Vertex<Hub> vertex2 = null;
        try {
            for (Vertex<Hub> ver: distances.vertices()) {
                if(sourceHub.getName().equalsIgnoreCase(ver.element().getName()))
                    vertex1 = ver;
                if(destinationHub.getName().equalsIgnoreCase(ver.element().getName()))
                    vertex2 = ver;
            }
            return distances.areAdjacent(vertex1, vertex2);
        }catch (InvalidVertexException e){
            throw new InvalidVertexException("Vertex does not exist");
        }
    }

    /**
     *The method that returns how many vertexes we have
     * @return returns the number of edges
     */
    public int numOfEdges(){
        return distances.numEdges();
    }

    /**
     *The method that returns how many vertexes we have
     * @return returns the number of vertexes
     */
    public int numOfVertex(){
        return distances.numVertices();
    }

    /**
     * Orders the Map in a crescent order based of how many edges each vertex has
     * @return return a sorted Map from highest number of edges to lowest
     */
    public HashMap<Vertex<Hub>, Integer> centrality(){
        Map<Vertex<Hub>, Integer> unsortedList = new HashMap<>();
        for (Vertex<Hub> vert: distances.vertices()) {
            unsortedList.put(vert, distances.incidentEdges(vert).size());
        }
        List<Entry<Vertex<Hub>, Integer>> sortList = new LinkedList<>(unsortedList.entrySet());
        boolean order = true;
        sortList.sort((o2, o1) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().element().getName().compareTo(o2.getKey().element().getName())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().element().getName().compareTo(o1.getKey().element().getName())
                : o2.getValue().compareTo(o1.getValue()));
        return sortList.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    /**
     *Method to return the exact vertex in the graph that corresponds to the Hub
     * @param name Name of a Hub
     * @return return the vertex of said Hub
     */
    public Vertex<Hub> fetchVertex(String name){
        for (Vertex<Hub> vert: distances.vertices()) {
            if(vert.element().getName().equalsIgnoreCase(name)){
                return vert;
            }
        }
        return null;
    }

    /**
     *This method creates an edge by linking both vertexes together with a route
     * @param vert1 First vertex that will be used in the edge
     * @param vert2 Second vertex that will be used in the edge
     * @param distance The route which will contain the distance between each vertex
     */
    public void createEdge(Vertex<Hub> vert1, Vertex<Hub> vert2, Route distance){
        distances.insertEdge(vert1, vert2, distance);
    }

    /**
     * This method removes an edge by finding the edge the two vertexes are connected
     * @param vert1 First vertex that will be used in the edge
     * @param vert2 Second vertex that will be used in the edge
     */
    public void removeEdge(Vertex<Hub> vert1, Vertex<Hub> vert2){
        for (Edge<Route, Hub> edge: distances.edges()) {
            if(distances.incidentEdges(vert1).contains(edge) && distances.incidentEdges(vert2).contains(edge)) {
                distances.removeEdge(edge);
                break;
            }
        }
    }

    /**
     *
     * Method that returns a new Memento with the purpose to be stored on the Caretaker
     * @return returns the memento of the performed action
     */
    public Memento createMemento(){
        return new ModelMemento(distances);
    }

    /**
     *
     * @param memento receives a Memento and replaces the stored variable with it
     */
    public void setMemento(Memento memento){
        distances = ((ModelMemento)memento).distancesMem;
        }

    /**
     *This method serves to find how many sub-graphs are in a graph
     * @return returns the number of sub-graphs inside a graph
     */
    public int numberOfSubGraphs(){
        int counter=1;
        List<Vertex<Hub>> allNodes = new ArrayList<>(distances.vertices());
        Stack<Vertex<Hub>> visited = new Stack<>();
        List<Vertex<Hub>> nodesToVisit = new ArrayList<>();
        nodesToVisit.add(allNodes.get(0));
        while(visited.size() != distances.numVertices()){
            for (Edge<Route, Hub> edge: distances.incidentEdges(nodesToVisit.get(0))) {
                if(!visited.contains(edge.vertices()[0]) && !nodesToVisit.contains(edge.vertices()[0]))
                    nodesToVisit.add(edge.vertices()[0]);
                if(!visited.contains(edge.vertices()[1]) && !nodesToVisit.contains(edge.vertices()[1]))
                    nodesToVisit.add(edge.vertices()[1]);
            }
            visited.push(nodesToVisit.get(0));
            allNodes.remove(visited.peek());
            nodesToVisit.remove(0);
            if(nodesToVisit.isEmpty() && allNodes.size()!=0){
                counter++;
            nodesToVisit.add(allNodes.get(0));
            }
        }
        return counter;
    }

    /**
     *Method to obtain the result of the Dijkstra algorithm
     * @param hub1 The hub vertex that will be considered the origin of Dijkstra's algorithm
     * @param hub2 The hub vertex that will be considered the destination of Dijkstra's algorithm
     * @return returns the result after the algorithm has been implemented
     * @throws Alerts Throws an Alert in case something goes wrong in the following auxiliary methods
     */
    public DijkstraResult<Hub> dijkstra(Vertex<Hub> hub1, Vertex<Hub> hub2) throws Alerts {
        Dijkstra dijkstra = new Dijkstra();
        Graph<Hub, Route> newGraph = createGraph(hub1, hub2);
        for (Vertex<Hub> temp: newGraph.vertices()) {
            if(hub1.element().getName().equalsIgnoreCase(temp.element().getName())){
                hub1 = temp;
            }
            if(hub2.element().getName().equalsIgnoreCase(temp.element().getName())){
                hub2 = temp;
            }
        }
        return dijkstra.dijkstra(newGraph, hub1, hub2);
    }

    /**
     * This method will create a graph that is actually a subgraph(if there are more than 1 subgraph inside the graph) so that the
     * Dijkstra's algorithm code that was taught in class, this way the algorithm won't break down and every other subgraph outside is meaningless
     * since their value values will be infinite (impossible to reach), and they arent necessary for the tasks asked in this project
     * @param origin The hub vertex that will be considered the origin of Dijkstra's algorithm
     * @param dest The hub vertex that will be considered the destination of Dijkstra's algorithm
     * @return returns the graph that will be used on Dijkstra's algorithm
     * @throws Alerts Throws an Alert in case something goes wrong in the following auxiliary methods
     */
    public Graph<Hub, Route> createGraph(Vertex<Hub> origin, Vertex<Hub> dest) throws Alerts {
        List<Vertex<Hub>> visited = availableVertexesForDijkstra(origin, dest);
        Graph<Hub, Route> newGraph;
        if(distances instanceof GraphAdjacencyList)
            newGraph = new GraphAdjacencyList<>();
        else
            newGraph = new GraphEdgeList<>();
        newGraph = createMiniGraph(newGraph, visited);
        return newGraph;
    }

    /**
     *This method find all available methods for to be rin on Dijkstra
     * @param origin The hub vertex that will be considered the origin of Dijkstra's algorithm
     * @param dest The hub vertex that will be considered the destination of Dijkstra's algorithm
     * @return returns the list of all possible vertexes the algorithm can path through
     * @throws Alerts Throws an Alert in case something goes wrong
     */
    public List<Vertex<Hub>> availableVertexesForDijkstra(Vertex<Hub> origin, Vertex<Hub> dest) throws Alerts {
        List<Vertex<Hub>> nodesToVisit = new ArrayList<>();
        nodesToVisit.add(origin);
        List<Vertex<Hub>> visited = new ArrayList<>();
        while(nodesToVisit.size() != 0){
            for (Edge<Route, Hub> edge:distances.incidentEdges(nodesToVisit.get(0))) {
                if(!visited.contains(edge.vertices()[0]) && !nodesToVisit.contains(edge.vertices()[0]))
                    nodesToVisit.add(edge.vertices()[0]);
                if(!visited.contains(edge.vertices()[1]) && !nodesToVisit.contains(edge.vertices()[1]))
                    nodesToVisit.add(edge.vertices()[1]);
            }
            visited.add(nodesToVisit.get(0));
            nodesToVisit.remove(0);
        }
        if(!visited.contains(dest))
            throw new Alerts("These 2 Vertexes are impossible to meet", "No connection!");
        return visited;
    }

    /**
     *This method will create a graph where it ignores any vertex that would be value infinite in Dijkstra,
     * those vertexes can be neglected since they are impossible to ever be one of the solutions in determining the lowest cost path
     * @param newGraph the subgraph where all the vertexes are located
     * @param visited all the vertexes that can be visited
     * @return return a graph so that we can use it to apply the dijkstra algorithm
     */
    public Graph<Hub, Route> createMiniGraph(Graph<Hub, Route> newGraph, List<Vertex<Hub>> visited){
        List<Edge<Route, Hub>> edgeList= new ArrayList<>();
        for(int i = 0; i<visited.size(); i++){
            for (Edge<Route, Hub> edges: distances.incidentEdges(visited.get(i))) {
                if(!edgeList.contains(edges))
                    edgeList.add(edges);
            }
        }
        Set<Vertex<Hub>> set = new HashSet<>();
        for (Edge<Route, Hub> edges:edgeList) {
            if(!set.contains(edges.vertices()[0])){
                set.add(edges.vertices()[0]);
                newGraph.insertVertex(edges.vertices()[0].element());
            }
            if(!set.contains(edges.vertices()[1])){
                set.add(edges.vertices()[1]);
                newGraph.insertVertex(edges.vertices()[1].element());
            }
            newGraph.insertEdge(edges.vertices()[0].element(), edges.vertices()[1].element(), edges.element());
        }
        return newGraph;
    }

    /**
     *This method will be called whenever we want to apply the BFS algorithm to a certain vertex
     * @param vert vertex of origin
     * @param counter number of levels we will iterate our bfs
     * @return returns the result of the BFS application
     */
    public List<Vertex<Hub>> applyBFS(Vertex<Hub> vert, int counter){
        return new ArrayList<>(new BreathFirstSearch().BFS(distances, vert, counter));
    }

    /**
     * Private class necessary to implement the Memento pattern
     */
    private class ModelMemento implements Memento{
        public Graph<Hub, Route> distancesMem;

        public ModelMemento(Graph<Hub, Route> dist){
            if(dist instanceof GraphAdjacencyList)
                this.distancesMem = new GraphAdjacencyList<>();
            else
                this.distancesMem = new GraphEdgeList<>();
            for (Vertex<Hub> temp:dist.vertices()) {
                distancesMem.insertVertex(temp.element());
            }
            for (Edge<Route, Hub> temp2: dist.edges()) {
                if(!existConnection(temp2.vertices()[0].element(), temp2.vertices()[1].element(), distancesMem)){
                    distancesMem.insertEdge(temp2.vertices()[0].element() ,temp2.vertices()[1].element(), temp2.element() );
                }
            }
        }
    }
}
