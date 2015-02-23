package nl.esciencecenter.eecology.classification.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaToJobDirectorySaver;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class OutputFeaturesInCsvCommand implements Command {
    @Inject
    private Printer printer;
    @Inject
    private PathManager pathManager;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private SegmentToInstancesCreator segmentToinstancesCreator;
    @Inject
    private SchemaToJobDirectorySaver schemaToJobDirectorySaver = new SchemaToJobDirectorySaver();

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setSchemaToJobFolderSaver(SchemaToJobDirectorySaver schemaToJobFolderSaver) {
        schemaToJobDirectorySaver = schemaToJobFolderSaver;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setSegmentToinstancesCreator(SegmentToInstancesCreator segmentToinstancesCreator) {
        this.segmentToinstancesCreator = segmentToinstancesCreator;
    }

    @Override
    public void execute() {
        printer.print("Executing output features as csv process: ");
        StringBuilder output = new StringBuilder();
        output.append(getHeaders());
        printer.print("loading segments...");
        output.append(getCsv(pathManager.getTrainSetPath(), "train"));
        output.append(getCsv(pathManager.getTestSetPath(), "test"));
        output.append(getCsv(pathManager.getValidationSetPath(), "validation"));
        printer.print("saving output...");
        saveOutputToFile(output.toString());
        schemaToJobDirectorySaver.saveSchemaToJobDirectory();
        printer.print("done.\n");
    }

    private String getHeaders() {
        List<Segment> segments = loadFromJson(pathManager.getTrainSetPath());
        segmentToinstancesCreator.createInstancesAndUpdateSegments(segments);
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

    private String getCsv(String path, String set) {
        List<Segment> segments = loadFromJson(path);
        segmentToinstancesCreator.createInstancesAndUpdateSegments(segments);
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

    private void saveOutputToFile(String output) {
        String testResultPath = pathManager.getFeaturesCsvPath();
        File file = new File(testResultPath);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
            printWriter.print(output);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not write result log to file (" + testResultPath + ").", e);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    private List<Segment> loadFromJson(String path) {
        List<Segment> segments;
        try {
            segments = objectMapper.readValue(new File(path), new TypeReference<List<Segment>>() {
            });
        } catch (JsonParseException | JsonMappingException e) {
            String message = "File (" + path + ") doesn't appear to be in the correct format.";
            throw new RuntimeException(message, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from file (" + path + ").", e);

        }
        return segments;
    }
}
