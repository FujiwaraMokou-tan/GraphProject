package pt.pa.memento;

public interface Originator {
    Memento createMemento();
    void setMemento(Memento memento);
}
