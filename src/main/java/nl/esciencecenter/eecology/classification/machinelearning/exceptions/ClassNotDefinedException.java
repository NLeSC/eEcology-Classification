package nl.esciencecenter.eecology.classification.machinelearning.exceptions;

public class ClassNotDefinedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClassNotDefinedException(Exception e) {
        super(e);
    }

}
