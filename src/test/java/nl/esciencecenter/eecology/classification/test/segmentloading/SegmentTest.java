package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SegmentTest {
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
        String path = "src/test/java/resources/segmenttest.json";

        // Act
        objectMapper.writeValue(new File(path), segment);
        Segment result = objectMapper.readValue(new File(path), new TypeReference<Segment>() {
        });

        // Assert
        assertEquals(1, result.getMeasurements().size());
    }
}
