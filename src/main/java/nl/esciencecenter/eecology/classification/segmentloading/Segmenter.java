package nl.esciencecenter.eecology.classification.segmentloading;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import nl.esciencecenter.eecology.classification.commands.Printer;

/**
 * Segments a group of (accelerometer data) measurements into windows. The size of each window can be set. Windows are
 * homogeneous, meaning they can only hold measurements belonging to the same device, taken at the same time, and given the same
 * label.
 *
 * @author Christiaan Meijer, NLeSc
 *
 */
public class Segmenter {
    @Inject
    private SegmentFactory segmentFactory;
    private Printer printer;

    @Inject
    @Named("segments_must_have_unique_id_timestamp_combination")
    private boolean segmentsMustHaveUniqueIdTimeStampCombination;

    private int overlapSize;
    private int segmentSize;
    private boolean ignoreLabel;
    private Set<String> uniqueIdTimeStamps;

    @Inject
    // Injecting sizes in constructor to be able to use setter.
    public Segmenter(@Named("measurement_segment_size") int segmentSize, @Named("accelerometer_overlap_size") int overlapSize) {
        setSegmentSizeAndOverlap(segmentSize, overlapSize);
    }

    public void setSegmentsMustHaveUniqueIdTimeStampCombination(boolean segmentsMustHaveUniqueIdTimeStampCombination) {
        this.segmentsMustHaveUniqueIdTimeStampCombination = segmentsMustHaveUniqueIdTimeStampCombination;
    }

    public void setSegmentFactory(SegmentFactory segmentFactory) {
        this.segmentFactory = segmentFactory;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setSegmentSizeAndOverlap(int segmentSize, int overlapSize) {
        this.segmentSize = Math.max(1, segmentSize);
        this.overlapSize = Math.min(this.segmentSize - 1, Math.max(0, overlapSize));
        if (this.overlapSize != overlapSize || this.segmentSize != segmentSize && printer != null) {
            printer.warn("Incorrect segment size and overlap size set: adjusted to segment size " + this.segmentSize
                    + " and overlap size " + this.overlapSize + ".");
        }
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public int getOverlapSize() {
        return overlapSize;
    }

    /**
     * Segments a group of (accelerometer data) measurements into segments. The size of each segment can be set. The segments are
     * non-overlapping. Segments are homogeneous, meaning they can only hold measurements belonging to the same device, taken at
     * the same time, and given the same label.
     *
     * @param measurements
     * @return segments
     */
    public List<Segment> createLabeledSegments(List<IndependentMeasurement> measurements) {
        ignoreLabel = false;
        List<Segment> segments = getSegments(measurements);
        List<Segment> labeledSegments = new LinkedList<Segment>();
        for (Segment segment : segments) {
            if (segment.isLabeled()) {
                labeledSegments.add(segment);
            }
        }
        return labeledSegments;

    }

    /**
     * Segments a group of (accelerometer data) measurements into segments. The size of each segment can be set. The segments are
     * non-overlapping. Segments are homogeneous, meaning they can only hold measurements belonging to the same device, taken at
     * the same time, but disregarding the label.
     *
     * @param measurements
     * @return segments
     */
    public List<Segment> createSegmentsIgnoringLabel(List<IndependentMeasurement> measurements) {
        ignoreLabel = true;
        return getSegments(measurements);
    }

    private List<Segment> getSegments(List<IndependentMeasurement> measurements) {
        if (measurements == null || measurements.size() == 0) {
            return new LinkedList<Segment>();
        }

        return getSegmentsFromMeasurements(measurements);
    }

    private List<Segment> getSegmentsFromMeasurements(List<IndependentMeasurement> measurements) {
        LinkedList<Segment> segments = new LinkedList<Segment>();
        LinkedList<IndependentMeasurement> measurementsToGroup = new LinkedList<IndependentMeasurement>(measurements);
        uniqueIdTimeStamps = new HashSet<String>();
        while (measurementsToGroup.size() >= segmentSize) {
            List<IndependentMeasurement> currentGroup = popSegmentCompatibleMeasurementsWithOverlap(measurementsToGroup);
            addNewSegmentIfCorrect(currentGroup, segments);
        }
        return segments;
    }

    private void addNewSegmentIfCorrect(List<IndependentMeasurement> currentGroup, LinkedList<Segment> segments) {
        if (currentGroup.size() == segmentSize) {
            String idTimeStamp = getIdAndTimeStampAsString(currentGroup.get(0));
            if (segmentsMustHaveUniqueIdTimeStampCombination) {
                if (uniqueIdTimeStamps.contains(idTimeStamp)) {
                    return;
                }
            }
            uniqueIdTimeStamps.add(idTimeStamp);
            segments.add(segmentFactory.createSegment(currentGroup));
        }
    }

    private String getIdAndTimeStampAsString(IndependentMeasurement firstMeasurement) {
        int deviceId = firstMeasurement.getDeviceId();
        long timeStamp = firstMeasurement.getTimeStamp().getMillis();
        String idTimeStamp = deviceId + ":" + timeStamp;
        return idTimeStamp;
    }

    private List<IndependentMeasurement>
            popSegmentCompatibleMeasurementsWithOverlap(LinkedList<IndependentMeasurement> measurementsToSegment) {
        List<IndependentMeasurement> currentSegment = new LinkedList<IndependentMeasurement>();
        IndependentMeasurement firstMeasurement = measurementsToSegment.getFirst();
        for (int i = 0; i < segmentSize; i++) {
            IndependentMeasurement currentMeasurement = measurementsToSegment.get(i);
            if (areCompatible(firstMeasurement, currentMeasurement)) {
                currentSegment.add(currentMeasurement);
            } else {
                break;
            }
        }
        int stepSize = segmentSize - overlapSize;
        for (int i = 0; i < stepSize; i++) {
            measurementsToSegment.removeFirst();
        }
        return currentSegment;
    }

    private boolean areCompatible(IndependentMeasurement firstMeasurement, IndependentMeasurement currentMeasurement) {
        boolean deviceIdIsCompatible = currentMeasurement.getDeviceId() == firstMeasurement.getDeviceId();
        boolean labelIsCompatible = currentMeasurement.isLabeled() == firstMeasurement.isLabeled()
                && (currentMeasurement.isLabeled() == false || currentMeasurement.getLabel() == firstMeasurement.getLabel());
        boolean timeStampIsCompatible = currentMeasurement.getTimeStamp().equals(firstMeasurement.getTimeStamp());

        if (ignoreLabel) {
            return deviceIdIsCompatible && timeStampIsCompatible;
        }

        return deviceIdIsCompatible && labelIsCompatible && timeStampIsCompatible;
    }

}
