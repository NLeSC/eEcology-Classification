package nl.esciencecenter.eecology.classification.test.featureextraction;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentFormatter;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class SegmentFormatterTest {
    SegmentFormatter segmentFormatter;
    private final double errorMargin = 0.00001;

    @Test
    public void format_emptyInput_emptyOutput() {
        // Arrange
        List<Segment> emptyList = new LinkedList<Segment>();

        // Act
        FormattedSegments output = segmentFormatter.format(emptyList);

        // Assert
        assertEquals(0, output.getNumberOfRows());
    }

    @Test
    public void format_1Input_1Output() {
        List<Segment> segments = get1WindowWithMeasurements(0);

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(1, output.getNumberOfRows());
    }

    @Test
    public void format_1WindowWith5Measurements_outputHas5Columns() {
        // Arrange
        int measurementCount = 5;
        List<Segment> segments = get1WindowWithMeasurements(measurementCount);

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(measurementCount, output.getNumberOfColumns());
    }

    @Test
    public void format_1WindowWith1Measurements_outputHasCorrectX() {
        // Arrange
        List<Segment> segments = new LinkedList<Segment>();
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        double x = 1.2;
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurement.setX(x);
        measurements.add(measurement);
        segments.add(new Segment(measurements));

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(x, output.getX().get(0, 0), errorMargin);
    }

    @Test
    public void format_1WindowWith1Measurements_outputHasCorrectY() {
        // Arrange
        List<Segment> segments = new LinkedList<Segment>();
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        double y = 1.4;
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurement.setY(y);
        measurements.add(measurement);
        segments.add(new Segment(measurements));

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(y, output.getY().get(0, 0), errorMargin);
    }

    @Test
    public void format_1WindowWith1Measurements_outputHasCorrectZ() {
        // Arrange
        List<Segment> segments = new LinkedList<Segment>();
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        double z = 1.5;
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurement.setZ(z);
        measurements.add(measurement);
        segments.add(new Segment(measurements));

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(z, output.getZ().get(0, 0), errorMargin);
    }

    @Test
    public void format_1WindowWith1Measurements_outputHasCorrectGpsSpeed() {
        // Arrange
        List<Segment> segments = new LinkedList<Segment>();
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        double gpsSpeed = 1.7;
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurements.add(measurement);
        Segment segment = new Segment(measurements);
        segment.setGpsSpeed(gpsSpeed);
        segments.add(segment);

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(gpsSpeed, output.getGpsSpeed().get(0, 0), errorMargin);
    }

    @Test
    public void format_1WindowWith1Measurements_outputHasCorrectDeviceId() {
        // Arrange
        List<Segment> segments = new LinkedList<Segment>();
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        int deviceId = 7;
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurements.add(measurement);
        Segment segment = new Segment(measurements);
        segment.setDeviceId(deviceId);
        segments.add(segment);

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(deviceId, output.getDeviceId()[0][0], errorMargin);
    }

    @Test
    public void format_1WindowWith1Measurements_outputHasCorrectTimeStamp() {
        // Arrange
        List<Segment> segments = new LinkedList<Segment>();
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurements.add(measurement);
        Segment segment = new Segment(measurements);
        DateTime expected = new DateTime(2015, 1, 29, 16, 27, 30);
        segment.setTimeStamp(expected);
        segments.add(segment);

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(expected, output.getTimeStamp()[0][0]);
    }

    @Test
    public void format_gpsRecordSegment_correctAltitude() {
        // Arrange
        List<Segment> segments = new LinkedList<>();
        GpsRecord mainGpsRecord = new GpsRecord(15, new DateTime(2014, 8, 14, 11, 44, 30));
        double expected = 156;
        mainGpsRecord.setAltitude(expected);
        segments.add(new Segment(mainGpsRecord, new LinkedList<GpsRecord>(), new LinkedList<GpsRecord>()));

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(expected, output.getAltitude().get(0, 0), errorMargin);
    }

    @Test
    public void format_gpsRecordSegment_correctLongitude() {
        // Arrange
        List<Segment> segments = new LinkedList<>();
        GpsRecord main = new GpsRecord(15, new DateTime(2014, 8, 14, 11, 44, 30));
        double expected = 187;
        main.setLongitude(expected);
        segments.add(new Segment(main, new LinkedList<GpsRecord>(), new LinkedList<GpsRecord>()));

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(expected, output.getLongitude().get(0, 0), errorMargin);
    }

    @Test
    public void format_gpsRecordSegment_correctLatitude() {
        // Arrange
        List<Segment> segments = new LinkedList<>();
        GpsRecord main = new GpsRecord(15, new DateTime(2014, 8, 14, 11, 44, 30));
        double expected = 323;
        main.setLatitude(expected);
        segments.add(new Segment(main, new LinkedList<GpsRecord>(), new LinkedList<GpsRecord>()));

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(expected, output.getLatitude().get(0, 0), errorMargin);
    }

    @Test
    public void format_gpsRecordSegment_correctTimeStamp() {
        // Arrange
        List<Segment> segments = new LinkedList<>();
        GpsRecord main = new GpsRecord(15, new DateTime(2014, 8, 14, 11, 44, 30));
        DateTime expected = new DateTime(2014, 8, 14, 14, 53, 30);
        main.setTimeStamp(expected);
        segments.add(new Segment(main, new LinkedList<GpsRecord>(), new LinkedList<GpsRecord>()));

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(expected, output.getTimeStamp()[0][0]);
    }

    @Test
    public void format_gpsRecordSegment_correctId() {
        // Arrange
        List<Segment> segments = new LinkedList<>();
        int expected = 15;
        GpsRecord main = new GpsRecord(expected, new DateTime(2014, 8, 14, 11, 44, 30));
        segments.add(new Segment(main, new LinkedList<GpsRecord>(), new LinkedList<GpsRecord>()));

        // Act
        FormattedSegments output = segmentFormatter.format(segments);

        // Assert
        assertEquals(expected, output.getDeviceId()[0][0]);
    }

    @Before
    public void setUp() {
        segmentFormatter = new SegmentFormatter();
    }

    private List<Segment> get1WindowWithMeasurements(int measurementCount) {
        List<Segment> segments = new LinkedList<Segment>();
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < measurementCount; i++) {
            IndependentMeasurement measurement = new IndependentMeasurement();
            measurements.add(measurement);
        }
        segments.add(new Segment(measurements));
        return segments;
    }
}
