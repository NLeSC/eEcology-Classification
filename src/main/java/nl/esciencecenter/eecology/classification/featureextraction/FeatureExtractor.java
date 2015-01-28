package nl.esciencecenter.eecology.classification.featureextraction;

import java.util.Arrays;
import java.util.List;

import org.jblas.DoubleMatrix;

public abstract class FeatureExtractor {
    /**
     * Extracts features from the measurements given in the rows of x, y and z.
     * 
     * @param formattedSegments
     * @return
     */
    public abstract DoubleMatrix extractFeatures(FormattedSegments formattedSegments);

    /**
     * Gets the name of the feature. This name is used to identify the feature by in the config/properties file.
     * 
     * @return
     */
    public abstract String getName();

    /**
     * Gets a descriptive name for each of the columns of features that are returned.
     * 
     * @return
     */
    public List<String> getColumnNames() {
        return Arrays.asList(new String[] { getName() });
    }

}