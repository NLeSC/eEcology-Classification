package nl.esciencecenter.eecology.classification.exceptionhandling;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.ConfigurationException;
import com.google.inject.CreationException;
import com.google.inject.ProvisionException;
import com.google.inject.spi.Message;

/**
 * Prints exception messages onto the screen without stack traces in order to keep the users attention on the actual cause of the
 * exception rather than scare the user away.
 *
 * @author christiaan
 *
 */
public class MessagePrintingExceptionHandler implements ExceptionHandler {

    @Override
    public void handle(Exception exception, File path) {
        List<String> messages = getInformativeExceptionMessages(exception);
        StringBuilder report = generateReport(messages, path.getName());
        System.err.print(report.toString());
    }

    private StringBuilder generateReport(List<String> messages, String name) {
        StringBuilder report = new StringBuilder("\nThe classification tool stopped executing job '" + name
                + "' for the following reason:\n");
        String separator = "\nThis was caused by:\n";
        report.append(StringUtils.join(messages, separator));
        report.append("\n");
        return report;
    }

    private LinkedList<String> getInformativeExceptionMessages(Exception exception) {
        LinkedList<String> messages = new LinkedList<String>();
        Throwable cause = exception;
        while (cause != null) {
            addMessageIfInformative(messages, cause);
            cause = cause.getCause();
        }
        return messages;
    }

    private void addMessageIfInformative(LinkedList<String> messages, Throwable cause) {
        if (cause instanceof ProvisionException) { // This technical exception is hidden. Only its causes should be shown.
            return;
        }
        if (cause instanceof ConfigurationException) { // A missing setting in settings.properties can cause this exception.
            ConfigurationException configurationException = (ConfigurationException) cause;
            addClearifiedErrorMessages(messages, configurationException.getErrorMessages());
            return;
        }
        if (cause instanceof CreationException) { // A missing setting in settings.properties can cause this exception.
            CreationException creationException = (CreationException) cause;
            addClearifiedErrorMessages(messages, creationException.getErrorMessages());
            return;
        }
        messages.add(cause.getMessage());
    }

    private void addClearifiedErrorMessages(LinkedList<String> messages, Collection<Message> errorMessages) {
        for (Message message : errorMessages) {
            messages.add(clearify(message.toString()));
        }
    }

    private String clearify(String technicalMessage) {
        String string = technicalMessage.split("\\) was bound\\.")[0];
        String[] keyValue = string.split("=");
        if (keyValue.length == 2) {
            String value = keyValue[1];
            return "Cannot start job because of missing setting '" + value + "'.";
        }
        return technicalMessage;
    }
}