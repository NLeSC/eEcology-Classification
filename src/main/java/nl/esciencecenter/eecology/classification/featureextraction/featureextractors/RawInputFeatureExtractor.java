package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import static org.jblas.DoubleMatrix.concatHorizontally;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Calculates the correlations between xy, yz and xz for each row and adds these 3 values to that rows features.
 * 
 * @author christiaan
 * 
 */
public class RawInputFeatureExtractor extends FeatureExtractor {
    @Inject
    @Named("measurement_segment_size")
    private int segmentSize;

    private final String name = "raw";

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix result = concatHorizontally(dataForInstances.getX(),
                concatHorizontally(dataForInstances.getY(), dataForInstances.getZ()));
        return result.dup();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getColumnNames() {
        LinkedList<String> columnNames = new LinkedList<String>();
        for (String dimension : new String[] { "x", "y", "z" }) {
            for (int i = 0; i < segmentSize; i++) {
                columnNames.add(dimension + i);
            }
        }

        return columnNames;
    }
}
