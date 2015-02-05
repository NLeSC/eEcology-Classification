package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.IOException;

public class UnableToReadGpsFixesFileException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnableToReadGpsFixesFileException(String message, IOException e) {
        super(message, e);
    }

}
