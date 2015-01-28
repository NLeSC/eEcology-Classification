package nl.esciencecenter.eecology.classification.segmentloading;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Segments a group of gps recordings into windows. The size of each window can be set. The windows are non-overlapping. Windows
 * are homogeneous, meaning they can only hold measurements belonging to the same device, taken at the same time, and given the
 * same label.
 *
 * @author Christiaan Meijer, NLeSc
 *
 */
public class GpsRecordSegmenter {
    @Inject
    private SegmentFactory segmentFactory;

    @Inject
    @Named("gps_segment_size")
    private int segmentSize;

    private Set<String> uniqueIdTimeStamps;

    @Inject
    public GpsRecordSegmenter() {
    }

    public void setSegmentFactory(SegmentFactory segmentFactory) {
        this.segmentFactory = segmentFactory;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = Math.max(1, segmentSize);
    }

    /**
     * Segments a group of (accelerometer data) measurements into segments. The size of each segment can be set. The segments are
     * non-overlapping. Segments are homogeneous, meaning they can only hold measurements belonging to the same device, and given
     * the same label.
     *
     * @param gpsRecords
     * @return segments
     */
    public List<Segment> createLabeledSegments(List<GpsRecord> gpsRecords) {
        List<Segment> segments = createSegmentsIgnoringLabel(gpsRecords);
        LinkedList<Segment> labeledSegments = new LinkedList<Segment>();
        for (Segment segment : segments) {
            if (segment.isLabeled()) {
                labeledSegments.add(segment);
            }
        }
        return labeledSegments;
    }

    /**
     * Segments a group of (accelerometer data) measurements into segments. The size of each segment can be set. The segments are
     * non-overlapping. Segments are homogeneous, meaning they can only hold measurements belonging to the same device, but
     * disregarding the label.
     *
     * @param gpsRecords
     * @return segments
     */
    public List<Segment> createSegmentsIgnoringLabel(List<GpsRecord> gpsRecords) {
        if (gpsRecords == null || gpsRecords.size() == 0) {
            return new LinkedList<Segment>();
        }

        return getSegmentsFromGpsRecords(gpsRecords);
    }

    private List<Segment> getSegmentsFromGpsRecords(List<GpsRecord> gpsRecords) {
        LinkedList<Segment> segments = new LinkedList<Segment>();
        LinkedList<GpsRecord> recordsToGroup = new LinkedList<GpsRecord>(gpsRecords);
        uniqueIdTimeStamps = new HashSet<String>();
        while (recordsToGroup.size() >= segmentSize) {
            List<GpsRecord> currentGroup = popSegmentCompatibleRecords(recordsToGroup);
            addNewSegmentIfCorrect(currentGroup, segments);
        }
        return segments;
    }

    private void addNewSegmentIfCorrect(List<GpsRecord> currentGroup, LinkedList<Segment> segments) {
        if (currentGroup.size() == segmentSize) {
            String idTimeStamp = getIdAndTimeStampAsString(currentGroup.get(0));
            uniqueIdTimeStamps.add(idTimeStamp);
            segments.add(segmentFactory.createSegmentFromGpsRecords(currentGroup));
        }
    }

    private String getIdAndTimeStampAsString(GpsRecord gpsRecord) {
        int deviceId = gpsRecord.getDeviceId();
        long timeStamp = gpsRecord.getTimeStamp().getMillis();
        String idTimeStamp = deviceId + ":" + timeStamp;
        return idTimeStamp;
    }

    private List<GpsRecord> popSegmentCompatibleRecords(LinkedList<GpsRecord> recordsToGroup) {
        List<GpsRecord> currentSegment = new LinkedList<GpsRecord>();
        GpsRecord firstRecord = recordsToGroup.getFirst();
        for (int i = 0; i < segmentSize; i++) {
            GpsRecord currentRecord = recordsToGroup.peek();
            if (areCompatible(firstRecord, currentRecord)) {
                currentSegment.add(recordsToGroup.pop());
            } else {
                break;
            }
        }
        return currentSegment;
    }

    private boolean areCompatible(GpsRecord firstRecord, GpsRecord currentRecord) {
        boolean deviceIdIsCompatible = currentRecord.getDeviceId() == firstRecord.getDeviceId();
        return deviceIdIsCompatible;
    }

}
