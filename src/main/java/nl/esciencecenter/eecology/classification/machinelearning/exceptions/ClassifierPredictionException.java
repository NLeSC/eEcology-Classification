package nl.esciencecenter.eecology.classification.machinelearning.exceptions;

public class ClassifierPredictionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClassifierPredictionException(String message, Exception e) {
        super(message, e);
    }

}
