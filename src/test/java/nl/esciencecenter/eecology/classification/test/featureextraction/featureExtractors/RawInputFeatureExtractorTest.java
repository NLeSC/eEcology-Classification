package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.RawInputFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

/**
 * @author christiaan
 * 
 */
public class RawInputFeatureExtractorTest extends FeatureExtractorTest {
    private final int testSegmentSize = 2;

    @Test
    public void extractFeatures_1SequenceVectors_correctOutput() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 0, 1 } });
        y = new DoubleMatrix(new double[][] { { 1, 0 } });
        z = new DoubleMatrix(new double[][] { { 2, 1 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 0, 1, 1, 0, 2, 1 } });

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        double error = expected.sub(output).norm2();
        assertTrue(error < errorMargin);
    }

    @Test
    public void getColumnNames_segmentSizeIs3_9outputs() {
        // Arrange
        setSegmentSize(3);

        // Act
        List<String> output = featureExtractor.getColumnNames();

        // Assert
        assertEquals(9, output.size());
    }

    @Override
    public void getColumnNames_sameNumberOfNamesAsColumns() {
        int segmentSize = 0;
        setSegmentSize(segmentSize);
        super.getColumnNames_sameNumberOfNamesAsColumns();
    }

    private void setSegmentSize(int segmentSize) {
        RawInputFeatureExtractor extractor = (RawInputFeatureExtractor) featureExtractor;
        extractor.setSegmentSize(segmentSize);
    };

    @Before
    public void setUp() throws Exception {
        featureExtractor = new RawInputFeatureExtractor();
    }
}
