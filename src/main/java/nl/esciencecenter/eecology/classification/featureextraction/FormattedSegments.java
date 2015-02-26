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
    private DoubleMatrix pressure;
    private DoubleMatrix temperature;
    private DoubleMatrix sattellitesUsed;
    private DoubleMatrix gpsFixTime;
    private DoubleMatrix speed2d;
    private DoubleMatrix speed3d;
    private DoubleMatrix direction;
    private DoubleMatrix altitudeAboveGround;

    private DateTime[][] timeStamp;
    private int[][] deviceId;

    public FormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed, DateTime[][] timeStamp,
            int[][] deviceId) {
        this(x, y, z, gpsSpeed, getDummyMatrix(x), getDummyMatrix(x), getDummyMatrix(x), timeStamp, deviceId);
    }

    public FormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed, DoubleMatrix latitude,
            DoubleMatrix longitude, DoubleMatrix altitude, DateTime[][] timeStamp, int[][] deviceId) {
        this(x, y, z, gpsSpeed, latitude, longitude, altitude, getDummyMatrix(x), getDummyMatrix(x), getDummyMatrix(x),
                getDummyMatrix(x), getDummyMatrix(x), getDummyMatrix(x), getDummyMatrix(x), getDummyMatrix(x), timeStamp,
                deviceId);
    }

    public FormattedSegments(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z, DoubleMatrix gpsSpeed, DoubleMatrix latitude,
            DoubleMatrix longitude, DoubleMatrix altitude, DoubleMatrix pressure, DoubleMatrix temperature,
            DoubleMatrix satellitesUsed, DoubleMatrix gpsFixtime, DoubleMatrix speed2d, DoubleMatrix speed3d,
            DoubleMatrix direction, DoubleMatrix altitudeAboveGround, DateTime[][] timeStamp, int[][] deviceId) {
        setDeviceId(deviceId);
        setTimeStamp(timeStamp);
        setX(x);
        setY(y);
        setZ(z);
        setGpsSpeed(gpsSpeed);
        setLatitude(latitude);
        setLongitude(longitude);
        setAltitude(altitude);
        setPressure(pressure);
        setTemperature(temperature);
        setSattellitesUsed(satellitesUsed);
        setGpsFixTime(gpsFixtime);
        setSpeed2d(speed2d);
        setSpeed3d(speed3d);
        setDirection(direction);
        setAltitudeAboveGround(altitudeAboveGround);
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

    public DoubleMatrix getPressure() {
        return pressure;
    }

    public void setPressure(DoubleMatrix pressure) {
        this.pressure = pressure;
    }

    public DoubleMatrix getTemperature() {
        return temperature;
    }

    public void setTemperature(DoubleMatrix temperature) {
        this.temperature = temperature;
    }

    public DoubleMatrix getSattellitesUsed() {
        return sattellitesUsed;
    }

    public void setSattellitesUsed(DoubleMatrix sattellitesUsed) {
        this.sattellitesUsed = sattellitesUsed;
    }

    public DoubleMatrix getGpsFixTime() {
        return gpsFixTime;
    }

    public void setGpsFixTime(DoubleMatrix gpsFixTime) {
        this.gpsFixTime = gpsFixTime;
    }

    public DoubleMatrix getSpeed2d() {
        return speed2d;
    }

    public void setSpeed2d(DoubleMatrix speed2d) {
        this.speed2d = speed2d;
    }

    public DoubleMatrix getSpeed3d() {
        return speed3d;
    }

    public void setSpeed3d(DoubleMatrix speed3d) {
        this.speed3d = speed3d;
    }

    public DoubleMatrix getDirection() {
        return direction;
    }

    public void setDirection(DoubleMatrix direction) {
        this.direction = direction;
    }

    public DoubleMatrix getAltitudeAboveGround() {
        return altitudeAboveGround;
    }

    public void setAltitudeAboveGround(DoubleMatrix altitudeAboveGround) {
        this.altitudeAboveGround = altitudeAboveGround;
    }

    private static DoubleMatrix getDummyMatrix(DoubleMatrix x) {
        return new DoubleMatrix(x.rows, 0);
    }

}
