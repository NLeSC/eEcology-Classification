package nl.esciencecenter.eecology.classification.dataaccess;

import nl.esciencecenter.eecology.classification.exceptionhandling.ClassificationToolException;

public class GpsRecordLoadingException extends ClassificationToolException {
    private static final long serialVersionUID = 1L;

    public GpsRecordLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

}
