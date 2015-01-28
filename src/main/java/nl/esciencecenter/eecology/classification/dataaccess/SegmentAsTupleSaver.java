package nl.esciencecenter.eecology.classification.dataaccess;

import static org.apache.commons.lang3.StringUtils.join;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SegmentAsTupleSaver {

    @Inject
    private PathManager pathManager;

    @Inject
    @Named("output_max_one_classification_per_device_timestamp_combination")
    private boolean outputSingleClassificationPerDeviceTimeStampCombination;

    protected String defaultFirstIndexText = "0";

    public void setOutputSingleClassificationPerDeviceTimeStampCombination(boolean value) {
        outputSingleClassificationPerDeviceTimeStampCombination = value;
    }

    public void saveAsIdTimeTuple(List<Segment> segments) {
        PrintWriter printer = getPrinter();
        printer.print(getResultMessage(segments));
        printer.close();
    }

    protected String getResultMessage(List<Segment> segments) {
        StringBuilder result = new StringBuilder();
        String headers = "device_info_serial,date_time,first_index,class_id,class_name,class_red,class_green,class_blue,longitude,latitude,altitude,gpsspeed\n";
        result.append(headers);
        for (Segment segment : getIncludedSegments(segments)) {
            result.append(getDetailsAsCsv(segment));
            result.append("\n");
        }
        return result.toString();
    }

    private List<Segment> getIncludedSegments(List<Segment> segments) {
        List<Segment> includedSegments;
        if (outputSingleClassificationPerDeviceTimeStampCombination) {
            includedSegments = getOnlySegmentsWithUniqueIdTimeStamp(segments);
        } else {
            includedSegments = segments;
        }
        return includedSegments;
    }

    private LinkedList<Segment> getOnlySegmentsWithUniqueIdTimeStamp(List<Segment> segments) {
        Map<String, Segment> map = buildTupleToLabelMap(segments);
        ArrayList<String> tuples = new ArrayList<String>(map.keySet());
        Collections.sort(tuples);
        LinkedList<Segment> includedSegments = new LinkedList<Segment>();
        for (String tuple : tuples) {
            includedSegments.add(map.get(tuple));
        }
        return includedSegments;
    }

    private String getDetailsAsCsv(Segment segment) {
        List<String> list = new LinkedList<String>();
        list.add("" + segment.getDeviceId());
        list.add(segment.getTimeStamp().toString());
        list.add(segment.hasFirstIndex() ? "" + segment.getFirstIndex() : defaultFirstIndexText);

        list.add("" + segment.getPredictedLabel());
        addLabelDetailFields(list, segment.getPredictedLabelDetail());

        list.add("" + segment.getLongitude());
        list.add("" + segment.getLatitude());
        list.add("" + segment.getAltitude());
        list.add("" + segment.getGpsSpeed());
        return join(list, ",");
    }

    private void addLabelDetailFields(List<String> list, LabelDetail labelDetail) {
        list.add(labelDetail == null ? "" : labelDetail.getDescription());
        list.add(labelDetail == null ? "" : "" + labelDetail.getColorR());
        list.add(labelDetail == null ? "" : "" + labelDetail.getColorG());
        list.add(labelDetail == null ? "" : "" + labelDetail.getColorB());
    }

    private PrintWriter getPrinter() {
        PrintWriter printer = null;
        String outputPath = pathManager.getClassificationTuplesPath();
        try {
            printer = new PrintWriter(outputPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error writing to \"" + outputPath + "\".", e);
        }
        return printer;
    }

    private Map<String, Segment> buildTupleToLabelMap(List<Segment> segments) {
        Map<String, Segment> map = new HashMap<String, Segment>();
        for (Segment segment : segments) {
            String tuple = segment.getDeviceId() + "," + segment.getTimeStamp().toString();
            map.put(tuple, segment);
        }
        return map;
    }

}
