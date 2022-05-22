package pt.pa.model;

public class Positions {
    private int x;
    private int y;

    public Positions(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Changes the value of the coordinate x
     * @param x the new value x will have
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Changes the value of the coordinate y
     * @param y the new value y will have
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * This method returns the current value x
     * @return returns the value x
     */
    public int getX() {
        return x;
    }

    /**
     * This method returns the current value y
     * @return returns the value y
     */
    public int getY() {
        return y;
    }
}
