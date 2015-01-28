package nl.esciencecenter.eecology.classification.dataaccess;

public class UndefinedLabelException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UndefinedLabelException(String message) {
        super(message);
    }
}
