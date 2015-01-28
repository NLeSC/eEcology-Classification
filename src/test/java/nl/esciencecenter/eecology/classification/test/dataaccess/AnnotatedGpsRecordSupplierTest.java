package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordAnnotationCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordDtoCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.AnnotatedGpsRecordSupplier;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

public class AnnotatedGpsRecordSupplierTest {
    private final String path = "src/test/java/resources/annotatedgpstest/";
    private final String gpsRecordsPath = path + "honeybuzzard.gpsrecordings.csv";
    private final String annotationsPath = path + "honeybuzzard.annotations.txt";

    private AnnotatedGpsRecordSupplier supplier;
    private final double errorMargin = 0.0001;

    @Test
    public void getAnnotatedGpsRecords_testFile_correctNumberOfRecordsReturned() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        assertEquals(34, gpsRecords.size());
    }

    @Test
    public void getAnnotatedGpsRecords_testFile_firstIsLabeled() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        GpsRecord result = gpsRecords.get(0);
        assertTrue(result.isLabeled());
    }

    @Test
    public void getAnnotatedGpsRecords_testFile_firstHasCorrectLabel() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        GpsRecord result = gpsRecords.get(0);
        assertEquals(3, result.getLabel());
    }

    @Test
    public void getAnnotatedGpsRecords_testFile_firstHasCorrectDeviceId() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        GpsRecord result = gpsRecords.get(0);
        assertEquals(184, result.getDeviceId(), errorMargin);
    }

    @Test
    public void getAnnotatedGpsRecords_testFile_lastHasCorrectTimeStamp() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        GpsRecord result = gpsRecords.get(gpsRecords.size() - 1);
        assertEquals(new DateTime(2014, 05, 04, 20, 19, 18, DateTimeZone.UTC), result.getTimeStamp());
    }

    @Test
    public void getAnnotatedGpsRecords_testFile_firstHasCorrectLatitude() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        GpsRecord result = gpsRecords.get(0);
        assertEquals(9.6837141, result.getLatitude(), errorMargin);
    }

    @Test
    public void getAnnotatedGpsRecords_testFile_firstHasCorrectLongitude() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        GpsRecord result = gpsRecords.get(0);
        assertEquals(6.9903918, result.getLongitude(), errorMargin);
    }

    @Test
    public void getAnnotatedGpsRecords_testFile_firstHasCorrectAltitude() {
        // Arrange

        // Act
        List<GpsRecord> gpsRecords = supplier.getGpsRecords(gpsRecordsPath, annotationsPath);

        // Assert
        GpsRecord result = gpsRecords.get(0);
        assertEquals(442, result.getAltitude(), errorMargin);
    }

    @Before
    public void setUp() {
        supplier = new AnnotatedGpsRecordSupplier();
        supplier.setAnnotationLoader(new GpsRecordAnnotationCsvLoader());
        supplier.setRecordLoader(new GpsRecordDtoCsvLoader());
    }
}
