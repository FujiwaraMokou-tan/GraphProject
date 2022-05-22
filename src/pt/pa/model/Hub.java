package pt.pa.model;

/**
 * This class represents the Hub/city class of our project, our vertexes will be composed of Hubs each
 */
public class Hub {
    private String name;
    private int weight;
    private Positions position;

    /**
     *The constructor for the Hub Class
     * @param name Name that will be assigned to the Hub
     * @param weight Weight that will be assigned to the Hub
     * @param position contains the X and Y coordinates on where the Hub is located
     */
    public Hub(String name, int weight, Positions position) {
        this.name = name;
        this.weight = weight;
        this.position = position;

    }

    /**
     * This method returns the name
     * @return String value that represents out variable name
     */
    public String getName() {
        return name;
    }

    /**
     * This method replaced the variable name with a new name
     * @param name The name we want to apply to the Hub
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method serves to return the position of the Hub
     * @return returns the position of our Hub (where he is located)
     */
    public Positions getPosition(){
        return position;
    }

    @Override
    public String toString() {
        return /*"Hub name: " +*/ "" + getName(); //+ "\t|Weight of Products: " + getWeight();
    }
}
