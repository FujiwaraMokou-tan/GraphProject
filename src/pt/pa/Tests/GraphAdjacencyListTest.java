package pt.pa.Tests;

import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.pa.graph.*;
import pt.pa.model.Hub;
import pt.pa.model.Positions;
import pt.pa.model.Route;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphAdjacencyListTest {
    Graph<Hub, Route> distances = new GraphAdjacencyList<>();
    Positions pos1;
    Positions pos2;
    Positions pos3;
    Hub hub1;
    Hub hub2;
    Hub hub3;
    Vertex<Hub> vhub1;
    Vertex<Hub> vhub2;
    Vertex<Hub> vhub3;
    Route route1;
    Route route2;
    Edge<Route, Hub> edge1;
    Edge<Route, Hub> edge2;

    @BeforeEach
    void setUp() {
        pos1 = new Positions(5, 5);
        pos2 = new Positions(25, 35);
        pos3 = new Positions(125, 45);
        hub1 = new Hub("Gensokyo", 44, pos1);
        hub2 = new Hub("Lordran", 23, pos2);
        hub3 = new Hub("Whiterun", 50, pos3);
        route1 = new Route(500);
        route2 = new Route(600);
        vhub1 = distances.insertVertex(hub1);
        vhub2 = distances.insertVertex(hub2);
        vhub3 = distances.insertVertex(hub3);
        edge1 = distances.insertEdge(hub1, hub2, route1);
    }

    @Test
    void numVertices() {
        Hub hub4 = new Hub("Novigrad", 99, new Positions(1,1));
        Hub hub5 = new Hub("Hentailand", 99, new Positions(4,3));
        distances.insertVertex(hub4);
        assertEquals(4, distances.numVertices());
        distances.removeVertex(vhub3);
        assertNotEquals(4, distances.numVertices());
        distances.insertVertex(hub5);
        assertEquals(4, distances.numVertices());
        assertNotEquals(5, distances.numVertices());
    }

    @Test
    void opposite() {
        assertEquals(vhub2, distances.opposite(vhub1, edge1));
        assertNotEquals(vhub3, distances.opposite(vhub1, edge1));
        edge2 = distances.insertEdge(hub2, hub3, route2);
        assertEquals(vhub2, distances.opposite(vhub3, edge2));
        distances.removeEdge(edge2);
        distances.removeVertex(vhub3);
        assertThrows(InvalidVertexException.class, () -> distances.opposite(vhub3, edge1));
        distances.insertVertex(hub3);
        distances.insertEdge(hub2, hub3, route2);
        assertThrows(InvalidVertexException.class, () -> distances.opposite(vhub3, edge2));
    }

    @Test
    void InsertEdge() {
        Edge<Route, Hub> edge3 = distances.insertEdge(vhub1, vhub3, new Route(660));
        assertThrows(InvalidEdgeException.class, () -> distances.insertEdge(vhub1, vhub3, edge3.element()));
        assertEquals(2, distances.numEdges());
        assertEquals(edge3.element(), distances.removeEdge(edge3));
        assertNotEquals(3, distances.numEdges());
        assertNotNull(distances.insertEdge(vhub1, vhub3, edge3.element()));
        assertThrows(InvalidEdgeException.class, () -> distances.insertEdge(vhub1, vhub3, edge3.element()));
        assertEquals(2, distances.numEdges());
    }

    @Test
    void InsertEdgeUsingHub() {
        Route route1 = new Route(660);
        assertNotNull(distances.insertEdge(hub1, hub3, route1));
        assertThrows(InvalidEdgeException.class, () -> distances.insertEdge(hub1, hub3, route1));
        Edge<Route, Hub> edg = null;
        for (Edge<Route, Hub> edge: distances.edges()){
            if(edge.element() == route1)
                edg = edge;
        }
        assertEquals(edg.element(), distances.removeEdge(edg));
        Edge<Route, Hub> edg2 = edg;
        assertThrows(InvalidEdgeException.class, () -> distances.removeEdge(edg2));
    }

    @Test
    void ReplacedElement() {
        Hub hub4 = new Hub("HentaiLand", 69, new Positions(12, 45));
        Vertex<Hub> vhub4 = distances.insertVertex(hub4);
        distances.removeVertex(vhub4);
        assertEquals(hub3, distances.replace(vhub3,hub4));
        assertEquals(hub4, distances.replace(vhub3,hub4));
        assertEquals(hub4, distances.removeVertex(vhub3));
    }

    @Test
    void removeReplacedEdgeElement() {
        Route route3 = new Route(900);
        assertEquals(route1, distances.replace(edge1, route3));
        assertEquals(route3, distances.removeEdge(edge1));
    }

    @Test
    void incidentEdge() {
        assertEquals(edge1, distances.incidentEdges(vhub1).iterator().next());
        assertEquals(edge1, distances.incidentEdges(vhub2).iterator().next());
        Hub hub4 = new Hub("HentaiLand", 69, new Positions(12, 45));
        Vertex<Hub> vhub4 = distances.insertVertex(hub4);
        distances.removeVertex(vhub4);
        assertThrows(InvalidVertexException.class, () -> distances.incidentEdges(vhub4));
    }

}
