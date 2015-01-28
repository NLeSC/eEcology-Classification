package nl.esciencecenter.eecology.classification.commands.exceptions;

public class StoringClassifierException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StoringClassifierException(String message, Throwable e) {
        super(message, e);
    }
}
