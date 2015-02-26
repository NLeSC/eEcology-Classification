package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.exceptionhandling.ClassificationToolException;

public class InvalidExpressionException extends ClassificationToolException {

    private static final long serialVersionUID = 1L;

    public InvalidExpressionException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

}
