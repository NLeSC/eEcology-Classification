package nl.esciencecenter.eecology.classification.featureextraction;

import java.util.List;

import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.jblas.DoubleMatrix;

import weka.core.Instances;

import com.google.inject.Inject;

public class SegmentToInstancesCreator {
    private final SegmentFormatter segmentFormatter;
    private final InstancesCreator instancesCreator;
    private final FeatureExtractorFactory featureExtractorFactory;

    @Inject
    public SegmentToInstancesCreator(SegmentFormatter segmentFormatter, InstancesCreator instancesCreator,
            FeatureExtractorFactory featureExtractorFactory) {
        this.segmentFormatter = segmentFormatter;
        this.instancesCreator = instancesCreator;
        this.featureExtractorFactory = featureExtractorFactory;
    }

    public Instances createInstancesAndUpdateSegments(List<Segment> segments) {
        FeatureExtractor featureExtractor = featureExtractorFactory.getFeatureExtractor();
        FormattedSegments formattedSegments = segmentFormatter.format(segments);
        DoubleMatrix features = featureExtractor.extractFeatures(formattedSegments);
        List<String> featureNames = featureExtractor.getColumnNames();
        updateSegmentsWithFeatures(segments, features, featureNames);
        return instancesCreator.createInstances(segments, features);
    }

    private void updateSegmentsWithFeatures(List<Segment> segments, DoubleMatrix features, List<String> featureNames) {
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).setFeatures(features.getRow(i).toArray(), featureNames.toArray(new String[featureNames.size()]));
        }
    }

}
