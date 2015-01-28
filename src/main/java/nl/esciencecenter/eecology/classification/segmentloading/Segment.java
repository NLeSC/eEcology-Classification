package nl.esciencecenter.eecology.classification.segmentloading;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A sequence of accelerometer data measurements. Together they form one segment which can be labeled automatically. A (training)
 * Instance can be created for each segment.
 *
 * @author Christiaan Meijer, NLeSc
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE)
public class Segment {

    private GpsRecord gpsRecord;
    private List<GpsRecord> lastGpsRecords;
    private List<GpsRecord> nextGpsRecords;
    private double[] features;
    private String[] featureNames;
    private boolean hasFeatures;

    private LabelDetail labelDetail;
    private boolean isLabeled = false;
    private LabelDetail predictedLabelDetail;
    private boolean hasPrediction = false;

    /**
     * Constructor only used for deserializing.
     */
    @SuppressWarnings("unused")
    private Segment() {
    }

    public Segment(List<IndependentMeasurement> measurements) {
        gpsRecord = new GpsRecord(measurements);
        lastGpsRecords = new LinkedList<>();
        nextGpsRecords = new LinkedList<>();
    }

    public Segment(GpsRecord gpsRecord, List<GpsRecord> lastGpsRecords, List<GpsRecord> nextGpsRecords) {
        this.gpsRecord = gpsRecord;
        this.lastGpsRecords = lastGpsRecords;
        this.nextGpsRecords = nextGpsRecords;
    }

    public GpsRecord getGpsRecord() {
        return gpsRecord;
    }

    public void setGpsRecord(GpsRecord gpsRecord) {
        this.gpsRecord = gpsRecord;
    }

    @JsonIgnore
    public List<Measurement> getMeasurements() {
        if (gpsRecord == null || gpsRecord.getMeasurements() == null) {
            return new LinkedList<Measurement>();
        }
        return gpsRecord.getMeasurements();
    }

    public void setLabelDetail(LabelDetail labelDetail) {
        this.labelDetail = labelDetail;
        isLabeled = labelDetail != null;
    }

    @JsonIgnore
    public int getNumberOfMeasurements() {
        return gpsRecord.getMeasurements().size();
    }

    public LabelDetail getLabelDetail() {
        return labelDetail;
    }

    @JsonIgnore
    public int getLabel() {
        if (isLabeled) {
            return labelDetail.getLabelId();
        }
        return 0;
    }

    @JsonIgnore
    public boolean isLabeled() {
        return isLabeled;
    }

    @JsonIgnore
    public boolean hasPrediction() {
        return hasPrediction;
    }

    @JsonIgnore
    public int getPredictedLabel() {
        if (hasPrediction) {
            return predictedLabelDetail.getLabelId();
        }
        return 0;
    }

    public void setPredictedLabelDetail(LabelDetail predictedLabelDetail) {
        this.predictedLabelDetail = predictedLabelDetail;
        hasPrediction = true;
    }

    public LabelDetail getPredictedLabelDetail() {
        return predictedLabelDetail;
    }

    public double[] getFeatures() {
        return features;
    }

    public String[] getFeatureNames() {
        return featureNames;
    }

    public void setFeatures(double[] features, String[] featureNames) {
        hasFeatures = true;
        this.features = features;
        this.featureNames = featureNames;
    }

    public boolean hasFeatures() {
        return hasFeatures;
    }

    public int getDeviceId() {
        return gpsRecord.getDeviceId();
    }

    public void setDeviceId(int deviceId) {
        gpsRecord.setDeviceId(deviceId);
    }

    public DateTime getTimeStamp() {
        return gpsRecord.getTimeStamp();
    }

    public void setTimeStamp(DateTime timeStamp) {
        gpsRecord.setTimeStamp(timeStamp);
    }

    public double getLongitude() {
        return gpsRecord.getLongitude();
    }

    public void setLongitude(double longitude) {
        gpsRecord.setLongitude(longitude);
    }

    public double getLatitude() {
        return gpsRecord.getLatitude();
    }

    public void setLatitude(double latitude) {
        gpsRecord.setLatitude(latitude);
    }

    public double getAltitude() {
        return gpsRecord.getAltitude();
    }

    public void setAltitude(double altitude) {
        gpsRecord.setAltitude(altitude);
    }

    public double getGpsSpeed() {
        return gpsRecord.getGpsSpeed();
    }

    public void setGpsSpeed(double gpsSpeed) {
        gpsRecord.setGpsSpeed(gpsSpeed);
    }

    public boolean hasFirstIndex() {
        return gpsRecord.hasFirstIndex();
    }

    public int getFirstIndex() {
        return gpsRecord.getFirstIndex();
    }

    public void setFirstIndex(int firstIndex) {
        gpsRecord.setFirstIndex(firstIndex);

    }

    public List<GpsRecord> getLastGpsRecords() {
        return lastGpsRecords;
    }

    public List<GpsRecord> getNextGpsRecords() {
        return nextGpsRecords;
    }
}
