package nl.esciencecenter.eecology.classification.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.machinelearning.DatasetSplitter;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentProvider;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class SplitDatasetCommand implements Command {
    @Inject
    private DatasetSplitter datasetSplitter;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private Printer printer;
    @Inject
    private PathManager pathManager;
    @Inject
    private SegmentProvider segmentProvider;

    public void setSegmentProvider(SegmentProvider segmentProvider) {
        this.segmentProvider = segmentProvider;
    }

    public void setDatasetSplitter(DatasetSplitter datasetSplitter) {
        this.datasetSplitter = datasetSplitter;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void execute() {
        printer.print("Executing dataset splitting process: ");
        printer.print("loading and segmenting annotated data...");
        List<Segment> segments = segmentProvider.getAnnotatedSegments();
        datasetSplitter.setInputDataset(segments);
        writeToJson(datasetSplitter.getTrainDataset(), pathManager.getTrainSetPath());
        writeToJson(datasetSplitter.getTestDataset(), pathManager.getTestSetPath());
        writeToJson(datasetSplitter.getValidationDataset(), pathManager.getValidationSetPath());
        printer.print("done.\n");
    }

    private void writeToJson(List<Segment> segments, String fileName) {
        try {
            objectMapper.writeValue(new File(fileName), segments);
        } catch (JsonGenerationException | JsonMappingException e) {
            throw new RuntimeException("Error serializing dataset to write to file (" + fileName + ").", e);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file (" + fileName + ").", e);
        }
    }

}
