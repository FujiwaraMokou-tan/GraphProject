package pt.pa.model;

public class LogisticNetwork {
    private Hub[] hubs;
    private int [][] routes;
    private String routeTxt = "";
    private boolean AdjancencyOrEdge;

    /**
     * The constructor of our logistic network, once we have the size of the dataset we can then set the size of our variables
     * @param size the size our logisticNetwork will have (number of Hubs)
     */
    public LogisticNetwork(int size) {
        hubs = new Hub[size];
        routes = new int[size][size];
    }

    /**
     * A method that returns that type of graph we will have
     * @return returns a boolean value that will be used to determine what the user has chosen as a preferred graph type
     */
    public boolean isAdjancencyOrEdge() {
        return AdjancencyOrEdge;
    }

    /**
     *This method returns the size of our Network(Number of Hubs)
     * @return returns the size of our logistic Network
     */
    public int getSize(){
        return hubs.length;
    }

    /**
     *This method returns a certain Hub
     * @param position the position of the Hub we want
     * @return returns a Hub
     */
    public Hub getHub(int position){ return hubs[position]; }

    /**
     *This method returns all the Hubs we have in our logistic network
     * @return returns all the hubs(cities)
     */
    public Hub[] getAllHubs(){
        return hubs;
    }

    /**
     *This method serves to set a value between two hubs via the coordinates
     * @param y y coordinate
     * @param x x coordinate
     * @param value the value that will be changed
     */
    public void setRoute(int y, int x, int value){
        routes[y][x] = value;
    }

    /**
     *This method returns the value that is in between two vertexes
     * @param y y coordinate
     * @param x x coordinate
     * @return returns the value from a specific position
     */
    public int getRoute(int y, int x){
        return routes[y][x];
    }

    /**
     * This method serves to return the name file we used to fill the routes when we were reading the dataset
     * @return returns a String value which is the name of the text file
     */
    public String getRouteTxt(){
        return routeTxt;
    }

    public void  createNetwork(String[] names, int[] weights, Positions[] positions, int [][] routes, String routeName,String type){
        this.routes = routes;
        routeTxt = routeName;
        for(int i = 0; i < hubs.length; i++){
            this.hubs[i] = new Hub(names[i], weights[i], positions[i]);
        }
        if(type.toLowerCase().charAt(0)=='e')
            AdjancencyOrEdge = false;
        else
            AdjancencyOrEdge = true;
    }

}