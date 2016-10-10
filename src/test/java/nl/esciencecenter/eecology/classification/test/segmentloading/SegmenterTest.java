package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentFactory;
import nl.esciencecenter.eecology.classification.segmentloading.Segmenter;
import nl.esciencecenter.eecology.classification.test.dataaccess.PrinterFixture;

public class SegmenterTest {

    private Segmenter segmenter;
    private final double errorMargin = 0.00001;
    private PrinterFixture printer;

    @Test
    public void getLabeledSegments_argumentNull_noExceptionThrown() {
        // Act
        segmenter.createLabeledSegments(null);
    }

    @Test
    public void getLabeledSegments_ZeroInputs_zerosegments() {
        // Arrange
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void getLabeledSegments_1InputssegmentSize1_1segment() {
        // Arrange
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        input.add(getMeasurementWithLabel(0));
        segmenter.setSegmentSizeAndOverlap(1, 0);

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(1, output.size());
    }

    @Test
    public void getLabeledSegments_1InputssegmentSize1_segmentContainsSegment() {
        // Arrange
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = getMeasurementWithLabel(0);
        measurement.setX(1234.56);
        input.add(measurement);
        segmenter.setSegmentSizeAndOverlap(1, 0);

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(1234.56, output.get(0).getMeasurements().get(0).getX(), errorMargin);
    }

    @Test
    public void getLabeledSegments_10InputssegmentSize3_segmentSize3() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(3, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < 10; i++) {
            input.add(getMeasurementWithLabel(0));
        }

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);
        Segment segment = output.get(1);

        // Assert

        assertEquals(3, segment.getMeasurements().size());
    }

    @Test
    public void getLabeledSegments_4InputssegmentSize1_4segments() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(1, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < 4; i++) {
            input.add(getMeasurementWithLabel(0));
        }

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(4, output.size());
    }

    @Test
    public void getLabeledSegments_10InputssegmentSize2_5segments() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < 10; i++) {
            IndependentMeasurement measurement = getMeasurementWithLabel(0);
            measurement.setX(i);
            input.add(measurement);
        }

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(5, output.size());
    }

    @Test
    public void getLabeledSegments_10000InputssegmentSize20_500segments() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(20, 0);
        List<IndependentMeasurement> input = getMeasurementsWithLabel0(10000);

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(500, output.size());
    }

    @Test
    public void getLabeledSegments_2InputsWithDifferentIdssegmentSize2_0segment() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();

        input.add(getMeasurementWithDeviceId(1));
        input.add(getMeasurementWithDeviceId(2));

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void getLabeledSegments_2InputsWithDifferentLabelssegmentSize2_0segment() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();

        input.add(getMeasurementWithLabel(1));
        input.add(getMeasurementWithLabel(2));

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void getLabeledSegments_measurementWithoutLabel_noSegment() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(1, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        input.add(new IndependentMeasurement());

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void getLabeledSegments_2InputsWithDifferentTimeStampssegmentSize2_0segment() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();

        DateTime time1 = new DateTime(2014, 2, 7, 11, 49, 00);
        DateTime time2 = time1.plusHours(2);
        input.add(getLabeledMeasurementWithTimeStamp(time1));
        input.add(getLabeledMeasurementWithTimeStamp(time2));

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void getLabeledSegments_20InputsWithOnly2DifferentTimeStampsSegmentSize2_2segment() {
        // Arrange
        segmenter.setSegmentsMustHaveUniqueIdTimeStampCombination(true);
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();

        DateTime time1 = new DateTime(2014, 2, 7, 11, 49, 00);
        DateTime time2 = time1.plusHours(2);
        for (int i = 0; i < 12; i++) {
            input.add(getLabeledMeasurementWithTimeStamp(new DateTime(time1)));
        }
        for (int i = 0; i < 18; i++) {
            input.add(getLabeledMeasurementWithTimeStamp(new DateTime(time2)));
        }

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(2, output.size());
    }

    @Test
    public void getLabeledSegments_20InputsWithOnly3DifferentDeviceIdsSegmentSize2_3segment() {
        // Arrange
        segmenter.setSegmentsMustHaveUniqueIdTimeStampCombination(true);
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();

        int label = 0;
        for (int i = 0; i < 12; i++) {
            input.add(getMeasurementWithDeviceIdAndLabel(5, label));
        }

        for (int i = 0; i < 4; i++) {
            input.add(getMeasurementWithDeviceIdAndLabel(33, label));
        }

        for (int i = 0; i < 4; i++) {
            input.add(getMeasurementWithDeviceIdAndLabel(81, label));
        }

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(3, output.size());
    }

    @Test
    public void getLabeledSegments_1InputsWithLabel_segmentHasCorrectLabel() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(1, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        input.add(getMeasurementWithLabel(5));

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(5, output.get(0).getLabel());
    }

    @Test
    public void getLabeledSegments_1InputsWithDeviceId_segmentHasCorrectDeviceId() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(1, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = getMeasurementWithLabel(0);
        measurement.setDeviceId(320);
        input.add(measurement);

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(320, output.get(0).getDeviceId());
    }

    @Test
    public void getLabeledSegments_1InputsWithTimeStamp_segmentHasCorrectTimeStamp() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(1, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement measurement = getMeasurementWithLabel(0);
        DateTime timeStamp = new DateTime(2014, 3, 2, 23, 59, 14);
        measurement.setTimeStamp(timeStamp);
        input.add(measurement);

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(timeStamp, output.get(0).getTimeStamp());
    }

    @Test
    public void getLabeledSegments_1labeled1UnlabeledSize2_noSegments() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        input.add(getMeasurementWithLabel(0));
        input.add(new IndependentMeasurement());

        // Act
        List<Segment> output = segmenter.createLabeledSegments(input);

        // Assert
        assertEquals(0, output.size());
    }

    @Test
    public void createSegmentsIgnoringLabel_1unlabeled_1segment() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(1, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        input.add(new IndependentMeasurement()); // unlabeled

        // Act
        List<Segment> output = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(1, output.size());
    }

    @Test
    public void createSegmentsIgnoringLabel_1unlabeled1labeledSize2_1segment() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        input.add(new IndependentMeasurement()); // unlabeled
        input.add(getMeasurementWithLabel(8));

        // Act
        List<Segment> output = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(1, output.size());
    }

    @Test
    public void createSegmentsIgnoringLabel_1labeled1unlabeledSize2_segmentIsUnlabeled() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(2, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        input.add(getMeasurementWithLabel(3));
        input.add(new IndependentMeasurement()); // unlabeled

        // Act
        List<Segment> output = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(false, output.get(0).isLabeled());
    }

    @Test
    public void createSegmentsIgnoringLabel_8measurements_getNumberOfMeasurementsCorrect() {
        // Arrange
        int segmentSize = 8;
        segmenter.setSegmentSizeAndOverlap(segmentSize, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < segmentSize; i++) {
            input.add(getMeasurementWithLabel(3));
        }

        // Act
        List<Segment> segments = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(segmentSize, segments.get(0).getNumberOfMeasurements());
    }

    @Test
    public void createSegmentsIgnoringLabel_8measurementsAndSegmentSize0_return8SegmentsOf1() {
        // Arrange
        int numberOfMeasurements = 8;
        segmenter.setSegmentSizeAndOverlap(0, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < numberOfMeasurements; i++) {
            input.add(getMeasurementWithLabel(4));
        }

        // Act
        List<Segment> segments = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(numberOfMeasurements, segments.size());
    }

    @Test
    public void createSegmentsIgnoringLabel_4measurementsWithoutIndex_segmentHasNoFirstIndex() {
        // Arrange
        int numberOfMeasurements = 4;
        segmenter.setSegmentSizeAndOverlap(4, 0);
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < numberOfMeasurements; i++) {
            input.add(getMeasurementWithLabel(4));
        }

        // Act
        List<Segment> segments = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(false, segments.get(0).hasFirstIndex());
    }

    @Test
    public void createSegmentsIgnoringLabel_4measurementsWithIndex_segmentHasFirstIndex() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(4, 0);
        List<IndependentMeasurement> input = get4LabeledMeasurementsWithIndices8through11();

        // Act
        List<Segment> segments = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(true, segments.get(0).hasFirstIndex());
    }

    @Test
    public void createSegmentsIgnoringLabel_4measurementsWithIndex_segmentHasCorrectFirstIndex() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(4, 0);
        List<IndependentMeasurement> input = get4LabeledMeasurementsWithIndices8through11();

        // Act
        List<Segment> segments = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(8, segments.get(0).getFirstIndex());
    }

    @Test
    public void setOverlapSize_segmentSize6Overlap4_getOverlap4() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(6, 4);

        // Assert
        assertEquals(4, segmenter.getOverlapSize());
    }

    @Test
    public void setOverlapSize_segmentSize6OverlapNeg1_getOverlap0() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(6, -1);

        // Assert
        assertEquals(0, segmenter.getOverlapSize());
    }

    @Test
    public void setOverlapSize_segmentSize6OverlapNeg1_warningPrinted() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(6, -1);

        // Assert
        assertEquals(1, printer.hasWarned());
    }

    @Test
    public void setOverlapSize_segmentSizeNeg1Overlap0_warningPrinted() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(-1, 0);

        // Assert
        assertEquals(1, printer.hasWarned());
    }

    @Test
    public void setOverlapSize_segmentSizeNeg2Overlap0_getSegmentSize1() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(-2, 0);

        // Assert
        assertEquals(0, segmenter.getOverlapSize());
    }

    @Test
    public void setOverlapSize_segmentSize4Overlap6_getOverlap3() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(4, 6);

        // Assert
        assertEquals(3, segmenter.getOverlapSize());
    }

    @Test
    public void createSegmentsIgnoringLabel_measurements10size4Overlap3_7segments() {
        // Arrange
        segmenter.setSegmentSizeAndOverlap(4, 3);
        List<IndependentMeasurement> input = getMeasurementsWithLabel0(10);

        // Act
        List<Segment> segments = segmenter.createSegmentsIgnoringLabel(input);

        // Assert
        assertEquals(7, segments.size());
    }

    @Before
    public void setUp() {
        segmenter = new Segmenter(0, 0);
        SegmentFactory segmentFactory = new SegmentFactory();
        SchemaProvider labelMapReader = createNiceMock(SchemaProvider.class);
        expect(labelMapReader.getSchema()).andReturn(getMap()).anyTimes();
        replay(labelMapReader);
        segmentFactory.setLabelMapReader(labelMapReader);
        segmenter.setSegmentFactory(segmentFactory);
        printer = new PrinterFixture();
        segmenter.setPrinter(printer);
    }

    private List<IndependentMeasurement> getMeasurementsWithLabel0(int numberOfMeasurements) {
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < numberOfMeasurements; i++) {
            IndependentMeasurement measurement = getMeasurementWithLabel(0);
            input.add(measurement);
        }
        return input;
    }

    private List<IndependentMeasurement> get4LabeledMeasurementsWithIndices8through11() {
        int numberOfMeasurements = 4;
        List<IndependentMeasurement> input = new LinkedList<IndependentMeasurement>();
        for (int i = 0; i < numberOfMeasurements; i++) {
            IndependentMeasurement measurement = getMeasurementWithLabel(4);
            measurement.setIndex(i + 8);
            input.add(measurement);
        }
        return input;
    }

    private IndependentMeasurement getMeasurementWithDeviceIdAndLabel(int id, int label) {
        IndependentMeasurement measurementWithDeviceId = getMeasurementWithDeviceId(id);
        measurementWithDeviceId.setLabel(label);
        return measurementWithDeviceId;
    }

    private IndependentMeasurement getMeasurementWithLabel(int label) {
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurement.setLabel(label);
        return measurement;
    }

    private IndependentMeasurement getLabeledMeasurementWithTimeStamp(DateTime dateTime) {
        IndependentMeasurement measurement = getMeasurementWithLabel(0);
        measurement.setLabel(0);
        measurement.setTimeStamp(dateTime);
        return measurement;
    }

    private IndependentMeasurement getMeasurementWithDeviceId(int id) {
        IndependentMeasurement measurement = new IndependentMeasurement();
        measurement.setDeviceId(id);
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
