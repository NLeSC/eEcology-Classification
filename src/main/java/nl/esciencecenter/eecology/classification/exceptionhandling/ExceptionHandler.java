package nl.esciencecenter.eecology.classification.exceptionhandling;

import java.io.File;

/**
 * Arbitrary exceptions that stopped execution of a job can be handled by an object with this interface.
 *
 * @author christiaan
 *
 */
public interface ExceptionHandler {
    public void handle(Exception e, File path);
}
