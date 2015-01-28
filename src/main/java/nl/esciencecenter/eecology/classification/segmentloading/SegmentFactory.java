package nl.esciencecenter.eecology.classification.segmentloading;

import java.util.List;
import java.util.Map;

import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;

import com.google.inject.Inject;

public class SegmentFactory {
    @Inject
    private SchemaProvider labelMapReader;

    public void setLabelMapReader(SchemaProvider labelMapReader) {
        this.labelMapReader = labelMapReader;
    }

    public Segment createSegment(List<IndependentMeasurement> measurements) {
        Segment segment = new Segment(measurements);
        if (measurements.size() != 0) {
            IndependentMeasurement firstMeasurement = measurements.get(0);
            setLabel(segment, firstMeasurement, measurements);
            segment.setDeviceId(firstMeasurement.getDeviceId());
            segment.setTimeStamp(firstMeasurement.getTimeStamp());
            segment.setLongitude(firstMeasurement.getLongitude());
            segment.setLatitude(firstMeasurement.getLatitude());
            segment.setAltitude(firstMeasurement.getAltitude());
            segment.setGpsSpeed(firstMeasurement.getGpsSpeed());
        }
        return segment;
    }

    public Segment createSegmentFromGpsRecords(GpsRecord mainRecord, List<GpsRecord> lastGpsRecords,
            List<GpsRecord> nextGpsRecords) {
        return new Segment(mainRecord, lastGpsRecords, nextGpsRecords);
    }

    public Segment createSegmentFromGpsRecords(List<GpsRecord> gpsRecords) {
        int size = gpsRecords.size();
        int iMainRecord = (int) (Math.ceil(size / 2d) - 1);
        GpsRecord mainRecord = gpsRecords.get(iMainRecord);
        List<GpsRecord> lastGpsRecords = gpsRecords.subList(0, iMainRecord);
        List<GpsRecord> nextGpsRecords = gpsRecords.subList(iMainRecord + 1, size);
        Segment segment = createSegmentFromGpsRecords(mainRecord, lastGpsRecords, nextGpsRecords);
        if (mainRecord.isLabeled()) {
            setLabelDetail(segment, mainRecord.getLabel());
        }
        return segment;
    }

    private void setLabel(Segment segment, IndependentMeasurement firstMeasurement, List<IndependentMeasurement> measurements) {
        boolean isLabeled = true;
        int label = firstMeasurement.getLabel();
        for (IndependentMeasurement measurement : measurements) {
            isLabeled = isLabeled && measurement.isLabeled() && measurement.getLabel() == label;
        }
        if (isLabeled) {
            setLabelDetail(segment, firstMeasurement.getLabel());
        }
    }

    protected void setLabelDetail(Segment segment, int labelId) {
        Map<Integer, LabelDetail> map = labelMapReader.getSchema();
        segment.setLabelDetail(map.get(labelId));
    }

}
