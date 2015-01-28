package nl.esciencecenter.eecology.classification.segmentloading;

public class NotEnoughSegmentsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotEnoughSegmentsException(String message) {
        super(message);
    }
}
