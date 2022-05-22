package pt.pa.Observer;

/**
 * Observer Interface, to be implemented on out view so we can update our graph in real time
 */
public interface Observer {
    void update(Observable subject, Object arg);
}
