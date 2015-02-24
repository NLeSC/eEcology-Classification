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
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordSegmenter;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentFactory;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class GpsRecordSegmenterTest {

    private GpsRecordSegmenter segmenter;

    @Test
    public void getLabeledSegments__argumentNull_noExceptionThrown() {
        // Act
        segmenter.createLabeledSegments(null);
    }

    @Test
    public void getLabeledSegments_zeroInputs_zerosegments() {
        // Arrange
        List<GpsRecord> input = new LinkedList<GpsRecord>();

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void getLabeledSegments_ZeroInputs_zerosegments() {
        // Arrange
        List<GpsRecord> input = new LinkedList<GpsRecord>();

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void getLabeledSegments_labeledInput_correctNumberOfResults() {
        // Arrange
        int n = 12;
        List<GpsRecord> input = getLabeledRecordsWithDeviceIdAndConsecutiveTimeStamps(3, n);
        segmenter.setSegmentSize(5);

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(2, output.size());
    }

    @Test
    public void getLabeledSegments_unlabeledInput_0results() {
        // Arrange
        List<GpsRecord> input = getUnlabeledRecordsWithConsecutiveTimeStamps(12);
        segmenter.setSegmentSize(5);

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void getLabeledSegments_labeledButIncompatibleInput_0resuls() {
        // Arrange
        int segmentSize = 5;
        int underSegmentSize = 4;
        int justOverSegmentSize = 6;
        LinkedList<GpsRecord> input = new LinkedList<GpsRecord>();
        input.addAll(getLabeledRecordsWithDeviceIdAndConsecutiveTimeStamps(3, underSegmentSize));
        input.addAll(getLabeledRecordsWithDeviceIdAndConsecutiveTimeStamps(18, underSegmentSize));
        input.addAll(getLabeledRecordsWithDeviceIdAndConsecutiveTimeStamps(21, justOverSegmentSize));
        input.addAll(getLabeledRecordsWithDeviceIdAndConsecutiveTimeStamps(18, underSegmentSize));

        segmenter.setSegmentSize(segmentSize);

        // Act
        List<Segment> labeledSegments = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(1, labeledSegments.size());
    }

    @Test
    public void createSegmentsIgnoringLabel_withOverlap_correctNumberOfResuls() {
        // Arrange
        int segmentSize = 5;
        int totalRecords = 10;
        int expectedSegments = 6;

        segmenter.setAllowOverlap(true);
        LinkedList<GpsRecord> input = new LinkedList<GpsRecord>();
        input.addAll(getUnlabeledRecordsWithConsecutiveTimeStamps(totalRecords));
        segmenter.setSegmentSize(segmentSize);

        // Act
        List<Segment> labeledSegments = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(expectedSegments, labeledSegments.size());
    }

    @Test
    public void getLabeledSegments_labeledInput_segmentIsLabeled() {
        // Arrange
        int n = 1;
        List<GpsRecord> input = getLabeledRecordsWithDeviceIdAndConsecutiveTimeStamps(3, n);
        segmenter.setSegmentSize(1);

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertTrue(output.get(0).isLabeled());
    }

    @Before
    public void setUp() {
        segmenter = new GpsRecordSegmenter();
        segmenter.setSegmentSize(5);
        mockStuff();
    }

    private void mockStuff() {
        SegmentFactory segmentFactory = new SegmentFactory();
        SchemaProvider labelMapReader = createNiceMock(SchemaProvider.class);
        expect(labelMapReader.getSchema()).andReturn(getMap()).anyTimes();
        replay(labelMapReader);
        segmentFactory.setLabelMapReader(labelMapReader);
        segmenter.setSegmentFactory(segmentFactory);
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

    private List<GpsRecord> getLabeledRecordsWithDeviceIdAndConsecutiveTimeStamps(int deviceId, int n) {
        List<GpsRecord> input = new LinkedList<GpsRecord>();
        for (int i = 0; i < n; i++) {
            DateTime timeStamp = new DateTime(2014, 8, 13, 13, 26, 30).plusMinutes(i);
            GpsRecord gpsRecord = new GpsRecord(deviceId, timeStamp);
            gpsRecord.setLabel(3);
            input.add(gpsRecord);
        }
        return input;
    }

    private List<GpsRecord> getUnlabeledRecordsWithConsecutiveTimeStamps(int n) {
        List<GpsRecord> input = new LinkedList<GpsRecord>();
        int id = 184;
        for (int i = 0; i < n; i++) {
            DateTime timeStamp = new DateTime(2014, 8, 13, 13, 26, 30).plusMinutes(i);
            input.add(new GpsRecord(id, timeStamp));
        }
        return input;
    }
}
