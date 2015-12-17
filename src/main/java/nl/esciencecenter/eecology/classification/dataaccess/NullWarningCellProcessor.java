package nl.esciencecenter.eecology.classification.dataaccess;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import nl.esciencecenter.eecology.classification.commands.Printer;

public class NullWarningCellProcessor extends CellProcessorAdaptor {

    private final Printer printer;
    private boolean mute = false;
    private final String columnName;

    public NullWarningCellProcessor(Printer printer, String columnName) {
        super();
        this.printer = printer;
        this.columnName = columnName;
    }

    public NullWarningCellProcessor(CellProcessor cellProcessor, Printer printer, String columnName) {
        super(cellProcessor);
        this.printer = printer;
        this.columnName = columnName;
    }

    @Override
    public Object execute(Object input, CsvContext context) {
        if (input == null && mute == false) {
            printer.warn(composeWarningMessage(context));
            mute = true; // Print max 1 warning each job to prevent flooding the user with messages.
        }
        return next.execute(input, context);
    }

    private String composeWarningMessage(CsvContext context) {
        StringBuilder message = new StringBuilder();
        message.append("Warning: Missing value(s) encountered while loading gps records for column '");
        message.append(columnName);
        message.append("'. First occurence at line ");
        message.append(context.getLineNumber());
        message.append(". Zero(s) is inserted instead.");
        return message.toString();
    }

}