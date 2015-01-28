package nl.esciencecenter.eecology.classification.segmentloading;

import org.joda.time.DateTime;

/**
 * A single data point. This object holds all the information that is required to interpret this data point. This includes a
 * single sensor reading for x, y and z. Measurements can be grouped together to form a window of measurements. Classification is
 * done on windows, not on single measurements.
 * 
 * @author Christiaan Meijer, NLeSc *
 * 
 */
public class IndependentMeasurement extends Measurement {

    private int deviceId;
    private DateTime timeStamp = new DateTime(0);
    private int label;
    private boolean isLabeled;
    private double longitude;
    private double latitude;
    private double altitude;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int i) {
        deviceId = i;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
        isLabeled = true;
    }

    public boolean isLabeled() {
        return isLabeled;
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

}
