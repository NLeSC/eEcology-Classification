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
        setCustomFeatureExpression(" 8 + 14 ");
        FormattedSegments segments = getFormattedSegments();

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(22, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_gpsSpeedVariableExpression_correctAnswer() {
        // Arrange
        setCustomFeatureExpression(" 8+ speed1 ");
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
        setCustomFeatureExpression(" 8+ undefinedVariableasdf ");
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
        setCustomFeatureExpression(" 8+ speed1");
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
        setCustomFeatureExpression("x1*3");
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
        setCustomFeatureExpression("12 * y3+1");
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
        setCustomFeatureExpression("z2^2");
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
        setCustomFeatureExpression("meanx");
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
        setCustomFeatureExpression("stdy");
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
        setCustomFeatureExpression("long2");
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
        setCustomFeatureExpression("lat1");
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
        setCustomFeatureExpression("meanalt");
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

    @Test
    public void extractFeature_pressure_correctAnswer() {
        // Arrange
        setCustomFeatureExpression("meanpres");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] pressureData = new double[][] { { 2, 3 } };
        int[][] id = new int[][] { { 538 } };
        DateTime[][] timeData = new DateTime[1][1];
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, gpsData,
                pressureData, gpsData, gpsData, gpsData, gpsData, gpsData, gpsData, gpsData, id, timeData);
        double expected = 2.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_temperature_correctAnswer() {
        // Arrange
        setCustomFeatureExpression("meantemp");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] temperatureData = new double[][] { { 2, 3 } };
        DateTime[][] timeData = new DateTime[1][1];
        int[][] id = new int[][] { { 538 } };
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, gpsData, gpsData,
                temperatureData, gpsData, gpsData, gpsData, gpsData, gpsData, gpsData, id, timeData);
        double expected = 2.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_satellitesUsed_correctAnswer() {
        // Arrange
        setCustomFeatureExpression("meansat");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] satellitesData = new double[][] { { 2, 3 } };
        DateTime[][] timeData = new DateTime[1][1];
        int[][] id = new int[][] { { 538 } };
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, gpsData, gpsData,
                gpsData, satellitesData, gpsData, gpsData, gpsData, gpsData, gpsData, id, timeData);
        double expected = 2.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_gpsFixTime_correctAnswer() {
        // Arrange
        setCustomFeatureExpression("meanfixtime");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] gpsFixData = new double[][] { { 2, 3 } };
        DateTime[][] timeData = new DateTime[1][1];
        int[][] id = new int[][] { { 538 } };
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, gpsData, gpsData,
                gpsData, gpsData, gpsFixData, gpsData, gpsData, gpsData, gpsData, id, timeData);
        double expected = 2.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_speed2d_correctAnswer() {
        // Arrange
        setCustomFeatureExpression("meanspeed2d");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] speed2Data = new double[][] { { 2, 3 } };
        DateTime[][] timeData = new DateTime[1][1];
        int[][] id = new int[][] { { 538 } };
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, gpsData, gpsData,
                gpsData, gpsData, gpsData, speed2Data, gpsData, gpsData, gpsData, id, timeData);
        double expected = 2.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_speed3d_correctAnswer() {
        // Arrange
        setCustomFeatureExpression("meanspeed3d");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] speed3Data = new double[][] { { 2, 3 } };
        DateTime[][] timeData = new DateTime[1][1];
        int[][] id = new int[][] { { 538 } };
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, gpsData, gpsData,
                gpsData, gpsData, gpsData, gpsData, speed3Data, gpsData, gpsData, id, timeData);
        double expected = 2.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_direction_correctAnswer() {
        // Arrange
        setCustomFeatureExpression("meandir");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] directionData = new double[][] { { 2, 3 } };
        DateTime[][] timeData = new DateTime[1][1];
        int[][] id = new int[][] { { 538 } };
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, gpsData, gpsData,
                gpsData, gpsData, gpsData, gpsData, gpsData, directionData, gpsData, id, timeData);
        double expected = 2.5;

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(segments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeature_altitudeAboveGround_correctAnswer() {
        // Arrange
        setCustomFeatureExpression("meanagl");
        double[][] gpsData = new double[][] { { 0 } };
        double[][] accData = new double[][] { { 0 } };
        double[][] altitudeAboveGroundData = new double[][] { { 2, 3 } };
        DateTime[][] timeData = new DateTime[1][1];
        int[][] id = new int[][] { { 538 } };
        FormattedSegments segments = getFormattedSegments(accData, accData, accData, gpsData, gpsData, gpsData, gpsData, gpsData,
                gpsData, gpsData, gpsData, gpsData, gpsData, gpsData, altitudeAboveGroundData, id, timeData);
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

    private void setCustomFeatureExpression(String expression) {
        CustomFeatureExtractor customFeatureExtractor = (CustomFeatureExtractor) featureExtractor;
        customFeatureExtractor.setExpression(expression);
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
        double[][] emptyGpsRecordData = getEmptyArrayWithSameDimensions(xData);
        DateTime[][] emptyTimeStampData = new DateTime[xData.length][];
        return getFormattedSegments(xData, yData, zData, gpsData, emptyGpsRecordData, emptyGpsRecordData, emptyGpsRecordData,
                emptyTimeStampData);
    }

    private double[][] getEmptyArrayWithSameDimensions(double[][] xData) {
        double[][] emptyGpsRecordData = new double[xData.length][xData[0].length];
        return emptyGpsRecordData;
    };

    private FormattedSegments getFormattedSegments(double[][] xData, double[][] yData, double[][] zData, double[][] gpsSpeedData,
            double[][] longitudeData, double[][] latitudeData, double[][] altitudeData, DateTime[][] timeStamp) {
        double[][] emptyGpsRecordData = new double[xData.length][xData[0].length];
        int[][] emptyGpsRecordIdData = new int[xData.length][xData[0].length];
        return getFormattedSegments(xData, yData, zData, gpsSpeedData, longitudeData, latitudeData, altitudeData,
                emptyGpsRecordData, emptyGpsRecordData, emptyGpsRecordData, emptyGpsRecordData, emptyGpsRecordData,
                emptyGpsRecordData, emptyGpsRecordData, emptyGpsRecordData, emptyGpsRecordIdData, timeStamp);
    }

    private FormattedSegments getFormattedSegments(double[][] xData, double[][] yData, double[][] zData, double[][] gpsSpeedData,
            double[][] longitudeData, double[][] latitudeData, double[][] altitudeData, double[][] pressureData,
            double[][] temperatureData, double[][] satellitesUsedData, double[][] gpsFixTimeData, double[][] speed2dData,
            double[][] speed3dData, double[][] directionData, double[][] altitudeAboveGroundData, int[][] deviceId,
            DateTime[][] timeStamp) {
        DoubleMatrix x = new DoubleMatrix(xData);
        DoubleMatrix y = new DoubleMatrix(yData);
        DoubleMatrix z = new DoubleMatrix(zData);
        DoubleMatrix gpsSpeed = new DoubleMatrix(gpsSpeedData);
        DoubleMatrix latitude = new DoubleMatrix(latitudeData);
        DoubleMatrix longitude = new DoubleMatrix(longitudeData);
        DoubleMatrix altitude = new DoubleMatrix(altitudeData);
        DoubleMatrix pressure = new DoubleMatrix(pressureData);
        DoubleMatrix temperature = new DoubleMatrix(temperatureData);
        DoubleMatrix satellitesUsed = new DoubleMatrix(satellitesUsedData);
        DoubleMatrix gpsFixTime = new DoubleMatrix(gpsFixTimeData);
        DoubleMatrix speed2d = new DoubleMatrix(speed2dData);
        DoubleMatrix speed3d = new DoubleMatrix(speed3dData);
        DoubleMatrix direction = new DoubleMatrix(directionData);
        DoubleMatrix altitudeAboveGround = new DoubleMatrix(altitudeAboveGroundData);
        FormattedSegments segments = createFormattedSegments(x, y, z, gpsSpeed, latitude, longitude, altitude, pressure,
                temperature, satellitesUsed, gpsFixTime, speed2d, speed3d, direction, altitudeAboveGround, deviceId, timeStamp);
        return segments;
    }

}