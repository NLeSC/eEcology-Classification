package nl.esciencecenter.eecology.classification.test.exceptionhandling;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

import nl.esciencecenter.eecology.classification.exceptionhandling.MessagePrintingExceptionHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.ConfigurationException;
import com.google.inject.CreationException;
import com.google.inject.ProvisionException;
import com.google.inject.spi.Message;

public class MessagePrintingExceptionHandlerTest {

    private MessagePrintingExceptionHandler messagePrintingExceptionHandler;
    private ByteArrayOutputStream errContent;
    private PrintStream originalErr;
    private final File path = new File(".");

    @Test
    public void singleException_pathIsMentioned() {
        // Arrange
        IOException exception = new IOException("Problem reading from disc.");

        // Act
        messagePrintingExceptionHandler.handle(exception, new File("testlocation123"));

        // Assert
        assertTrue(errContent.toString().contains("testlocation123"));
    }

    @Test
    public void singleException_showIt() {
        // Arrange
        IOException exception = new IOException("Problem reading from disc.");

        // Act
        messagePrintingExceptionHandler.handle(exception, path);

        // Assert
        assertTrue(errContent.toString().contains("Problem reading from disc."));
    }

    @Test
    public void wrappedExceptions_showBoth() {
        // Arrange
        IOException exception = new IOException("Problem reading from disc.", new ArithmeticException("Does not compute."));

        // Act
        messagePrintingExceptionHandler.handle(exception, path);

        // Assert
        assertTrue("Outer exception not shown.", errContent.toString().contains("Problem reading from disc."));
        assertTrue("Inner exception not shown.", errContent.toString().contains("Does not compute."));
    }

    @Test
    public void guiceProvisionException_showInnerException() {
        // Arrange
        ProvisionException exception = new ProvisionException("Scary Guice error message", new Exception(
                "The selected option is invalid."));

        // Act
        messagePrintingExceptionHandler.handle(exception, path);

        // Assert
        assertTrue(errContent.toString().contains("The selected option is invalid."));
    }

    @Test
    public void guiceProvisionException_dontShowProvisionException() {
        // Arrange
        ProvisionException exception = new ProvisionException("Scary Guice error message", new Exception(
                "The selected option is invalid."));

        // Act
        messagePrintingExceptionHandler.handle(exception, path);

        // Assert
        assertTrue(errContent.toString().contains("Scary Guice error message.") == false);
    }

    @Test
    public void guiceConfigurationException_dontShowConfigurationException() {
        // Arrange
        LinkedList<Message> messages = new LinkedList<Message>();
        messages.add(new Message(
                "No implementation for java.lang.Integer annotated with @com.google.inject.name.Named(value=random_forest_number_of_trees) was bound."));
        ConfigurationException exception = new ConfigurationException(messages);

        // Act
        messagePrintingExceptionHandler.handle(exception, path);

        // Assert
        assertTrue(errContent.toString().contains(
                "No implementation for java.lang.Integer annotated with @com.google.inject.name.Named:") == false);
    }

    @Test
    public void guiceConfigurationException_showConfigurationExceptionCauses() {
        // Arrange
        LinkedList<Message> messages = new LinkedList<Message>();
        messages.add(new Message(
                "No implementation for java.lang.Integer annotated with @com.google.inject.name.Named(value=random_forest_number_of_trees) was bound."));
        ConfigurationException exception = new ConfigurationException(messages);

        // Act
        messagePrintingExceptionHandler.handle(exception, path);

        // Assert
        assertTrue(errContent.toString().contains("random_forest_number_of_trees"));
    }

    @Test
    public void guiceCreationException_dontShowCreationException() {
        // Arrange
        LinkedList<Message> messages = new LinkedList<Message>();
        messages.add(new Message(
                "No implementation for java.lang.String annotated with @com.google.inject.name.Named(value=measurement_classifier_path) was bound."));
        CreationException exception = new CreationException(messages);

        // Act
        messagePrintingExceptionHandler.handle(exception, path);

        // Assert
        assertTrue(errContent.toString().contains(
                "No implementation for java.lang.String annotated with @com.google.inject.name.Named") == false);
    }

    @Test
    public void guiceCreationException_showCreationExceptionCauses() {
        // Arrange
        LinkedList<Message> messages = new LinkedList<Message>();
        messages.add(new Message(
                "No implementation for java.lang.String annotated with @com.google.inject.name.Named(value=measurement_classifier_path) was bound."));
        CreationException exception = new CreationException(messages);

        // Act
        messagePrintingExceptionHandler.handle(exception, path);

        // Assert
        System.out.print(errContent.toString());
        assertTrue(errContent.toString().contains("measurement_classifier_path"));
    }

    @Before
    public void setUp() {
        messagePrintingExceptionHandler = new MessagePrintingExceptionHandler();
        originalErr = System.err;
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUp() {
        System.setErr(originalErr);
    }
}
