package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.UnannotatedMeasurementsCsvLoader;
import nl.esciencecenter.eecology.classification.dataaccess.UnannotatedMeasurementsLoader;
import nl.esciencecenter.eecology.classification.dataaccess.UnannotatedMeasurementsMatLoader;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.junit.Before;
import org.junit.Test;

public class UnannotatedMeasurementLoaderTest {
    private final String path = "src/test/java/resources/unannotatedmeasurements/";
    private final String testCsvName = path + "unannotatedexample.csv";
    private final String testMatName = path + "measurements.mat";
    private UnannotatedMeasurementsLoader measurementsCsvLoader;
    private final int testCsvCount = 10;
    private final int testMatCount = 80;

    @Test
    public void load_matFile_correctNumberOfResults() {
        // Arrange

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.load(getListWith(testMatName));

        // Assert
        assertEquals(testMatCount, output.size());
    }

    @Test
    public void load_csvFile_correctNumberOfResults() {
        // Arrange

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.load(getListWith(testCsvName));

        // Assert
        assertEquals(testCsvCount, output.size());
    }

    @Test
    public void load_2files_correctNumberOfResults() {
        // Arrange
        List<String> paths = new LinkedList<String>();
        paths.add(testCsvName);
        paths.add(testMatName);

        // Act
        List<IndependentMeasurement> output = measurementsCsvLoader.load(paths);

        // Assert
        assertEquals(testCsvCount + testMatCount, output.size());
    }

    @Before
    public void setUp() {
        measurementsCsvLoader = new UnannotatedMeasurementsLoader();
        measurementsCsvLoader.setUnannotatedMeasurementsCsvLoader(new UnannotatedMeasurementsCsvLoader());
        measurementsCsvLoader.setUnannotatedMeasurementsMatLoader(new UnannotatedMeasurementsMatLoader());
    }

    private List<String> getListWith(String string) {
        List<String> list = new LinkedList<String>();
        list.add(string);
        return list;
    }

}
