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