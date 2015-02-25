package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordDtoCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordLoadingException;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordLoadingMissingColumnsException;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordDto;

import org.junit.Before;
import org.junit.Test;

public class GpsRecordDtoCsvLoaderTest {

    private final String path = "src/test/java/resources/gpsrecords/";
    private final String testFileName = path + "testgpsfixes.csv";
    private final String testWithExtraColumnFileName = path + "testgpsfixeswithextracolumn.csv";
    private final String testWithMissingColumnFileName = path + "testgpsfixesmissingcolumns.csv";
    private GpsRecordDtoCsvLoader gpsFixAnnotationCsvLoader;
    private final double errorMargin = 0.00001;

    @Test
    public void load_canBeCalled() {
        // Act
        gpsFixAnnotationCsvLoader.load(testFileName);
    }

    @Test
    public void load_has11Results() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(11, results.size());
    }

    @Test
    public void load_getSatelites_usedIsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(10, results.get(1).getSatellitesUsed());
    }

    @Test
    public void load_getDateTime_IsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals("2011-05-31T17:13:21.000+0000", results.get(10).getTimeStamp().toString(Constants.DATE_TIME_PATTERN_ISO8601));
    }

    @Test
    public void load_getDeviceId_IsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(538, results.get(5).getDeviceId());
    }

    @Test
    public void load_getPressure_IsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(9, results.get(4).getPressure());
    }

    @Test
    public void load_getAltitude_IsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(5, results.get(4).getAltitude());
    }

    @Test
    public void load_getAltitudeAboveGround_IsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(-8, results.get(4).getAltitudeAboveGround(), errorMargin);
    }

    @Test
    public void load_getTemperature_IsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(28.1, results.get(4).getTemperature(), errorMargin);
    }

    @Test
    public void load_getGpsFixTime_IsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(7.5, results.get(4).getGpsFixTime(), errorMargin);
    }

    @Test
    public void load_withExtraColumn_hasCorrectNumberOfResults() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testWithExtraColumnFileName);

        // Assert
        assertEquals(11, results.size());
    }

    @Test(expected = GpsRecordLoadingException.class)
    public void load_nonExistentFilePath_throwError() {
        // Act
        gpsFixAnnotationCsvLoader.load("some non existent file.csv");
    }

    @Test(expected = GpsRecordLoadingMissingColumnsException.class)
    public void load_csvMissingColumns_throwError() {
        // Act
        gpsFixAnnotationCsvLoader.load(testWithMissingColumnFileName);
    }

    @Before
    public void setUp() {
        gpsFixAnnotationCsvLoader = new GpsRecordDtoCsvLoader();
    }
}
