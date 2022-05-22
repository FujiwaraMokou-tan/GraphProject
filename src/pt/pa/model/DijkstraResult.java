package pt.pa.model;

import pt.pa.graph.Vertex;

import java.util.List;

/**
 * This class's sole purpose is to store the result of the Dijkstra algorithm (Dijkstra class)
 * @param <V>This param will be considered a Hub
 */
public class DijkstraResult<V> {

    double cost;
    List<Vertex<V>> path;

    /**
     *The constructor for our class whose onjective is to assign the cost and the path after the Dijkstra class as done its algorithm
     * @param cost the total cost of the path (sum of all routes)
     * @param path The path of Vertexes we must follow to reach the destination
     */
    public DijkstraResult(double cost, List<Vertex<V>> path) {
        this.cost = cost;
        this.path = path;
    }

    /**
     *This method returns the cost of our path when going from point origin to destination
     * @return returns the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     *This method returns the list of vertexes that take to go from point origin to destination
     * @return returns our path/list of vertexes
     */
    public List<Vertex<V>> getPath() {
        return path;
    }
}
