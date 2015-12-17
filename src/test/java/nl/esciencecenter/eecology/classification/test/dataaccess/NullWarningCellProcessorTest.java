package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.util.CsvContext;

import nl.esciencecenter.eecology.classification.dataaccess.NullWarningCellProcessor;

public class NullWarningCellProcessorTest {
    private final CsvContext context = new CsvContext(0, 0, 0);
    private final String notNull = "not null";
    private PrinterFixture printer;

    @Test
    public void execute_notNullWithPrinter_noWarning() {
        // Arrange
        NullWarningCellProcessor nullWarningCellProcessor = getNullWarningCellProcessorWithPrinter();

        // Act
        nullWarningCellProcessor.execute(notNull, context);

        // Assert
        assertEquals(0, printer.hasWarned());
    }

    @Test
    public void execute_nullWithPrinter_warn() {
        // Arrange
        NullWarningCellProcessor nullWarningCellProcessor = getNullWarningCellProcessorWithPrinter();

        // Act
        nullWarningCellProcessor.execute(null, context);

        // Assert
        assertTrue(printer.hasWarned() > 0);
    }

    @Test
    public void execute_nullTwice_warnOnce() {
        // Arrange
        NullWarningCellProcessor nullWarningCellProcessor = getNullWarningCellProcessorWithPrinter();

        // Act
        nullWarningCellProcessor.execute(null, context);
        nullWarningCellProcessor.execute(null, context);

        // Assert
        assertEquals(1, printer.hasWarned());
    }

    @Test
    public void execute_notNullWithNext_nextCalled() {
        // Arrange
        CellProcessorFixture next = new CellProcessorFixture();
        NullWarningCellProcessor nullWarningCellProcessor = new NullWarningCellProcessor(next, printer, "");

        // Act
        nullWarningCellProcessor.execute(notNull, context);

        // Assert
        assertTrue(next.wasExecuted());
    }

    @Test
    public void execute_nullWithNext_nextCalled() {
        // Arrange
        CellProcessorFixture next = new CellProcessorFixture();
        NullWarningCellProcessor nullWarningCellProcessor = new NullWarningCellProcessor(next, printer, "");

        // Act
        nullWarningCellProcessor.execute(null, context);

        // Assert
        assertTrue(next.wasExecuted());

    }

    @Before
    public void startUp() {
        printer = new PrinterFixture();
    }

    private NullWarningCellProcessor getNullWarningCellProcessorWithPrinter() {
        NullWarningCellProcessor nullWarningCellProcessor = new NullWarningCellProcessor(printer, "");
        return nullWarningCellProcessor;
    }
}
