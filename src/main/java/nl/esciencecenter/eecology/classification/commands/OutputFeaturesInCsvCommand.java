package nl.esciencecenter.eecology.classification.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.LoadingMeasurementsException;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaToJobDirectorySaver;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentProvider;

public class OutputFeaturesInCsvCommand implements Command {
    @Inject
    private Printer printer;
    @Inject
    private PathManager pathManager;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private SchemaToJobDirectorySaver schemaToJobDirectorySaver = new SchemaToJobDirectorySaver();
    @Inject
    private SegmentsCsvBuilder segmentsCsvBuilder = new SegmentsCsvBuilder();
    @Inject
    private SegmentProvider segmentProvider;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setSegmentProvider(SegmentProvider segmentProvider) {
        this.segmentProvider = segmentProvider;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setSegmentsCsvBuilder(SegmentsCsvBuilder segmentsCsvBuilder) {
        this.segmentsCsvBuilder = segmentsCsvBuilder;
    }

    public void setSchemaToJobFolderSaver(SchemaToJobDirectorySaver schemaToJobFolderSaver) {
        schemaToJobDirectorySaver = schemaToJobFolderSaver;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void execute() {
        printer.print("Executing output features as csv process: ");
        printer.print("loading segments...");
        List<Segment> trainSet = loadFromJsonAndUpdateSegments(pathManager.getTrainSetPath());
        List<Segment> testSet = loadFromJsonAndUpdateSegments(pathManager.getTestSetPath());
        List<Segment> validationSet = loadFromJsonAndUpdateSegments(pathManager.getValidationSetPath());
        List<Segment> unclassified = getUnclassifiedSegments();
        printer.print("saving output...");
        String csv = segmentsCsvBuilder.buildCsv(trainSet, testSet, validationSet, unclassified);
        saveOutputToFile(csv);
        schemaToJobDirectorySaver.saveSchemaToJobDirectory();
        printer.print("done.\n");
    }

    private List<Segment> getUnclassifiedSegments() {
        try {
            return segmentProvider.getUnannotatedSegments();
        } catch (LoadingMeasurementsException e) {
            // no problem
            return new LinkedList<Segment>();
        }
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

    private List<Segment> loadFromJsonAndUpdateSegments(String path) {
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
