package nl.esciencecenter.eecology.classification.dataaccess;

public class MapFeatureExtractorFileNotReadableException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MapFeatureExtractorFileNotReadableException(String message, Exception e) {
        super(message, e);
    }

}
