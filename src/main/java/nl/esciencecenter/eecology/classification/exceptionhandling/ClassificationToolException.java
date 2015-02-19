package nl.esciencecenter.eecology.classification.exceptionhandling;

public abstract class ClassificationToolException extends RuntimeException {
    /**
     * Superclass for all exceptions within the classification tool.
     */
    private static final long serialVersionUID = 1L;

    public ClassificationToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getErrorCode();

}
