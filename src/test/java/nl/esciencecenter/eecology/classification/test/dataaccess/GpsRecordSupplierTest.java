package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordAnnotationCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordDtoCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordLoadingException;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordSupplier;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GpsRecordSupplierTest {
    private final String path = "src/test/java/resources/annotatedgpstest/";
    private final String gpsRecordsPath = path + "honeybuzzard.gpsrecordings.csv";
    private final String invalidGpsRecordsPath = path + "invalid.gpsrecordings.csv";
    private final String annotationsPath = path + "honeybuzzard.annotations.txt";

    private GpsRecordSupplier supplier;
    private final double errorMargin = 0.0001;

    @Test
    public void getAllGpsRecords_testFile_correctNumberOfRecordsReturned() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        assertEquals(35, gpsRecords.size());
    }

    /**
     * gps speed should be changed to x speed, y speed and z speed for segment, record and measurement
     */
    @Ignore
    @Test
    public void getAnnotatedGpsRecords_testFile_firstHasCorrectXSpeed() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        GpsRecord result = gpsRecords.get(0);
        //        assertEquals(-0.01, result.getXSpeed(), errorMargin);
    }

    @Test(expected = GpsRecordLoadingException.class)
    public void getAnnotatedGpsRecords_invalidTestFile_throwCorrectException() {
        // Act
        supplier.getGpsRecords(invalidGpsRecordsPath, annotationsPath);
    }

    @Before
    public void setUp() {
        supplier = new GpsRecordSupplier();
        supplier.setAnnotationLoader(new GpsRecordAnnotationCsvLoader());
        supplier.setRecordLoader(new GpsRecordDtoCsvLoader());
    }
}
