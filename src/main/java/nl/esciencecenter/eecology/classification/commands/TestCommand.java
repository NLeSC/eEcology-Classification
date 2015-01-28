package nl.esciencecenter.eecology.classification.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.ClassifierLoader;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaProvider;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaToJobDirectorySaver;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.machinelearning.EvaluationResult;
import nl.esciencecenter.eecology.classification.machinelearning.Evaluator;
import nl.esciencecenter.eecology.classification.machinelearning.LabelDetail;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class TestCommand implements Command {
    @Inject
    private SchemaProvider schemaProvider;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private ClassifierLoader classifierLoader;
    @Inject
    private SegmentToInstancesCreator segmentToinstancesCreator;
    @Inject
    private Evaluator evaluator;
    @Inject
    private Printer printer;
    @Inject
    private PathManager pathManager;
    @Inject
    private ConfusionMatrixStatisticsCalculator confusionMatrixStatisticsCalculator;
    @Inject
    private SchemaToJobDirectorySaver schemaToJobDirectorySaver;
    @Inject
    private ConfusionMatrixFormatter confusionMatrixFormatter;

    public void setClassifierLoader(ClassifierLoader classifierLoader) {
        this.classifierLoader = classifierLoader;
    }

    public void setConfusionMatrixStatisticsCalculator(ConfusionMatrixStatisticsCalculator confusionMatrixStatisticsCalculator) {
        this.confusionMatrixStatisticsCalculator = confusionMatrixStatisticsCalculator;
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
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

    public void setSchemaProvider(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public void setSchemaToJobDirectorySaver(SchemaToJobDirectorySaver schemaToJobDirectorySaver) {
        this.schemaToJobDirectorySaver = schemaToJobDirectorySaver;
    }

    public void setSegmentToinstancesCreator(SegmentToInstancesCreator segmentToinstancesCreator) {
        this.segmentToinstancesCreator = segmentToinstancesCreator;
    }

    @Override
    public void execute() {
        printer.print("Executing testing process: ");
        printer.print("loading segments...");
        List<Segment> testSet = loadFromJson(pathManager.getTestSetPath());
        printer.print("evaluating...");
        test(testSet);
        schemaToJobDirectorySaver.saveSchemaToJobDirectory();
        printer.print("done.\n");
    }

    private List<Segment> loadFromJson(String path) {
        List<Segment> segments;
        try {
            segments = objectMapper.readValue(new File(path), new TypeReference<List<Segment>>() {
            });
        } catch (JsonParseException | JsonMappingException e) {
            String message = "The file containing the test set (" + path + ") doesn't appear to be in the correct format.";
            throw new RuntimeException(message, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from the file containing the test set (" + path + ").", e);

        }
        return segments;
    }

    private void test(List<Segment> testSegments) {
        Classifier testClassifier = classifierLoader.loadClassifier();
        Instances testInstances = segmentToinstancesCreator.createInstancesAndUpdateSegments(testSegments);
        EvaluationResult result = evaluator.evaluate(testClassifier, testInstances);
        List<Segment> misclassifications = result.getMisclassifications();
        writeToJson(misclassifications, pathManager.getMisclassificationsPath());
        writeTestResultsToJson(result, testClassifier);
    }

    private void writeTestResultsToJson(EvaluationResult result, Classifier testClassifier) {
        TestResultViewModel model = new TestResultViewModel();
        Evaluation stat = result.getEvaluationStatistics();
        model.setErrorRate(stat.errorRate());
        model.setInstancesCorrect((int) Math.round(stat.correct()));
        model.setPercentageCorrect(stat.pctCorrect());
        model.setInstancesIncorrect((int) Math.round(stat.incorrect()));
        model.setPercentageIncorrect(stat.pctIncorrect());
        model.setKappa(stat.kappa());
        model.setClassifierDescription(testClassifier.toString().split("\n")[0]);

        double[][] confusionMatrix = stat.confusionMatrix();
        model.setConfusionMatrix(confusionMatrix);
        model.setPrecisionVector(confusionMatrixStatisticsCalculator.getPrecisionVector(confusionMatrix));
        model.setRecallVector(confusionMatrixStatisticsCalculator.getRecallVector(confusionMatrix));
        model.setLabelSchema(schemaProvider.getSchema().values());
        writeToJson(model, pathManager.getTestResultPath());
        exportConfusionMatrix(confusionMatrix);
    }

    public List<String> getLabels() {
        Collection<LabelDetail> labelDetails = schemaProvider.getSchema().values();
        List<String> labelDescriptions = new LinkedList<String>();
        for (LabelDetail labelDetail : labelDetails) {
            labelDescriptions.add(labelDetail.getDescription());
        }
        return labelDescriptions;
    }

    private void writeToJson(Object object, String fileName) {
        try {
            objectMapper.writeValue(new File(fileName), object);
        } catch (JsonGenerationException | JsonMappingException e) {
            throw new RuntimeException("Error serializing the misclassifications.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error storing the misclassifications.", e);
        }
    }

    private void exportConfusionMatrix(double[][] confusionMatrix) {
        String exportPath = pathManager.getTestResultConfusionMatrixExportPath();
        try {
            FileWriter writer = new FileWriter(exportPath);
            writer.write(confusionMatrixFormatter.format(confusionMatrix, getLabels()));
            writer.close();
        } catch (IOException e) {
            printer.warn("Error exporting confusion matrix to '" + exportPath + "'. Continuing...");
        }
    }
}
