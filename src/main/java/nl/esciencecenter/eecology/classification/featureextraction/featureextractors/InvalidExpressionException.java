package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

public class InvalidExpressionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidExpressionException(String message, Throwable e) {
        super(message, e);
    }

}
