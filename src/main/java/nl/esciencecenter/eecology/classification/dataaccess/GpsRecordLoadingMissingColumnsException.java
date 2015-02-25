package nl.esciencecenter.eecology.classification.dataaccess;

import nl.esciencecenter.eecology.classification.exceptionhandling.ClassificationToolException;

public class GpsRecordLoadingMissingColumnsException extends ClassificationToolException {

    private static final long serialVersionUID = 1L;

    public GpsRecordLoadingMissingColumnsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

}
