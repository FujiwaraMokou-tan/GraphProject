package pt.pa.model;

import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.Vertex;

import java.util.ArrayList;
import java.util.List;



/**
 * This class serves to implement the BFS algorithm
 */
public class BreathFirstSearch {

    /**
     * Performs the BFS algorithm search but it has a limited counter on it (each counter means 1 level of depth)
     * @param g the graph that we want to iterate on
     * @param v the vertex that is our starting node
     * @param counter the number of iterations/levels
     * @return return the list of vertexes that were found in the iterations/levels
     */
    public List<Vertex<Hub>> BFS(Graph<Hub, Route> g, Vertex<Hub> v, int counter){
        List<Vertex<Hub>> nodesToVisit = new ArrayList<>();
        nodesToVisit.add(v);
        while(counter > 0){
            nodesToVisit = descendants(g, nodesToVisit);
            counter--;
        }
        return nodesToVisit;
    }

    /**
     *This method is an auxiliary method with the purpose of finding all the direct descendants of the nodes we have to visit (basically go deeper 1 level)
     * @param g the graph that we want to iterate on
     * @param nodesToVisit List of vertexes to expand upon
     * @return returns updated list
     */
    public List<Vertex<Hub>> descendants(Graph<Hub, Route> g, List<Vertex<Hub>> nodesToVisit ){
        List<Vertex<Hub>> newNodesToVisit = new ArrayList<>(nodesToVisit);
        for (int x = 0; x < nodesToVisit.size() ;x++){
            for (Edge<Route, Hub> edge: g.incidentEdges(nodesToVisit.get(x))) {
                if(!nodesToVisit.contains(edge.vertices()[0]) && !newNodesToVisit.contains(edge.vertices()[0]))
                    newNodesToVisit.add(edge.vertices()[0]);
                if(!nodesToVisit.contains(edge.vertices()[1]) && !newNodesToVisit.contains(edge.vertices()[1]))
                    newNodesToVisit.add(edge.vertices()[1]);
            }
        }
        return newNodesToVisit;
    }
}
