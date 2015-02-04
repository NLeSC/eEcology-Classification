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
    private int[][] deviceId;

    public FormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed, DateTime[][] timeStamp,
            int[][] deviceId) {
        this(x, y, z, gpsSpeed, new DoubleMatrix(x.rows, 0), new DoubleMatrix(x.rows, 0), new DoubleMatrix(x.rows, 0), timeStamp,
                deviceId);
    }

    public FormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed, DoubleMatrix latitude,
            DoubleMatrix longitude, DoubleMatrix altitude, DateTime[][] timeStamp, int[][] deviceId) {
        setX(x);
        setY(y);
        setZ(z);
        setGpsSpeed(gpsSpeed);
        setLatitude(latitude);
        setLongitude(longitude);
        setAltitude(altitude);
        setTimeStamp(timeStamp);
        setDeviceId(deviceId);
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

    public int[][] getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int[][] deviceId) {
        this.deviceId = deviceId;
    }

    public void setTimeStamp(DateTime[][] timeStamp) {
        this.timeStamp = timeStamp;
    }

}
