package pt.pa.graph;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.*;

public class GraphAdjacencyList<V,E> implements Graph<V, E> {

    private Map<V, Vertex<V>> vertices;

    public GraphAdjacencyList() {
        this.vertices = new HashMap<>();
    }

    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        MyVertex myU = checkVertex(u);
        MyVertex myV = checkVertex(v);
        Set<Edge<E,V>> intersection = new HashSet<>(myU.incidentEdges);
        intersection.retainAll(myV.incidentEdges);
        return !intersection.isEmpty();
    }

    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        return edges().size();
    }

    @Override
    public Collection<Vertex<V>> vertices() {
        return vertices.values();
    }

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

    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {
        if (!this.vertices.containsValue(v))
            throw new InvalidVertexException("Vertex does not exist");

        return checkVertex(v).incidentEdges;
    }

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

    @Override
    public Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if(vElement1 == null || vElement2 == null || !vertices.containsKey(vElement1) || !vertices.containsKey(vElement2))
            throw new InvalidVertexException("At least One of the Vertexes doesn't exist.");
        Vertex<V> v1 = vertices.get(vElement1);
        Vertex<V> v2 = vertices.get(vElement2);
        return insertEdge(v1, v2, edgeElement);
    }

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
