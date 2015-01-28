package nl.esciencecenter.eecology.classification.commands;

public class Printer {
    public void print(String message) {
        System.out.print(message);
    }

    public void warn(String message) {
        System.err.print(message);
    }
}
