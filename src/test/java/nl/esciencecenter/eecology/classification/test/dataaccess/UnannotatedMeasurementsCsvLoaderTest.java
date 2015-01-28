package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.LoadingMeasurementsException;
import nl.esciencecenter.eecology.classification.dataaccess.UnannotatedMeasurementsCsvLoader;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

public class UnannotatedMeasurementsCsvLoaderTest {

    private final String path = "src/test/java/resources/unannotatedmeasurements/";
    private final String testFile1Name = path + "unannotatedexample.csv";
    private final String testFileWithNullName = path + "unannotatedexamplewithnull.csv";
    private UnannotatedMeasurementsCsvLoader measurementsCsvLoader;
    private final double errorMargin = 0.0001;
    private final int testFile1Count = 10;
    private final String corruptTestFile1Name = path + "corrupt.csv";

    @Test(expected = LoadingMeasurementsException.class)
    public void load_nonExistentPath_exceptionThrown() {
        // Arrange

        // Act
        measurementsCsvLoader.loadFromSingleSource("nonExistent.path");

        // Assert
    }

    @Test
    public void load_testFilePath_noExceptionThrown() {
        // Arrange

        // Act
        measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
    }

    @Test
    public void load_testFilePathWithTrailingSpace_noExceptionThrown() {
        // Arrange

        // Act
        measurementsCsvLoader.loadFromSingleSource(testFile1Name + " ");

        // Assert
    }

    @Test
    public void load_testFilePath_returnsCorrectNumberOfMeasurements() {
        int expectedSize = testFile1Count;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expectedSize, output.size());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectX() {
        // Arrange
        double expected = -0.2978227061;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getX(), errorMargin);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectY() {
        // Arrange
        double expected = -0.1823529412;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getY(), errorMargin);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectZ() {
        // Arrange
        double expected = 0.9779299848;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getZ(), errorMargin);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectTime() {
        // Arrange
        DateTime expected = new DateTime(2014, 06, 07, 12, 4, 24, DateTimeZone.UTC);

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getTimeStamp());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectDeviceId() {
        // Arrange
        int expected = 806;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getDeviceId());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectGpsSpd() {
        // Arrange
        double expected = 0.0463417083823992;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getGpsSpeed(), errorMargin);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectLongitude() {
        // Arrange
        double expected = 10.9820;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectLatitude() {
        // Arrange
        double expected = 52.7186629;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getLatitude(), errorMargin);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectAltitude() {
        // Arrange
        double expected = -5;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getAltitude(), errorMargin);
    }

    @Test
    public void load_testFilePath_returnsLastCorrectIndex() {
        // Arrange
        int expected = 10;

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(output.size() - 1).getIndex());
    }

    @Test
    public void load_testFilePath_returnsLastHasIndex() {
        // Arrange

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(true, output.get(output.size() - 1).hasIndex());
    }

    @Test
    public void load_testFileWithNan_0results() {
        // Arrange
        measurementsCsvLoader.setRemoveMeasurementsContainingNan(true);
        int expected = testFile1Count - 1; // One row contains a nan.

        // Act
        List<IndependentMeasurement> outputs = measurementsCsvLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, outputs.size());
    }

    @Test(expected = LoadingMeasurementsException.class)
    public void load_corruptFile_correctException() {
        // Arrange

        // Act
        measurementsCsvLoader.loadFromSingleSource(corruptTestFile1Name);

        // Assert
    }

    @Test
    public void load_testFileWithNull_noException() {
        // Arrange

        // Act
        measurementsCsvLoader.loadFromSingleSource(testFileWithNullName);

        // Assert
    }

    @Test
    public void load_testFileWithNull_missingIndexHasIndexIsFalse() {
        // Arrange

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.loadFromSingleSource(testFileWithNullName);

        // Assert
        assertFalse(output.get(7).hasIndex());
    }

    @Before
    public void setUp() throws Exception {
        measurementsCsvLoader = new UnannotatedMeasurementsCsvLoader();
    }
}
