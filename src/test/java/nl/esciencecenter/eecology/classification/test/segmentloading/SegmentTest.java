package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.junit.After;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SegmentTest {
    private final Path path = Paths.get("src/test/java/resources/segmenttest.json");

    @Test
    /**
     * There once was a bug in the loading of segments where the measurements got duplicated. This tests for this bug.
     */
    public void saveAndLoadWithObjectMapper_segmentWithMeasurements_numberOfMeasurementsStaysConstant()
            throws JsonGenerationException, JsonMappingException, IOException {
        // Arrange
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurements.add(measurement);
        Segment segment = new Segment(measurements);
        ObjectMapper objectMapper = new ObjectMapper();

        // Act
        objectMapper.writeValue(new File(path.toString()), segment);
        Segment result = objectMapper.readValue(new File(path.toString()), new TypeReference<Segment>() {
        });

        // Assert
        assertEquals(1, result.getMeasurements().size());
    }

    @After
    public void cleanUp() throws IOException {
        Files.deleteIfExists(path);
    }
}
