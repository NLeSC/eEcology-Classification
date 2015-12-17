package nl.esciencecenter.eecology.classification.test.dataaccess;

import nl.esciencecenter.eecology.classification.commands.Printer;

public class PrinterFixture extends Printer {
    private int hasPrintedCount = 0;
    private int hasWarnedCount = 0;

    @Override
    public void print(String message) {
        hasPrintedCount++;
    }

    @Override
    public void warn(String message) {
        hasWarnedCount++;
    }

    public int hasPrinted() {
        return hasPrintedCount;
    }

    public int hasWarned() {
        return hasWarnedCount;
    }

}
