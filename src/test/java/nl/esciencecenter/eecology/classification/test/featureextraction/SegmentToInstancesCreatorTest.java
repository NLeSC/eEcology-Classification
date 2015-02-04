package nl.esciencecenter.eecology.classification.test.featureextraction;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractorFactory;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.InstancesCreator;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentFormatter;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class SegmentToInstancesCreatorTest {
    private SegmentFormatter segmentFormatter;
    private InstancesCreator instancesCreator;
    private FeatureExtractorFactory featureExtractorFactory;
    private final double delta = 0.0001;
    private SegmentToInstancesCreator segmentToInstancesCreator;

    @Test
    public void segment_1Input_segmentHasCorrectFeatures() {
        List<Segment> segments = new LinkedList<Segment>();
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        segments.add(segment);
        DoubleMatrix featuresIn = new DoubleMatrix(new double[][] { { 6 } });

        setDependencyExpectationsAndLetFeatureExtractorReturnFeaturesIn(segments, featuresIn);

        // Act
        segmentToInstancesCreator.createInstancesAndUpdateSegments(segments);
        double[] featuresOut = segment.getFeatures();

        // Assert
        assertTrue(featuresIn.sub(new DoubleMatrix(featuresOut)).norm1() < delta);
    }

    @Test
    public void segment_1Input_segmentHasFeatures() {
        List<Segment> segments = new LinkedList<Segment>();
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        segments.add(segment);
        DoubleMatrix featuresIn = new DoubleMatrix(new double[][] { { 6 } });

        setDependencyExpectationsAndLetFeatureExtractorReturnFeaturesIn(segments, featuresIn);

        // Act
        segmentToInstancesCreator.createInstancesAndUpdateSegments(segments);

        // Assert
        assertTrue(segment.hasFeatures());
    }

    @Test
    public void segment_1Input_segmentHasFeatureNames() {
        List<Segment> segments = new LinkedList<Segment>();
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        segments.add(segment);
        DoubleMatrix featuresIn = new DoubleMatrix(new double[][] { { 6 } });

        setDependencyExpectationsAndLetFeatureExtractorReturnFeaturesIn(segments, featuresIn);

        // Act
        segmentToInstancesCreator.createInstancesAndUpdateSegments(segments);

        // Assert
        assertTrue(segment.getFeatureNames() != null);
    }

    private void setDependencyExpectationsAndLetFeatureExtractorReturnFeaturesIn(List<Segment> segments, DoubleMatrix featuresIn) {
        DoubleMatrix m = new DoubleMatrix(0, 0);
        FormattedSegments formattedSegments = new FormattedSegments(m, m, m, m, new DateTime[0][0], new int[0][0]);
        expect(segmentFormatter.format(segments)).andReturn(formattedSegments);
        replay(segmentFormatter);

        FeatureExtractor featureExtractor = createNiceMock(FeatureExtractor.class);
        expect(featureExtractor.extractFeatures(isA(FormattedSegments.class))).andReturn(featuresIn);
        expect(featureExtractor.getColumnNames()).andReturn(new LinkedList<String>()).anyTimes();
        replay(featureExtractor);

        featureExtractorFactory = createNiceMock(FeatureExtractorFactory.class);
        expect(featureExtractorFactory.getFeatureExtractor()).andReturn(featureExtractor);
        replay(featureExtractorFactory);

        segmentToInstancesCreator = new SegmentToInstancesCreator(segmentFormatter, instancesCreator, featureExtractorFactory);
    }

    @Before
    public void setUp() {
        segmentFormatter = createNiceMock(SegmentFormatter.class);
        instancesCreator = createNiceMock(InstancesCreator.class);
        featureExtractorFactory = createNiceMock(FeatureExtractorFactory.class);
        segmentToInstancesCreator = new SegmentToInstancesCreator(segmentFormatter, instancesCreator, featureExtractorFactory);
    }
}
