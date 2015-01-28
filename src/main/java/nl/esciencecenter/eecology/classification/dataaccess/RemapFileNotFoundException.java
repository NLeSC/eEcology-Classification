package nl.esciencecenter.eecology.classification.dataaccess;

public class RemapFileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RemapFileNotFoundException(String message, Throwable e) {
        super(message, e);
    }

}
