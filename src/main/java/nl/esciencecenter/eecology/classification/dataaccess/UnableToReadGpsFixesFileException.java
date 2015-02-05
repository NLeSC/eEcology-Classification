package nl.esciencecenter.eecology.classification.dataaccess;


public class UnableToReadGpsFixesFileException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnableToReadGpsFixesFileException(String message, Exception e) {
        super(message, e);
    }

}
