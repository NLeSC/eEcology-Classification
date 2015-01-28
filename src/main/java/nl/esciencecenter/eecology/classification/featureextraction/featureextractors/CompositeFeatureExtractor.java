package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

/***
 * A composite feature extractor.
 * 
 * @author christiaan
 * 
 */
public class CompositeFeatureExtractor extends FeatureExtractor {
    private final List<FeatureExtractor> childFeatureExtractors = new LinkedList<FeatureExtractor>();
    private final String name = "composite";

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix features = new DoubleMatrix(formattedSegments.getNumberOfRows(), 0);
        for (FeatureExtractor featureExtractor : childFeatureExtractors) {
            DoubleMatrix newFeatures = featureExtractor.extractFeatures(formattedSegments);
            features = DoubleMatrix.concatHorizontally(features, newFeatures);
        }
        return features;
    }

    /**
     * Add a child feature extractor that will be executed when extractFeatures on the batch extractor is called.
     * 
     * @param FeatureExtractor
     */
    public void addChild(FeatureExtractor FeatureExtractor) {
        childFeatureExtractors.add(FeatureExtractor);
    }

    public List<FeatureExtractor> getChildFeatureExtractors() {
        return childFeatureExtractors;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getColumnNames() {
        LinkedList<String> columnNames = new LinkedList<String>();
        for (FeatureExtractor extractor : childFeatureExtractors) {
            columnNames.addAll(extractor.getColumnNames());
        }
        return columnNames;
    }
}
