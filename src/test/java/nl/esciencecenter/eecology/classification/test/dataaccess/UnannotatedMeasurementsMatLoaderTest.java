package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.LoadingMeasurementsException;
import nl.esciencecenter.eecology.classification.dataaccess.UnannotatedMeasurementsMatLoader;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

public class UnannotatedMeasurementsMatLoaderTest {

    private final String path = "src/test/java/resources/unannotatedmeasurements/";
    private final String testFile1Name = path + "measurements.mat";
    private final String testFile2withNanName = path + "measurementswithnans.mat";
    private UnannotatedMeasurementsMatLoader measurementsMatLoader;
    private final double delta = 0.0001;
    private final int testFile1Count = 80;

    @Test(expected = LoadingMeasurementsException.class)
    public void load_nonExistentPath_exceptionThrown() {
        // Arrange

        // Act
        measurementsMatLoader.loadFromSingleSource("nonExistent.path");

        // Assert
    }

    @Test
    public void load_testFilePathWithTrailingSpace_noExceptionThrown() {
        // Arrange

        // Act
        measurementsMatLoader.loadFromSingleSource(testFile1Name + " ");

        // Assert
    }

    @Test
    public void load_testFilePath_returnsCorrectNumberOfMeasurements() {
        int expectedSize = testFile1Count;

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expectedSize, output.size());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectX() {
        // Arrange
        double expected = -0.8187; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getX()) < delta);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectY() {
        // Arrange
        double expected = -0.1498; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getY()) < delta);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectZ() {
        // Arrange
        double expected = 0.4915; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getZ()) < delta);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectTime() {
        // Arrange
        DateTime expected = new DateTime(2013, 07, 06, 12, 20, 54, DateTimeZone.UTC); //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getTimeStamp());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectDeviceId() {
        // Arrange
        int expected = 1507; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(0).getDeviceId());
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectGpsSpd() {
        // Arrange
        double expected = 1.0022; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getGpsSpeed()) < delta);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectLongitude() {
        // Arrange
        double expected = 10.9820; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectLatitude() {
        // Arrange
        double expected = 65.2003; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getLatitude()) < delta);
    }

    @Test
    public void load_testFilePath_returnsFirstCorrectAltitude() {
        // Arrange
        double expected = -2; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertTrue(Math.abs(expected - output.get(0).getAltitude()) < delta);
    }

    @Test
    public void load_testFilePath_returnsLastCorrectIndex() {
        // Arrange
        int expected = 40; //according to matlab

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(expected, output.get(output.size() - 1).getIndex());
    }

    @Test
    public void load_testFilePath_returnsLastHasIndex() {
        // Arrange

        // Act
        List<IndependentMeasurement> output = measurementsMatLoader.loadFromSingleSource(testFile1Name);

        // Assert
        assertEquals(true, output.get(output.size() - 1).hasIndex());
    }

    @Test
    public void load_testFileWithNan_0results() {
        // Arrange
        measurementsMatLoader.setRemoveMeasurementsContainingNan(true);

        // Act
        List<IndependentMeasurement> outputs = measurementsMatLoader.loadFromSingleSource(testFile2withNanName);

        // Assert
        assertEquals(0, outputs.size());
    }

    @Before
    public void setUp() throws Exception {
        measurementsMatLoader = new UnannotatedMeasurementsMatLoader();
    }
}
