package nl.esciencecenter.eecology.classification.commands;

import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

public class SegmentsAsCsvFormatter {

    public StringBuilder getCsv(List<Segment> segments, String set) {
        StringBuilder output = new StringBuilder();
        for (Segment segment : segments) {
            output.append(segment.getDeviceId() + ",");
            output.append(segment.getTimeStamp().toString(Constants.DATE_TIME_PATTERN_ISO8601) + ",");
            output.append(segment.getLongitude() + ",");
            output.append(segment.getLatitude() + ",");
            output.append(segment.getAltitude() + ",");
            output.append(segment.getLabel() + ",");
            for (double feature : segment.getFeatures()) {
                output.append(feature + ",");
            }
            output.append(set + "\n");

        }
        return output;
    }
}