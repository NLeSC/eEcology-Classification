package nl.esciencecenter.eecology.classification.segmentloading;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GpsRecord {
    private int deviceId;
    private DateTime timeStamp;
    private int firstIndex;
    private boolean hasFirstIndex;
    private double longitude;
    private double latitude;
    private double altitude;
    private double gpsSpeed;
    private List<Measurement> measurements;
    private boolean isLabeled;
    private int label;
    private double pressure;
    private double temperature;
    private int satellitesUsed;
    private double gpsFixTime;
    private double speed2d;
    private double speed3d;
    private double direction;
    private double altitudeAboveGround;

    /**
     * Constructor only used for deserialization.
     */
    @SuppressWarnings("unused")
    private GpsRecord() {
    }

    public GpsRecord(List<IndependentMeasurement> measurements) {
        this.measurements = new ArrayList<Measurement>();
        if (measurements.size() > 0 && measurements.get(0).hasIndex()) {
            setFirstIndex(measurements.get(0).getIndex());
        }
        for (IndependentMeasurement independentMeasurement : measurements) {
            this.measurements.add(new Measurement(independentMeasurement));
        }

    }

    public GpsRecord(int deviceId, DateTime timeStamp) {
        this.deviceId = deviceId;
        this.timeStamp = timeStamp;
    }

    public GpsRecord(GpsRecordDto gpsRecordDto) {
        setDeviceId(gpsRecordDto.getDeviceId());
        setTimeStamp(new DateTime(gpsRecordDto.getTimeStamp()));
        setLatitude(gpsRecordDto.getLatitude());
        setLongitude(gpsRecordDto.getLongitude());
        setAltitude(gpsRecordDto.getAltitude());
        setPressure(gpsRecordDto.getPressure());
        setTemperature(gpsRecordDto.getTemperature());
        setSatellitesUsed(gpsRecordDto.getSatellitesUsed());
        setGpsFixTime(gpsRecordDto.getGpsFixTime());
        setSpeed2d(gpsRecordDto.getSpeed2d());
        setSpeed3d(gpsRecordDto.getSpeed3d());
        setAltitudeAboveGround(gpsRecordDto.getAltitudeAboveGround());
    }

    public boolean hasFirstIndex() {
        return hasFirstIndex;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
        hasFirstIndex = true;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getGpsSpeed() {
        return gpsSpeed;
    }

    public void setGpsSpeed(double gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getSatellitesUsed() {
        return satellitesUsed;
    }

    public void setSatellitesUsed(int satellitesUsed) {
        this.satellitesUsed = satellitesUsed;
    }

    public double getGpsFixTime() {
        return gpsFixTime;
    }

    public void setGpsFixTime(double gpsFixTime) {
        this.gpsFixTime = gpsFixTime;
    }

    public double getSpeed2d() {
        return speed2d;
    }

    public void setSpeed2d(double speed2d) {
        this.speed2d = speed2d;
    }

    public double getSpeed3d() {
        return speed3d;
    }

    public void setSpeed3d(double speed3d) {
        this.speed3d = speed3d;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getAltitudeAboveGround() {
        return altitudeAboveGround;
    }

    public void setAltitudeAboveGround(double altitudeAboveGround) {
        this.altitudeAboveGround = altitudeAboveGround;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    @JsonIgnore
    public boolean isLabeled() {
        return isLabeled;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
        isLabeled = true;
    }
}