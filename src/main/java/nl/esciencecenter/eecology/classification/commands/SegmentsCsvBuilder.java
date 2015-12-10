package nl.esciencecenter.eecology.classification.commands;

import java.util.List;

import com.google.inject.Inject;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

public class SegmentsCsvBuilder {
    @Inject
    private SegmentToInstancesCreator segmentToinstancesCreator;

    public void setSegmentToinstancesCreator(SegmentToInstancesCreator segmentToinstancesCreator) {
        this.segmentToinstancesCreator = segmentToinstancesCreator;
    }

    public String buildCsv(List<Segment> trainSet, List<Segment> testSet, List<Segment> validationSet,
            List<Segment> unclassified) {
        segmentToinstancesCreator.createInstancesAndUpdateSegments(trainSet);
        segmentToinstancesCreator.createInstancesAndUpdateSegments(testSet);
        segmentToinstancesCreator.createInstancesAndUpdateSegments(validationSet);
        segmentToinstancesCreator.createInstancesAndUpdateSegments(unclassified);

        StringBuilder output = new StringBuilder();
        output.append(getHeaders(trainSet));
        output.append(getCsv(trainSet, "train"));
        output.append(getCsv(testSet, "test"));
        output.append(getCsv(validationSet, "validation"));
        output.append(getCsv(unclassified, "unclassified"));
        String csv = output.toString();
        return csv;
    }

    private String getHeaders(List<Segment> segments) {
        if (segments.size() == 0) {
            return "";
        }
        Segment segment = segments.get(0);

        StringBuilder output = new StringBuilder();
        output.append("device_info_serial,");
        output.append("date_time,");
        output.append("lon,");
        output.append("lat,");
        output.append("alt,");
        output.append("class_id,");
        for (String feature : segment.getFeatureNames()) {
            output.append(feature + ",");
        }
        output.append("set\n");
        return output.toString();

    }

    private String getCsv(List<Segment> segments, String set) {
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
        return output.toString();
    }
}
