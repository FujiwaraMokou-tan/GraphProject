package pt.pa.Observer;

/**
 * Interface Observable necessary to create the Observer pattern
 */
public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Object arg);
}
