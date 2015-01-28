package nl.esciencecenter.eecology.classification.featureextraction;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;

public class FormattedSegments {
    private DoubleMatrix x;
    private DoubleMatrix y;
    private DoubleMatrix z;

    private DoubleMatrix gpsSpeed;
    private DoubleMatrix latitude;
    private DoubleMatrix longitude;
    private DoubleMatrix altitude;

    private DateTime[][] timeStamp;

    public FormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed) {
        this(x, y, z, gpsSpeed, new DoubleMatrix(x.rows, 0), new DoubleMatrix(x.rows, 0), new DoubleMatrix(x.rows, 0),
                new DateTime[0][0]);
    }

    public FormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed, DoubleMatrix latitude,
            DoubleMatrix longitude, DoubleMatrix altitude, DateTime[][] timeStamp) {
        setX(x);
        setY(y);
        setZ(z);
        setGpsSpeed(gpsSpeed);
        setLatitude(latitude);
        setLongitude(longitude);
        setAltitude(altitude);
        setTimeStamp(timeStamp);
    }

    public DoubleMatrix getX() {
        return x;
    }

    private void setX(DoubleMatrix x) {
        this.x = x;
    }

    public DoubleMatrix getY() {
        return y;
    }

    private void setY(DoubleMatrix y) {
        this.y = y;
    }

    public DoubleMatrix getZ() {
        return z;
    }

    private void setZ(DoubleMatrix z) {
        this.z = z;
    }

    public DoubleMatrix getGpsSpeed() {
        return gpsSpeed;
    }

    private void setGpsSpeed(DoubleMatrix gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public int getNumberOfRows() {
        return x.rows;
    }

    public DoubleMatrix getLatitude() {
        return latitude;
    }

    public void setLatitude(DoubleMatrix latitude) {
        this.latitude = latitude;
    }

    public DoubleMatrix getLongitude() {
        return longitude;
    }

    public void setLongitude(DoubleMatrix longitude) {
        this.longitude = longitude;
    }

    public DoubleMatrix getAltitude() {
        return altitude;
    }

    public void setAltitude(DoubleMatrix altitude) {
        this.altitude = altitude;
    }

    public int getNumberOfColumns() {
        return x.columns;
    }

    public DateTime[][] getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime[][] timeStamp) {
        this.timeStamp = timeStamp;
    }
}
