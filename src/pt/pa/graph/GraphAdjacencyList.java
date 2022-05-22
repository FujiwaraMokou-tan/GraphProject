package pt.pa.graph;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.*;

public class GraphAdjacencyList<V,E> implements Graph<V, E> {

    private Map<V, Vertex<V>> vertices;

    public GraphAdjacencyList() {
        this.vertices = new HashMap<>();
    }

    /**
     * This method receives two vertexes and verifies if both are adjacent
     * @param u the first vertex we receive to test the adjacency
     * @param v the second vertex we receive to test the adjacency
     *
     * @return returns a boolean value whether it is true or false
     * @throws InvalidVertexException throws an exception if one of the objects receive is not a vertex
     */
    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        MyVertex myU = checkVertex(u);
        MyVertex myV = checkVertex(v);
        Set<Edge<E,V>> intersection = new HashSet<>(myU.incidentEdges);
        intersection.retainAll(myV.incidentEdges);
        return !intersection.isEmpty();
    }

    /**
     * This method tries to find how many vertexes are in our map
     * @return returns the number of vertexes
     */
    @Override
    public int numVertices() {
        return vertices.size();
    }

    /**
     * This method tries to find how many edges are in our map of vertexes
     * @return returns the number of edges
     */
    @Override
    public int numEdges() {
        return edges().size();
    }

    /**
     * This method collects all the values of our map and returns it as a collection
     * @return returns a collection of vertexes
     */
    @Override
    public Collection<Vertex<V>> vertices() {
        return vertices.values();
    }

    /**
     * This method collects all the edges of the values of our map and returns it as a collection
     * @return returns a collection of edges
     */
    @Override
    public Collection<Edge<E, V>> edges() {
        Set<Edge<E,V>> edges = new HashSet<>();
        Iterator<Vertex<V>> it = vertices().iterator();
        while (it.hasNext())
        {
            edges.addAll(checkVertex(it.next()).incidentEdges);
        }
        return edges;
    }

    /**
     * This method is responsible for finding all the incident edges of a vertex
     * @param v     vertex for which to obtain the incident edges
     *
     * @return returns all the edges linked to that vertex
     * @throws InvalidVertexException if the vertex dos not exist then an exception is thrown
     */
    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {
        if (!this.vertices.containsValue(v))
            throw new InvalidVertexException("Vertex does not exist");

        return checkVertex(v).incidentEdges;
    }

    /**
     * This method is responsible for finding the opposite vertex of another vertex both connected by an edge
     * @param v         vertex on one end of <code>e</code>
     * @param e         edge connected to <code>v</code>
     * @return returns the opposite vertex
     * @throws InvalidVertexException if one of the vertexes is considered invalid then this exception will be triggered
     * @throws InvalidEdgeException if the edge is considered invalid (not an edge or not connected to the vertex) then this exception will be triggered
     */
    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        checkVertex(v);
        MyEdge edge = checkEdge(e);
        if (edge.vertices()[0] == v) {
            return edge.vertices()[1];
        } else {
            return edge.vertices()[0];
        }
    }

    /**
     *This method is responsible for placing a entry (Hub + vertex) into our map (graph)
     * @param vElement      the element to store at the vertex
     *
     * @return returns the vertex of the element tha was placed
     * @throws InvalidVertexException this exception is triggered when the element already exists or if it is null
     */
    @Override
    public Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if (vertices.containsKey(vElement))
            throw new InvalidVertexException("There's already a vertex with this element.");
        if (vElement == null)
            throw new InvalidVertexException("This element is null, cant be turned into a vertex.");
        MyVertex myV = new MyVertex(vElement);
        vertices.put(vElement, myV);
        return myV;
    }

    /**
     *This method is responsible for inserting an edge, finding the two vertexes it belongs and adding it to their list
     * @param u             a vertex
     * @param v             another vertex
     * @param edgeElement   the element to store in the new edge
     *
     * @return returns the edge after the element has been created successfully
     * @throws InvalidVertexException this exception is triggered when the vertex doesn't exists or if it is null
     * @throws InvalidEdgeException this exception is triggered when the element already exists or if it is null
     */
    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if (!vertices.containsKey(u.element()) || !vertices.containsKey(v.element()))
            throw new InvalidVertexException("One of the vertexes doesn't exist.");
        if (areAdjacent(u, v))
        {
            for (Edge<E, V> edge : incidentEdges(u))
            {
                if (opposite(u, edge) == v)
                {
                    throw new InvalidEdgeException("Vertices already have an adjacent edge with the same value");
                }
            }
        }
        MyEdge newEdge = new MyEdge(edgeElement);
        checkVertex(u).incidentEdges.add(newEdge);
        checkVertex(v).incidentEdges.add(newEdge);
        return newEdge;
    }

    /**
     *This method is responsible for inserting an edge, finding the two vertexes via the elements it belongs and adding it to their list
     * @param vElement1     a vertex's stored element
     * @param vElement2     another vertex's stored element
     * @param edgeElement   the element to store in the new edge
     *
     * @return returns the edge after the element has been created successfully
     * @throws InvalidVertexException this exception is triggered when the vertex doesn't exists or if it is null
     * @throws InvalidEdgeException this exception is triggered when the element already exists or if it is null
     */
    @Override
    public Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if(vElement1 == null || vElement2 == null || !vertices.containsKey(vElement1) || !vertices.containsKey(vElement2))
            throw new InvalidVertexException("At least One of the Vertexes doesn't exist.");
        Vertex<V> v1 = vertices.get(vElement1);
        Vertex<V> v2 = vertices.get(vElement2);
        return insertEdge(v1, v2, edgeElement);
    }

    /**
     * This method is responsible for removing a vertex and its key from the map
     * @param v     vertex to remove
     *
     * @return returns the removed element
     * @throws InvalidVertexException if the vertex doesn't exist or if it has incident edges then this exception will be triggered
     */
    @Override
    public V removeVertex(Vertex<V> v) throws InvalidVertexException {
        if (!vertices.containsValue(v))
            throw new InvalidVertexException("Vertex does not exist");

        MyVertex vertex = checkVertex(v);

        if (!vertex.incidentEdges.isEmpty())
        {
            throw new InvalidVertexException("Vertex has incident edges");
        }
        vertices.remove(v.element());
        return vertex.element;
    }

    /**
     * This method is responsible for removing an edge from the list of edges of the 2 vertexes that contain said edge
     * @param e     edge to remove
     *
     * @return returns the element of the removed edge
     * @throws InvalidEdgeException this exception is triggered when the edge already doesn't exists or if it is null
     */
    @Override
    public E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        Collection<Edge<E,V>> listOfEdges = edges();
        if (!listOfEdges.contains(e))
            throw new InvalidEdgeException("Edge does not exist");
        Vertex<V>[] vertexes = checkEdge(e).vertices();
        checkVertex(vertexes[0]).incidentEdges.remove(e);
        checkVertex(vertexes[1]).incidentEdges.remove(e);
        return e.element();
    }

    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if(vertices.containsKey(newElement))
            throw new InvalidVertexException("This Key already exists");
        MyVertex vertex = checkVertex(v);
        V oldElement = vertex.element;
        vertex.element = newElement;
        return oldElement;
    }

    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        MyEdge edge = checkEdge(e);
        for (Edge temp: edges()) {
            if(temp.element() == newElement)
                throw new InvalidEdgeException("Element already exists");
        }
        E oldElement = edge.element;
        edge.element = newElement;
        return oldElement;
    }

    private class MyVertex implements Vertex<V> {
        private V element;
        private List<Edge<E,V>> incidentEdges;

        public MyVertex(V element) {
            this.element = element;
            this.incidentEdges = new ArrayList<>();
        }

        @Override
        public V element() {
            return element;
        }

        @Override
        public String toString() {
            return "Vertex{" + element + '}' + " --> " + incidentEdges.toString();
        }
    }

    private class MyEdge implements Edge<E, V> {
        private E element;

        public MyEdge(E element) {
            this.element = element;
        }

        @Override
        public E element() {
            return element;
        }

        @Override
        public Vertex<V>[] vertices() {
            List<Vertex<V>> adjacentVertices = new ArrayList<>();

            for(Vertex<V> v : GraphAdjacencyList.this.vertices.values()) {
                MyVertex myV = (MyVertex) v;

                if( myV.incidentEdges.contains(this)) {
                    adjacentVertices.add(v);
                }
            }

            if(adjacentVertices.isEmpty()) {
                return new Vertex[]{null, null}; //edge was removed meanwhile
            } else {
                return new Vertex[]{adjacentVertices.get(0), adjacentVertices.get(1)};
            }
        }

        @Override
        public String toString() {
            return "Edge{" + ((Route) element).getAdjacency() + "}";
        }
    }

    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if(v == null) throw new InvalidVertexException("Null vertex.");

        MyVertex vertex;
        try {
            vertex = (MyVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsValue(v)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if(e == null) throw new InvalidEdgeException("Null edge.");

        MyEdge edge;
        try {
            edge = (MyEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an edge.");
        }

        if (!edges().contains(edge)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph | Adjacency List : \n");
        sb.append("Number of vertexes: " + vertices.size() +"\nNumber of Edges: " + numEdges() +"\n");
        for(Vertex<V> v : vertices.values()) {
            sb.append( String.format("%s", v) );
            sb.append("\n");
        }

        return sb.toString();
    }
}
