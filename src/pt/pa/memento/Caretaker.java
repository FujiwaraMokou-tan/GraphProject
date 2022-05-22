package pt.pa.memento;

import pt.pa.graphicInterface.Alerts;
import pt.pa.model.GraphModel;

import java.util.Stack;

/**
 * This class is responsible for managing our save-states so that we can go back to a previous change without problems
 */
public class Caretaker {
    private Stack<Memento> objMementos;
    private Originator originator;

    /**
     *This constructor will serve as a way to know who the caretaker will take care of.
     * @param originator our originator the class which will contain the main data that we want to save  so that we can acces the methods to restore and create a memento
     */
    public Caretaker(Originator originator) {
        this.originator = originator;
        objMementos = new Stack();
    }

    /**
     * This method is responsible for saving a savestate, adding it into our stack so that we can later go back if needed be
     */
    public void saveState() {
        objMementos.add(originator.createMemento());
    }

    /**
     *This method is responsible for going back a the previous move done by the user, popping it from the stack and applying it to the graph
     * @throws Alerts if the memento stack is empty then it will warn the user that there are no previous actions
     */
    public void restoreState() throws Alerts {
        if(objMementos.isEmpty())
            throw new Alerts("The Memento Stack is Empty.", "Empty Stack");
        originator.setMemento(objMementos.pop());
    }

}
