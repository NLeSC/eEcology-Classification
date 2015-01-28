package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GpsRecordTest {
    @Test
    public void test() throws JsonGenerationException, JsonMappingException, IOException {
        // Arrange
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurements.add(measurement);
        GpsRecord gpsRecord = new GpsRecord(measurements);
        ObjectMapper objectMapper = new ObjectMapper();
        String path = "src/test/java/resources/gpsrecordtest.json";

        // Act
        objectMapper.writeValue(new File(path), gpsRecord);
        GpsRecord result = objectMapper.readValue(new File(path), new TypeReference<GpsRecord>() {
        });

        // Assert
        assertEquals(1, result.getMeasurements().size());
    }
}
