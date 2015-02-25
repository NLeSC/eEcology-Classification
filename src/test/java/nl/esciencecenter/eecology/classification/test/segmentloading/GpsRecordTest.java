package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordDto;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GpsRecordTest {
    Path path = Paths.get("src/test/java/resources/gpsrecordtest.json");
    private GpsRecordDto gpsRecordDto;
    private final double errorMargin = 0.00001;
    final double someDouble = 12.5;
    final int someInteger = 41;

    @Test
    public void objectMapper_writeAndReadWithObjectMapper_sameResult() throws JsonGenerationException, JsonMappingException,
            IOException {
        // Arrange
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurements.add(measurement);
        GpsRecord gpsRecord = new GpsRecord(measurements);
        ObjectMapper objectMapper = new ObjectMapper();

        // Act
        objectMapper.writeValue(new File(path.toString()), gpsRecord);
        GpsRecord result = objectMapper.readValue(new File(path.toString()), new TypeReference<GpsRecord>() {
        });

        // Assert
        assertEquals(1, result.getMeasurements().size());
    }

    @Test
    public void constructFromDto_recordHasCorrectId() {
        // Arrange
        gpsRecordDto.setDeviceId(someInteger);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someInteger, gpsRecord.getDeviceId());
    }

    @Test
    public void constructFromDto_recordHasCorrectDateTime() {
        // Arrange
        DateTime timeStamp = new DateTime(2015, 2, 25, 13, 46, 15, DateTimeZone.UTC);
        gpsRecordDto.setTimeStamp(timeStamp);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertTrue(timeStamp.isEqual(gpsRecord.getTimeStamp()));
    }

    @Test
    public void constructFromDto_recordHasCorrectAltitude() {
        // Arrange
        gpsRecordDto.setAltitude(someInteger);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someInteger, gpsRecord.getAltitude(), errorMargin);
    }

    @Test
    public void constructFromDto_recordHasCorrectLongitude() {
        // Arrange
        gpsRecordDto.setLongitude(someDouble);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someDouble, gpsRecord.getLongitude(), errorMargin);
    }

    @Test
    public void constructFromDto_recordHasCorrectLatitude() {
        // Arrange
        gpsRecordDto.setLatitude(someDouble);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someDouble, gpsRecord.getLatitude(), errorMargin);
    }

    @Test
    public void constructFromDto_recordHasCorrectPressure() {
        // Arrange
        gpsRecordDto.setPressure(someInteger);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someInteger, gpsRecord.getPressure(), errorMargin);
    }

    @Test
    public void constructFromDto_recordHasCorrectTemperature() {
        // Arrange
        gpsRecordDto.setTemperature(someDouble);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someDouble, gpsRecord.getTemperature(), errorMargin);
    }

    @Test
    public void constructFromDto_recordHasCorrectSatellitesUsed() {
        // Arrange
        gpsRecordDto.setSatellitesUsed(someInteger);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someInteger, gpsRecord.getSatellitesUsed());
    }

    @Test
    public void constructFromDto_recordHasCorrectFixTime() {
        // Arrange
        gpsRecordDto.setGpsFixTime(someDouble);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someDouble, gpsRecord.getGpsFixTime(), errorMargin);
    }

    @Test
    public void constructFromDto_recordHasCorrectSpeed2d() {
        // Arrange
        gpsRecordDto.setSpeed2d(someDouble);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someDouble, gpsRecord.getSpeed2d(), errorMargin);
    }

    @Test
    public void constructFromDto_recordHasCorrectSpeed3d() {
        // Arrange
        gpsRecordDto.setSpeed3d(someDouble);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someDouble, gpsRecord.getSpeed3d(), errorMargin);
    }

    @Test
    public void constructFromDto_recordHasCorrectAltitudeAboveGround() {
        // Arrange
        gpsRecordDto.setAltitudeAboveGround(someDouble);

        // Act
        GpsRecord gpsRecord = new GpsRecord(gpsRecordDto);

        // Assert
        assertEquals(someDouble, gpsRecord.getAltitudeAboveGround(), errorMargin);
    }

    @Before
    public void setUp() {
        gpsRecordDto = new GpsRecordDto();
    }

    @After
    public void cleanUp() throws IOException {
        Files.deleteIfExists(path);
    }
}
