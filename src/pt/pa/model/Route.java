package pt.pa.model;

public class Route {
    private int route;
    public Route(int route) {
        this.route = route;
    }

    public int getRoute() {
        return route;
    }
    public void setRoute(int route) {
        this.route = route;
    }

    @Override
    public String toString(){
        return ""+ route;
    }

    public String getAdjacency(){
        return getRoute() + "km";
    }

}
