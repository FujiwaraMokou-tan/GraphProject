package pt.pa.memento;

import pt.pa.graphicInterface.Alerts;
import pt.pa.model.GraphModel;

import java.util.Stack;

public class Caretaker {
    private Stack<Memento> objMementos;

    private Originator originator;

    public Caretaker(Originator originator) {
        this.originator = originator;
        objMementos = new Stack();
    }

    public void saveState() {
        objMementos.add(originator.createMemento());
    }

    public void restoreState() throws Alerts {
        if(objMementos.isEmpty())
            throw new Alerts("The Memento Stack is Empty.", "Empty Stack");
        originator.setMemento(objMementos.pop());
    }

    public Originator getOriginator() {
        return originator;
    }
}
