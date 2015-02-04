package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CompositeFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanLocationXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanLocationYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanLocationZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanPitchFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanRollFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.PitchFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.RollFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdLocationXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdLocationYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdLocationZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdPitchFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdRollFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class CompositeFeatureExtractorTest extends FeatureExtractorTest {
    @Test
    public void addChild() {
        // Arrange
        CompositeFeatureExtractor compositeFeatureExtractor = (CompositeFeatureExtractor) featureExtractor;
        // Act
        compositeFeatureExtractor.addChild(new MeanLocationXFeatureExtractor(new MeanExtractor()));
        compositeFeatureExtractor.addChild(new MeanLocationYFeatureExtractor(new MeanExtractor()));
        compositeFeatureExtractor.addChild(new MeanLocationZFeatureExtractor(new MeanExtractor()));
        compositeFeatureExtractor.addChild(new StdLocationXFeatureExtractor(new StdExtractor()));
        compositeFeatureExtractor.addChild(new StdLocationYFeatureExtractor(new StdExtractor()));
        compositeFeatureExtractor.addChild(new StdLocationZFeatureExtractor(new StdExtractor()));
        compositeFeatureExtractor.addChild(new MeanPitchFeatureExtractor(new PitchFeatureExtractor(), new MeanExtractor()));
        compositeFeatureExtractor.addChild(new StdPitchFeatureExtractor(new PitchFeatureExtractor(), new StdExtractor()));
        compositeFeatureExtractor.addChild(new MeanRollFeatureExtractor(new RollFeatureExtractor(), new MeanExtractor()));
        compositeFeatureExtractor.addChild(new StdRollFeatureExtractor(new RollFeatureExtractor(), new StdExtractor()));

    }

    @Test
    public void extractFeatures_2SequenceVectors1Extractor_correctOutput() {
        // Arrange
        CompositeFeatureExtractor compositeFeatureExtractor = (CompositeFeatureExtractor) featureExtractor;
        DoubleMatrix x, y, z, gpsSpeed;
        x = y = z = gpsSpeed = new DoubleMatrix(2, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        FeatureExtractor childExtractor = createMock(FeatureExtractor.class);
        expect(childExtractor.extractFeatures(input)).andReturn(expected);
        compositeFeatureExtractor.addChild(childExtractor);
        replay(childExtractor);

        // Act
        DoubleMatrix output = compositeFeatureExtractor.extractFeatures(input);
        // Assert
        double error = expected.sub(output).norm2();
        assertTrue(error < errorMargin);
    }

    @Test
    public void extractFeatures_2SequenceVectors2Extractors_correctOutput() {
        // Arrange
        CompositeFeatureExtractor compositeFeatureExtractor = (CompositeFeatureExtractor) featureExtractor;
        DoubleMatrix x, y, z, gpsSpeed;
        x = y = z = gpsSpeed = new DoubleMatrix(2, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix halveExpected = new DoubleMatrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        DoubleMatrix expected = DoubleMatrix.concatHorizontally(halveExpected, halveExpected);

        FeatureExtractor childExtractor = createMock(FeatureExtractor.class);
        expect(childExtractor.extractFeatures(input)).andReturn(halveExpected).times(2);
        compositeFeatureExtractor.addChild(childExtractor);
        compositeFeatureExtractor.addChild(childExtractor);
        replay(childExtractor);

        // Act
        DoubleMatrix output = compositeFeatureExtractor.extractFeatures(input);

        // Assert
        double error = expected.sub(output).norm2();
        assertTrue(error < errorMargin);
    }

    @Test
    public void getChildFeatureExtractors_0children_report0Children() {
        // Arrange
        CompositeFeatureExtractor compositeFeatureExtractor = (CompositeFeatureExtractor) featureExtractor;

        // Act
        List<FeatureExtractor> children = compositeFeatureExtractor.getChildFeatureExtractors();

        // Assert
        assertEquals(0, children.size());
    }

    @Test
    public void getChildFeatureExtractors_2children_report2Children() {
        // Arrange
        CompositeFeatureExtractor compositeFeatureExtractor = (CompositeFeatureExtractor) featureExtractor;
        FeatureExtractor childExtractor = createMock(FeatureExtractor.class);
        compositeFeatureExtractor.addChild(childExtractor);
        compositeFeatureExtractor.addChild(childExtractor);

        // Act
        List<FeatureExtractor> children = compositeFeatureExtractor.getChildFeatureExtractors();

        // Assert
        assertEquals(2, children.size());
    }

    @Test
    public void getColumnNames_2children_2columnNames() {
        // Arrange
        CompositeFeatureExtractor compositeFeatureExtractor = (CompositeFeatureExtractor) featureExtractor;

        FeatureExtractor childExtractor = createMock(FeatureExtractor.class);
        expect(childExtractor.getColumnNames()).andReturn(Arrays.asList(new String[] { "_" })).anyTimes();
        replay(childExtractor);

        compositeFeatureExtractor.addChild(childExtractor);
        compositeFeatureExtractor.addChild(childExtractor);

        // Act
        List<String> names = compositeFeatureExtractor.getColumnNames();

        // Assert
        assertEquals(2, names.size());
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new CompositeFeatureExtractor();
    }
}
