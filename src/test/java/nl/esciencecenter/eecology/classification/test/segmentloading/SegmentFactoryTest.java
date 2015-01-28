package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecord;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentFactory;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class SegmentFactoryTest {

    private SegmentFactory segmentFactory;
    private final double errorMargin = 0.00001;

    @Test
    public void createSegment_measurementsHaveDifferentLabels_segmentIsUnlabeled() {
        // Arrange
        List<IndependentMeasurement> measurements = new LinkedList<IndependentMeasurement>();
        measurements.add(getMeasurementWithLabel(2));
        measurements.add(getMeasurementWithLabel(3));

        // Act
        Segment result = segmentFactory.createSegment(measurements);

        // Assert
        assertEquals(false, result.isLabeled());
    }

    @Test
    public void segmentFactory_1InputsWithLongitude_segmentHasCorrectLongitude() {
        // Arrange
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = getMeasurementWithLabel(2);
        int longitude = 8;
        measurement.setLongitude(longitude);
        input.add(measurement);

        // Act
        Segment output = segmentFactory.createSegment(input);

        // Assert
        assertEquals(longitude, output.getLongitude(), errorMargin);
    }

    @Test
    public void segmentFactory_1InputsWithLatitude_segmentHasCorrectLatitude() {
        // Arrange
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = getMeasurementWithLabel(2);
        int latitude = 7;
        measurement.setLatitude(latitude);
        input.add(measurement);

        // Act
        Segment output = segmentFactory.createSegment(input);

        // Assert
        assertEquals(latitude, output.getLatitude(), errorMargin);
    }

    @Test
    public void segmentFactory_1InputsWithAltitude_segmentHasCorrectAltitude() {
        // Arrange
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = getMeasurementWithLabel(2);
        int altitude = 6;
        measurement.setAltitude(altitude);
        input.add(measurement);

        // Act
        Segment output = segmentFactory.createSegment(input);

        // Assert
        assertEquals(altitude, output.getAltitude(), errorMargin);
    }

    @Test
    public void segmentFactory_1InputsWithGpsSpeed_segmentHasCorrectGpsSpeed() {
        // Arrange
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = getMeasurementWithLabel(2);
        double gpsSpeed = 3;
        measurement.setGpsSpeed(gpsSpeed);
        input.add(measurement);

        // Act
        Segment output = segmentFactory.createSegment(input);

        // Assert
        assertEquals(gpsSpeed, output.getGpsSpeed(), errorMargin);
    }

    @Test
    public void segmentFactory_gpsRecords_hasCorrectDeviceId() {
        // Arrange
        DateTime timeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        int id = 184;
        GpsRecord main = new GpsRecord(id, timeStamp);
        List<GpsRecord> last = new LinkedList<GpsRecord>();
        List<GpsRecord> next = new LinkedList<GpsRecord>();

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(main, last, next);

        // Assert
        assertEquals(id, output.getDeviceId());
    }

    @Test
    public void segmentFactory_gpsRecords_nextHasCorrectTimeStamp() {
        // Arrange
        DateTime timeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        int id = 184;
        GpsRecord main = new GpsRecord(id, timeStamp);
        List<GpsRecord> last = new LinkedList<GpsRecord>();
        DateTime lastTimeStamp = timeStamp.plus(10);
        last.add(new GpsRecord(id, lastTimeStamp));
        List<GpsRecord> next = new LinkedList<GpsRecord>();

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(main, last, next);

        // Assert
        assertEquals(lastTimeStamp, output.getLastGpsRecords().get(0).getTimeStamp());
    }

    @Test
    public void segmentFactory_9gpsRecordsAsList_mainHasCorrectTimeStamp() {
        // Arrange
        DateTime mainTimeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        List<GpsRecord> all = get9RecordsWithMainTimeStamp(mainTimeStamp);

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(all);

        // Assert
        assertEquals(mainTimeStamp, output.getGpsRecord().getTimeStamp());
    }

    @Test
    public void segmentFactory_9gpsRecordsAsList_correctNumberOfLastRecords() {
        // Arrange
        DateTime mainTimeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        List<GpsRecord> all = get9RecordsWithMainTimeStamp(mainTimeStamp);

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(all);

        // Assert
        assertEquals(4, output.getLastGpsRecords().size());
    }

    @Test
    public void segmentFactory_9gpsRecordsAsList_correctNumberOfNextRecords() {
        // Arrange
        DateTime mainTimeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        List<GpsRecord> all = get9RecordsWithMainTimeStamp(mainTimeStamp);

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(all);

        // Assert
        assertEquals(4, output.getNextGpsRecords().size());
    }

    @Test
    public void segmentFactory_10gpsRecordsAsList_correctNumberOfLastRecords() {
        // Arrange
        DateTime mainTimeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        List<GpsRecord> all = get10RecordsWithMainTimeStamp(mainTimeStamp);

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(all);

        // Assert
        assertEquals(4, output.getLastGpsRecords().size());
    }

    @Test
    public void segmentFactory_10gpsRecordsAsList_correctNumberOfNextRecords() {
        // Arrange
        DateTime mainTimeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        List<GpsRecord> all = get10RecordsWithMainTimeStamp(mainTimeStamp);

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(all);

        // Assert
        assertEquals(5, output.getNextGpsRecords().size());
    }

    @Test
    public void segmentFactory_1gpsRecordsAsList_correctNumberOfNextRecords() {
        // Arrange
        DateTime mainTimeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        List<GpsRecord> all = new LinkedList<GpsRecord>();
        all.add(new GpsRecord(184, mainTimeStamp));

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(all);

        // Assert
        assertEquals(0, output.getNextGpsRecords().size());
    }

    @Test
    public void segmentFactory_1gpsRecordsWithoutLabel_resultIsUnlabeled() {
        // Arrange
        DateTime mainTimeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        List<GpsRecord> all = new LinkedList<GpsRecord>();
        all.add(new GpsRecord(184, mainTimeStamp));

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(all);

        // Assert
        assertTrue(output.isLabeled() == false);
    }

    @Test
    public void segmentFactory_1gpsRecordsWithLabel_resultIsLabeled() {
        // Arrange
        DateTime mainTimeStamp = new DateTime(2014, 8, 13, 14, 18, 30);
        List<GpsRecord> all = new LinkedList<GpsRecord>();
        GpsRecord gpsRecord = new GpsRecord(184, mainTimeStamp);
        gpsRecord.setLabel(5);
        all.add(gpsRecord);

        // Act
        Segment output = segmentFactory.createSegmentFromGpsRecords(all);

        // Assert
        assertTrue(output.isLabeled());
    }

    private List<GpsRecord> get10RecordsWithMainTimeStamp(DateTime mainTimeStamp) {
        List<GpsRecord> recordsWithMainTimeStamp = get9RecordsWithMainTimeStamp(mainTimeStamp);
        recordsWithMainTimeStamp.add(recordsWithMainTimeStamp.get(0));
        return recordsWithMainTimeStamp;
    }

    private List<GpsRecord> get9RecordsWithMainTimeStamp(DateTime mainTimeStamp) {
        int id = 184;
        GpsRecord main = new GpsRecord(id, mainTimeStamp);
        GpsRecord other = new GpsRecord(id, mainTimeStamp.plusYears(2));
        List<GpsRecord> others = new LinkedList<GpsRecord>();
        for (int i = 0; i < 4; i++) {
            others.add(other);
        }

        List<GpsRecord> all = new LinkedList<GpsRecord>();
        all.addAll(others);
        all.add(main);
        all.addAll(others);
        return all;
    }

    @Before
    public void setUp() {
        segmentFactory = new SegmentFactory();
        SchemaProvider labelMapReader = createNiceMock(SchemaProvider.class);
        expect(labelMapReader.getSchema()).andReturn(getMap()).anyTimes();
        replay(labelMapReader);
        segmentFactory.setLabelMapReader(labelMapReader);
    }

    private IndependentMeasurement getMeasurementWithLabel(int label) {
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurement.setLabel(label);
        return measurement;
    }

    private Map<Integer, LabelDetail> getMap() {
        HashMap<Integer, LabelDetail> map = new HashMap<Integer, LabelDetail>();
        for (int i = 0; i < 10; i++) {
            LabelDetail labelDetail = new LabelDetail();
            labelDetail.setLabelId(i);
            map.put(i, labelDetail);
        }
        return map;
    }
}
