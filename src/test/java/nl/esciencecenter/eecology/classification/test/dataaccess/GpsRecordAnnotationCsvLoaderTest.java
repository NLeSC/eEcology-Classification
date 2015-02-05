package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.GpsRecordAnnotationCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.UnableToReadGpsFixesFileException;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordAnnotation;

import org.junit.Before;
import org.junit.Test;

public class GpsRecordAnnotationCsvLoaderTest {

    private final Path path = Paths.get("src/test/java/resources/gpsfixloading/");
    private final Path testFilePath = path.resolve("testgpsfixes.csv");
    private final Path testFileWithoutHeadersPath = path.resolve("testgpsfixeswitoutheaders.csv");
    private final Path testFileCausingNullReference = path.resolve("gull1annotations.csv");
    private GpsRecordAnnotationCsvLoader gpsRecordAnnotationCsvLoader;

    @Test
    public void load_canBeCalled() {
        // Act
        gpsRecordAnnotationCsvLoader.load(testFilePath.toString());
    }

    @Test
    public void load_has5Results() {
        // Act
        List<GpsRecordAnnotation> results = gpsRecordAnnotationCsvLoader.load(testFilePath.toString());

        // Assert
        assertEquals(5, results.size());
    }

    @Test
    public void load_firstHasCorrectLabel() {
        // Act
        List<GpsRecordAnnotation> results = gpsRecordAnnotationCsvLoader.load(testFilePath.toString());

        // Assert
        assertEquals(2, results.get(0).getAnnotation());
    }

    @Test(expected = UnableToReadGpsFixesFileException.class)
    public void load_fileWithoutHeaders_failGracefully() {
        // Act
        List<GpsRecordAnnotation> results = gpsRecordAnnotationCsvLoader.load(testFileWithoutHeadersPath.toString());

        // Assert
        assertEquals(2, results.get(0).getAnnotation());
    }

    @Test(expected = UnableToReadGpsFixesFileException.class)
    public void load_fileCausingNullReference_failGracefully() {
        // Act
        List<GpsRecordAnnotation> results = gpsRecordAnnotationCsvLoader.load(testFileCausingNullReference.toString());

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
