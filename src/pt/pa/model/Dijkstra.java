package pt.pa.model;

import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.GraphAdjacencyList;
import pt.pa.graph.Vertex;

import java.util.*;

public class Dijkstra {

    /**
     *
     * @param g The graph that we will be iterating on
     * @param origin The vertex of Origin
     * @param dest The vertex that is our destination
     * @return returns the result of out dijkstra algorithm containg the correct path and the cost
     */
    public DijkstraResult<Hub> dijkstra(Graph<Hub, Route> g,
                                               Vertex<Hub> origin,
                                               Vertex<Hub> dest) {

        Map<Vertex<Hub>, Double> minDist = new HashMap<>();
        Map<Vertex<Hub>, Vertex<Hub>> predecessors = new HashMap<>();
        List<Vertex<Hub>> unvisited = new ArrayList<>();
        for(Vertex<Hub> v : g.vertices()){
            unvisited.add(v);
            predecessors.put(v, null);
            minDist.put(v, Double.MAX_VALUE);
        }

        minDist.put(origin, 0.0);

        while(!unvisited.isEmpty()){
            Vertex<Hub> currentV = findMinCostVertex(minDist, unvisited);

            for (Edge<Route, Hub> e : g.incidentEdges(currentV)) {
                Vertex<Hub> oppositeV = g.opposite(currentV, e);
                if(unvisited.contains(oppositeV))
                {
                    double currentCost = minDist.get(currentV);
                    double edgeCost = e.element().getRoute();
                    double totalCost = currentCost + edgeCost;

                    if(totalCost < minDist.get(oppositeV))
                    {
                        minDist.put(oppositeV, totalCost);
                        predecessors.put(oppositeV, currentV);
                    }
                }
            }

            unvisited.remove(currentV);
        }
        Double cost = minDist.get(dest);
        if(cost == Double.MAX_VALUE)
        {
            return new DijkstraResult<>(cost, null);
        }

        List<Vertex<Hub>> path = new ArrayList<>();

        Vertex<Hub> current = dest;
        while(current != origin) {

            path.add(current);
            current = predecessors.get(current);
        }

        path.add(origin);

        Collections.reverse(path);

        return new DijkstraResult<>(cost, path);
    }


    /**
     *
     * @param distances Map of out distance/hubs
     * @param unvisited Unvisited vertexes
     * @return return the next vertex that possesses the minimum cost
     */
    public static Vertex<Hub> findMinCostVertex(Map<Vertex<Hub>, Double> distances,
                                                  List<Vertex<Hub>> unvisited)
    {
        Vertex<Hub> minCostVertex = null;
        double minCostValue = Double.MAX_VALUE;

        for(Vertex<Hub> v : unvisited)
        {
            Double distV = distances.get(v);
            if(distV < minCostValue)
            {
                minCostValue = distV;;
                minCostVertex = v;
            }
        }

        return minCostVertex;
    }

}
