package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.FileNotFoundException;

public class MapFeatureExtractorFileNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MapFeatureExtractorFileNotFoundException(String message, FileNotFoundException e) {
        super(message, e);
    }
}
