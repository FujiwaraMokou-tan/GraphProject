package pt.pa.model;

public class Route {
    private int route;
    public Route(int route) {
        this.route = route;
    }

    /**
     * This method returns the route value(distance)
     * @return returns the current value of route
     */
    public int getRoute() {
        return route;
    }

    /**
     * This method changes the value of route for another value
     * @param route the new route value that will replace the current one
     */
    public void setRoute(int route) {
        this.route = route;
    }

    @Override
    public String toString(){
        return ""+ route;
    }

    /**
     * Returns the route value with the distance measurement attached to it
     * @return a string value which is the sum of the distance with the distance measurement
     */
    public String getAdjacency(){
        return getRoute() + "km";
    }

}
