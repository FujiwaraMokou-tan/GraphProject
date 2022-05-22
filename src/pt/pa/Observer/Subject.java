package pt.pa.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject class that will be extended by our model, this class contains our list of Observers (in this case our View) so we can communicate our updates
 */
public class Subject implements Observable {

    private List<Observer> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        if(!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object arg) {
        for(Observer o : observers) {
            o.update(this, arg);
        }
    }
}