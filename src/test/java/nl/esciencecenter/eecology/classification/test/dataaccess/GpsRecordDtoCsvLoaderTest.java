package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordDtoCsvLoader;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordDto;

import org.junit.Before;
import org.junit.Test;

public class GpsRecordDtoCsvLoaderTest {

    private final String path = "src/test/java/resources/";
    private final String testFileName = path + "testgpsfixes2.csv";
    private GpsRecordDtoCsvLoader gpsFixAnnotationCsvLoader;

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
    public void load_getDateTimeIsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals("2011-05-31T17:13:21.000+0000", results.get(10).getTimeStamp().toString(Constants.DATE_TIME_PATTERN_ISO8601));
    }

    @Test
    public void load_getDeviceIdIsCorrect() {
        // Act
        List<GpsRecordDto> results = gpsFixAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(538, results.get(5).getDeviceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void load_nonExistentFilePath_throwError() {
        // Act
        gpsFixAnnotationCsvLoader.load("some non existent file.csv");
    }

    @Before
    public void setUp() {
        gpsFixAnnotationCsvLoader = new GpsRecordDtoCsvLoader();
    }
}
