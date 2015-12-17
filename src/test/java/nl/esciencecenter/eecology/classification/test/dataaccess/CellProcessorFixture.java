package nl.esciencecenter.eecology.classification.test.dataaccess;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class CellProcessorFixture implements CellProcessor {

    private boolean wasExecuted;

    public boolean wasExecuted() {
        return wasExecuted;
    }

    @Override
    public Object execute(Object value, CsvContext context) {
        wasExecuted = true;
        return null;
    }

}
