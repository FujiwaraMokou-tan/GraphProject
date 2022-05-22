package pt.pa.model;

public class LogisticNetwork {
    private Hub[] hubs;
    private int [][] routes;
    private String routeTxt = "";
    private boolean AdjancencyOrEdge;

    public LogisticNetwork(int size) {
        hubs = new Hub[size];
        routes = new int[size][size];
    }

    public boolean isAdjancencyOrEdge() {
        return AdjancencyOrEdge;
    }

    /**
     *
     * @return returns the size of our logistic Network
     */
    public int getSize(){
        return hubs.length;
    }

    /**
     *
     * @param position the position of the Hub we want
     * @return
     */
    public Hub getHub(int position){ return hubs[position]; }

    /**
     *
     * @return returns all the hubs(cities)
     */
    public Hub[] getAllHubs(){
        return hubs;
    }

    /**
     *
     * @param y y coordinate
     * @param x x coordinate
     * @param value the value that will be changed
     */
    public void setRoute(int y, int x, int value){
        routes[y][x] = value;
    }

    /**
     *
     * @param y y coordinate
     * @param x x coordinate
     * @return returns the value from a specific position
     */
    public int getRoute(int y, int x){
        return routes[y][x];
    }

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