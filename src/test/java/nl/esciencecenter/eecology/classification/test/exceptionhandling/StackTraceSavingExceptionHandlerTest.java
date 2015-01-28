package nl.esciencecenter.eecology.classification.test.exceptionhandling;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import nl.esciencecenter.eecology.classification.exceptionhandling.StackTraceSavingExceptionHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StackTraceSavingExceptionHandlerTest {

    private final Path testPath = Paths.get("src/test/java/resources/");
    private final String testFileName = "stacktrace.log";
    private StackTraceSavingExceptionHandler stackTraceSavingExceptionHandler;
    private File file;

    @Test
    public void singleException_logFileIsCreated() {
        // Arrange
        IOException exception = new IOException("Problem reading from disc.");

        // Act
        stackTraceSavingExceptionHandler.handle(exception, file);

        // Assert
        assertTrue(new File(testPath.resolve(testFileName).toString()).exists());
    }

    @Test
    public void nestedExceptions_logFileContainsBoth() throws IOException {
        // Arrange
        IOException exception = new IOException("Problem reading from disc.", new ArithmeticException("Cannot compute."));

        // Act
        stackTraceSavingExceptionHandler.handle(exception, file);

        // Assert
        String logContent = new String(Files.readAllBytes(testPath.resolve(testFileName)));
        assertTrue("Logfile doesn't contain outer exception.", logContent.contains("Problem reading from disc."));
        assertTrue("Logfile doesn't contain inner exception.", logContent.contains("Cannot compute."));
    }

    @Before
    public void setUp() {
        stackTraceSavingExceptionHandler = new StackTraceSavingExceptionHandler();
        file = new File(testPath.toString());
    }

    @After
    public void cleanUp() {
        File file = new File(testPath.resolve(testFileName).toString());
        file.delete();
    }
}
