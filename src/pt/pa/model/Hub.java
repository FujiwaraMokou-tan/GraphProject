package pt.pa.model;

public class Hub {
    private String name;
    private int weight;
    private Positions position;

    public Hub(String name, int weight, Positions position) {
        this.name = name;
        this.weight = weight;
        this.position = position;

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Positions getPosition(){
        return position;
    }

    @Override
    public String toString() {
        return /*"Hub name: " +*/ "" + getName(); //+ "\t|Weight of Products: " + getWeight();
    }
}
