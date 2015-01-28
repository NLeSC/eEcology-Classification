package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.segmentloading.FixedNumberDatasetFilter;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.NotEnoughSegmentsException;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.junit.Before;
import org.junit.Test;

public class FixedNumberDatasetFilterTest {
    private FixedNumberDatasetFilter fixedNumberDatasetFilter;

    @Test
    public void filter_correctTotalNumber() {
        // Arrange
        List<Segment> segments = getSegmentsWithLabels(5, 1);
        segments.addAll(getSegmentsWithLabels(5, 2));
        segments.addAll(getSegmentsWithLabels(5, 3));
        fixedNumberDatasetFilter.setTrainInstancesNumberPerClass("1:1,2:1,3:3");

        // Act
        List<Segment> output = fixedNumberDatasetFilter.filter(segments);

        // Assert
        assertEquals(5, output.size());
    }

    @Test
    public void filter_correctNumberOfFirstClass() {
        // Arrange
        List<Segment> segments = getSegmentsWithLabels(30, 5);
        segments.addAll(getSegmentsWithLabels(30, 2));
        fixedNumberDatasetFilter.setTrainInstancesNumberPerClass("5:14,2:6");

        // Act
        List<Segment> output = fixedNumberDatasetFilter.filter(segments);

        // Assert
        int counter = 0;
        for (Segment segment : output) {
            if (segment.getLabel() == 5) {
                counter++;
            }
        }
        assertEquals(14, counter);
    }

    @Test
    public void filter_randomSpacesInSetting_correctTotalNumber() {
        // Arrange
        List<Segment> segments = getSegmentsWithLabels(10, 8);
        segments.addAll(getSegmentsWithLabels(10, 2));
        segments.addAll(getSegmentsWithLabels(10, 3));
        fixedNumberDatasetFilter.setTrainInstancesNumberPerClass("   8: 1, 2:1,3  :  3 ");

        // Act
        List<Segment> output = fixedNumberDatasetFilter.filter(segments);

        // Assert
        assertEquals(5, output.size());
    }

    @Test(expected = NotEnoughSegmentsException.class)
    public void filter_tooFewSegments_correctTotalNumber() {
        // Arrange
        List<Segment> segments = getSegmentsWithLabels(10, 8);
        segments.addAll(getSegmentsWithLabels(10, 2));
        segments.addAll(getSegmentsWithLabels(10, 3));
        fixedNumberDatasetFilter.setTrainInstancesNumberPerClass("8:5,2:12,3:4");

        // Act
        List<Segment> output = fixedNumberDatasetFilter.filter(segments);

        // Assert
        assertEquals(5, output.size());
    }

    @Test
    public void filter_someLabelsPresentNotRequired_dontThrow() {
        // Arrange
        List<Segment> segments = getSegmentsWithLabels(10, 8);
        segments.addAll(getSegmentsWithLabels(10, 2));
        segments.addAll(getSegmentsWithLabels(10, 3));
        fixedNumberDatasetFilter.setTrainInstancesNumberPerClass("3:4");

        // Act
        List<Segment> output = fixedNumberDatasetFilter.filter(segments);

        // Assert
    }

    private Class<? extends Throwable> NotEnoughSegmentsException() {
        return null;
    }

    @Before
    public void setUp() {
        fixedNumberDatasetFilter = new FixedNumberDatasetFilter();
    }

    private List<Segment> getSegmentsWithLabels(int total, int labelId) {
        List<Segment> segments = new LinkedList<Segment>();
        for (int i = 0; i < total; i++) {
            Segment segment = getSegmentWithLabel(labelId);
            segments.add(segment);
        }
        return segments;
    }

    private Segment getSegmentWithLabel(int labelId) {
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        LabelDetail labelDetail = new LabelDetail();
        labelDetail.setLabelId(labelId);
        segment.setLabelDetail(labelDetail);
        return segment;
    }
}
