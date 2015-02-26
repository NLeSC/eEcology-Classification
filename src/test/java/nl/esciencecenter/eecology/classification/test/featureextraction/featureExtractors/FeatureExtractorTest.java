package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.junit.Test;

public abstract class FeatureExtractorTest {
    protected FeatureExtractor featureExtractor;
    protected final double errorMargin = 0.0001;
    protected final DateTime defaultTestDateTime = new DateTime(2015, 2, 2, 13, 58, 59);
    protected final int defaultDeviceId = 0;

    @Test
    public void extractFeatures_emptyInput_emptyOutput() {
        // Arrange
        FormattedSegments input = getEmptyFormattedSegments();

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(0, output.rows);
    }

    @Test
    public void extractFeatures_1emptyInput_1Output() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = y = z = new DoubleMatrix(1, 0);
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(1, output.rows);
    }

    @Test
    public void extractFeatures_2SequenceVectors_inputNotChanged() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed, latitude, longitude, altitude;
        x = new DoubleMatrix(new double[][] { { 0, 2 }, { 8, 12 } });
        y = new DoubleMatrix(new double[][] { { 1, 5 }, { 1, 3 } });
        z = new DoubleMatrix(new double[][] { { 6, 8 }, { 5, 7 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        latitude = new DoubleMatrix(x.rows, 1);
        longitude = new DoubleMatrix(x.rows, 1);
        altitude = new DoubleMatrix(x.rows, 1);
        DateTime[][] timeStamp = getDefaultTimeStamps(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed, latitude, longitude, altitude, timeStamp);
        DoubleMatrix xDup = x.dup();
        DoubleMatrix yDup = y.dup();
        DoubleMatrix zDup = z.dup();
        DoubleMatrix gpsSpeedDup = gpsSpeed.dup();
        DoubleMatrix latitudeDup = latitude.dup();
        DoubleMatrix longitudeDup = longitude.dup();
        DoubleMatrix altitudeDup = altitude.dup();
        DateTime[][] timeStampDup = new DateTime[timeStamp.length][timeStamp[0].length];
        for (int i = 0; i < timeStampDup.length; i++) {
            for (int j = 0; j < timeStampDup[i].length; j++) {
                timeStampDup[i][j] = timeStamp[i][j];
            }
        }

        // Act
        featureExtractor.extractFeatures(input);

        // Assert
        double error = x.sub(xDup).norm2() + y.sub(yDup).norm2() + z.sub(zDup).norm2() + gpsSpeed.sub(gpsSpeedDup).norm2()
                + latitude.sub(latitudeDup).norm2() + longitude.sub(longitudeDup).norm2() + altitude.sub(altitudeDup).norm2();
        assertEquals(0, error, errorMargin);
        for (int i = 0; i < timeStampDup.length; i++) {
            for (int j = 0; j < timeStampDup[i].length; j++) {
                assertEquals(timeStampDup[i][j], timeStamp[i][j]);
            }
        }
    }

    @Test
    public void getName_isNotNull() {
        // Arrange

        // Act
        String name = featureExtractor.getName();

        // Assert
        assertTrue(name != null);
    }

    @Test
    public void getColumnNames_isNotNull() {
        // Arrange

        // Act
        List<String> names = featureExtractor.getColumnNames();

        // Assert
        assertTrue(names != null);
    }

    @Test
    public void getColumnNames_sameNumberOfNamesAsColumns() {
        // Arrange
        FormattedSegments input = getEmptyFormattedSegments();

        // Act
        List<String> names = featureExtractor.getColumnNames();
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(names.size(), output.columns);
    }

    private FormattedSegments getEmptyFormattedSegments() {
        DoubleMatrix x, y, z, gpsSpeed;
        x = y = z = new DoubleMatrix(0, 0);
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        return input;
    }

    protected FormattedSegments createFormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed) {
        int[][] deviceId = new int[gpsSpeed.rows][gpsSpeed.columns];
        DateTime[][] timeStamp = getDefaultTimeStamps(gpsSpeed.rows, gpsSpeed.columns);
        return new FormattedSegments(x, y, z, gpsSpeed, timeStamp, deviceId);
    }

    private DateTime[][] getDefaultTimeStamps(int rows, int columns) {
        DateTime[][] timeStamp = new DateTime[rows][columns];
        for (int i = 0; i < timeStamp.length; i++) {
            DateTime[] currentTimeStamp = timeStamp[i];
            for (int j = 0; j < currentTimeStamp.length; j++) {
                currentTimeStamp[j] = new DateTime(defaultTestDateTime);
            }
        }
        return timeStamp;
    }

    protected FormattedSegments createFormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed,
            DoubleMatrix latitude, DoubleMatrix longitude, DoubleMatrix altitude, DateTime[][] timeStamp) {
        int[][] deviceId = new int[gpsSpeed.rows][gpsSpeed.columns];
        DoubleMatrix pressure = new DoubleMatrix(gpsSpeed.rows, gpsSpeed.columns);
        DoubleMatrix temperature = new DoubleMatrix(gpsSpeed.rows, gpsSpeed.columns);
        DoubleMatrix satellitesUsed = new DoubleMatrix(gpsSpeed.rows, gpsSpeed.columns);
        DoubleMatrix gpsFixTime = new DoubleMatrix(gpsSpeed.rows, gpsSpeed.columns);
        DoubleMatrix speed2d = new DoubleMatrix(gpsSpeed.rows, gpsSpeed.columns);
        DoubleMatrix speed3d = new DoubleMatrix(gpsSpeed.rows, gpsSpeed.columns);
        DoubleMatrix direction = new DoubleMatrix(gpsSpeed.rows, gpsSpeed.columns);
        DoubleMatrix altitudeAboveGround = new DoubleMatrix(gpsSpeed.rows, gpsSpeed.columns);
        return createFormattedSegments(x, y, z, gpsSpeed, latitude, longitude, altitude, pressure, temperature, satellitesUsed,
                gpsFixTime, speed2d, speed3d, direction, altitudeAboveGround, deviceId, timeStamp);
    }

    protected FormattedSegments createFormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed,
            DoubleMatrix latitude, DoubleMatrix longitude, DoubleMatrix altitude, DoubleMatrix pressure,
            DoubleMatrix temperature, DoubleMatrix satellitesUsed, DoubleMatrix gpsFixTime, DoubleMatrix speed2d,
            DoubleMatrix speed3d, DoubleMatrix direction, DoubleMatrix altitudeAboveGround, int[][] deviceId,
            DateTime[][] timeStamp) {
        return new FormattedSegments(x, y, z, gpsSpeed, latitude, longitude, altitude, pressure, temperature, satellitesUsed,
                gpsFixTime, speed2d, speed3d, direction, altitudeAboveGround, timeStamp, deviceId);
    }

}