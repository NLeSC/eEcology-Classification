package nl.esciencecenter.eecology.classification.exceptionhandling;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.io.Files;

/**
 * Saves the complete stacktrace to a file to help a developer analyse what exactly caused the exception.
 *
 * @author christiaan
 *
 */
public class StackTraceSavingExceptionHandler implements ExceptionHandler {

    private final String logFileName = "stacktrace.log";

    @Override
    public void handle(Exception exception, File path) {
        File file = new File(Paths.get(path.getPath()).resolve(logFileName).toString());
        try {
            Files.write(ExceptionUtils.getStackTrace(exception), file, Charset.defaultCharset());
        } catch (IOException e) {
            // Failed to save the stack trace. We will not try to recover from this again. Just continue.
        }
    }
}
