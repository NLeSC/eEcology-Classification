package nl.esciencecenter.eecology.classification.dataaccess;

import nl.esciencecenter.eecology.classification.exceptionhandling.ClassificationToolException;

public class CustomFeatureFileNotFoundException extends ClassificationToolException {
    private static final long serialVersionUID = 1L;

    public CustomFeatureFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

}
