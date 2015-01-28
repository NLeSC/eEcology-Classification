package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CustomFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.InvalidExpressionException;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdExtractor;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class CustomFeatureExtractorTest extends FeatureExtractorTest {

    @Test
    public void extractFeature_simpleExpression_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression(" 8 + 14 ");
        FormattedSegments segments = getFormattedSegments();

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(22, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_gpsSpeedVariableExpression_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression(" 8+ speed1 ");
        double[][] gpsData = new double[][] { { 0.5 } };
        FormattedSegments segments = getFormattedSegments(gpsData);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(8.5, features.get(0, 0), errorMargin);
    }

    @Test(expected = InvalidExpressionException.class)
    public void extractFeature_unknownVariableExpression_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression(" 8+ undefinedVariableasdf ");
        double[][] gpsData = new double[][] { { 0.5 } };
        double[][] xData = new double[][] { { 0 } };
        FormattedSegments segments = getFormattedSegments(xData, xData, xData, gpsData);

        // Act
        featureExtractor.extractFeatures(segments);

        // Assert
    }

    @Test
    public void extractFeature_multipleSegmentsExpression_2ndHasCorrectAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression(" 8+ speed1");
        double[][] gpsData = new double[][] { { 0.5 }, { 11.0 } };
        double[][] xData = new double[][] { { 0 }, { 0 } };
        FormattedSegments segments = getFormattedSegments(xData, xData, xData, gpsData);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(19, features.get(1, 0), errorMargin);
    }

    @Test
    public void extractFeature_x0VariableExpression_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression("x1*3");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] xData = new double[][] { { 6 } };
        double[][] yData = new double[][] { { 0 } };
        double[][] zData = new double[][] { { 0 } };
        ;
        FormattedSegments segments = getFormattedSegments(xData, yData, zData, gpsData);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(18, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_y2VariableExpression_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression("12 * y3+1");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] xData = new double[][] { { 0, 0, 0 } };
        double[][] yData = new double[][] { { 5, 8, 0.5 } };
        double[][] zData = new double[][] { { 0, 0, 0 } };
        FormattedSegments segments = getFormattedSegments(xData, yData, zData, gpsData);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals((12 * 0.5) + 1, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_z1VariableExpression_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression("z2^2");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] xData = new double[][] { { 0, 0, 0 } };
        double[][] yData = new double[][] { { 0, 0, 0 } };
        double[][] zData = new double[][] { { 5, 8, 0.5 } };
        FormattedSegments segments = getFormattedSegments(xData, yData, zData, gpsData);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(Math.pow(8, 2), features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_meanxVariableExpression_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression("meanx");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] xData = new double[][] { { 2, 2, 2, 4 } }; // mean = 2.5
        double[][] yData = new double[][] { { 0, 0, 0, 0 } };
        double[][] zData = new double[][] { { 0, 0, 0, 0 } };
        FormattedSegments segments = getFormattedSegments(xData, yData, zData, gpsData);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(2.5, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_stdyVariableExpression_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression("stdy");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] xData = new double[][] { { 0, 0, 0 } };
        double[][] yData = new double[][] { { 1, 2, 3 } };
        double[][] zData = new double[][] { { 0, 0, 0 } };
        FormattedSegments segments = getFormattedSegments(xData, yData, zData, gpsData);
        double expected = Math.sqrt((1 + 0 + 1) / (3 - 1));

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_longitude_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression("long2");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] longitudeData = new double[][] { { 0.5, 4.5 } };
        DateTime[][] timeData = new DateTime[1][1];
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, longitudeData, gpsData, gpsData,
                timeData);
        double expected = 4.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_latitude_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression("lat1");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] latitudeData = new double[][] { { 1.5, 7.5 } };
        DateTime[][] timeData = new DateTime[1][1];
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, latitudeData, gpsData,
                timeData);
        double expected = 1.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_altitude_correctAnswer() {
        // Arrange
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression("meanalt");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] altitudeData = new double[][] { { 2, 3 } };
        DateTime[][] timeData = new DateTime[1][1];
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, altitudeData,
                timeData);
        double expected = 2.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        CustomFeatureExtractor customFeatureExtractor = new CustomFeatureExtractor("testFeature", "0");
        customFeatureExtractor.setMeanExtractor(new MeanExtractor());
        customFeatureExtractor.setStdExtractor(new StdExtractor());
        featureExtractor = customFeatureExtractor;
    }

    private FormattedSegments getFormattedSegments() {
        double[][] gpsData = new double[][] { { 0 } };
        return getFormattedSegments(gpsData);
    }

    private FormattedSegments getFormattedSegments(double[][] gpsData) {
        double[][] xData = new double[][] { { 0 } };
        return getFormattedSegments(xData, xData, xData, gpsData);
    }

    /**
     *
     * @param xData
     *            should at least have length 1
     * @param yData
     * @param zData
     * @param gpsData
     * @return
     */
    private FormattedSegments getFormattedSegments(double[][] xData, double[][] yData, double[][] zData, double[][] gpsData) {
        double[][] emptyGpsRecordData = new double[xData.length][xData[0].length];
        DateTime[][] emptyTimeStampData = new DateTime[xData.length][];
        return getFormattedSegments(xData, yData, zData, gpsData, emptyGpsRecordData, emptyGpsRecordData, emptyGpsRecordData,
                emptyTimeStampData);
    };

    private FormattedSegments getFormattedSegments(double[][] xData, double[][] yData, double[][] zData, double[][] gpsData,
            double[][] longitudeData, double[][] latitudeData, double[][] altitudeData, DateTime[][] timeStamp) {
        DoubleMatrix x = new DoubleMatrix(xData);
        DoubleMatrix y = new DoubleMatrix(yData);
        DoubleMatrix z = new DoubleMatrix(zData);
        DoubleMatrix gpsSpeed = new DoubleMatrix(gpsData);
        DoubleMatrix latitude = new DoubleMatrix(latitudeData);
        DoubleMatrix longitude = new DoubleMatrix(longitudeData);
        DoubleMatrix altitude = new DoubleMatrix(altitudeData);
        FormattedSegments segments = new FormattedSegments(x, y, z, gpsSpeed, latitude, longitude, altitude, timeStamp);
        return segments;
    }
}