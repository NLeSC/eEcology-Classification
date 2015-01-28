package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordAnnotationCsvLoader;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordAnnotation;

import org.junit.Before;
import org.junit.Test;

public class GpsRecordAnnotationCsvLoaderTest {

    private final String path = "src/test/java/resources/";
    private final String testFileName = path + "testgpsfixes.csv";
    private GpsRecordAnnotationCsvLoader gpsRecordAnnotationCsvLoader;

    @Test
    public void load_canBeCalled() {
        // Act
        gpsRecordAnnotationCsvLoader.load(testFileName);
    }

    @Test
    public void load_has5Results() {
        // Act
        List<GpsRecordAnnotation> results = gpsRecordAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(5, results.size());
    }

    @Test
    public void load_firstHasCorrectLabel() {
        // Act
        List<GpsRecordAnnotation> results = gpsRecordAnnotationCsvLoader.load(testFileName);

        // Assert
        assertEquals(2, results.get(0).getAnnotation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void load_nonExistentFilePath_throwError() {
        // Act
        gpsRecordAnnotationCsvLoader.load("some non existent file.csv");
    }

    @Before
    public void setUp() {
        gpsRecordAnnotationCsvLoader = new GpsRecordAnnotationCsvLoader();
    }
}
