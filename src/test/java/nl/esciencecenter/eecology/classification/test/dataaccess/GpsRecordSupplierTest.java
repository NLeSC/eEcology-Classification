package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordAnnotationCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordDtoCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordSupplier;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;

public class GpsRecordSupplierTest {
    private final String path = "src/test/java/resources/annotatedgpstest/";
    private final String gpsRecordsPath = path + "honeybuzzard.gpsrecordings.csv";
    private final String annotationsPath = path + "honeybuzzard.annotations.txt";

    private GpsRecordSupplier supplier;

    @Test
    public void getAllGpsRecords_testFile_correctNumberOfRecordsReturned() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        assertEquals(35, gpsRecords.size());
    }

    @Before
    public void setUp() {
        supplier = new GpsRecordSupplier();
        supplier.setAnnotationLoader(new GpsRecordAnnotationCsvLoader());
        supplier.setRecordLoader(new GpsRecordDtoCsvLoader(new Printer()));
    }
}
