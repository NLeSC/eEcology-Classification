package nl.esciencecenter.eecology.classification.commands;

import java.io.File;
import java.util.List;

import nl.esciencecenter.eecology.classification.commands.exceptions.StoringClassifierException;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaToJobDirectorySaver;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.machinelearning.TrainerFactory;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.TrainSetProvider;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class TrainCommand implements Command {
    @Inject
    private SegmentToInstancesCreator segmentToinstancesCreator;
    @Inject
    private TrainerFactory trainerFactory;
    @Inject
    private TreeFactory treeFactory;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private Printer printer;
    @Inject
    private TrainSetProvider trainSetProvider;
    @Inject
    private PathManager pathManager;
    @Inject
    private SchemaToJobDirectorySaver schemaToJobDirectorySaver;

    public void setTrainerFactory(TrainerFactory trainerFactory) {
        this.trainerFactory = trainerFactory;
    }

    public void setSegmentToinstancesCreator(SegmentToInstancesCreator segmentToinstancesCreator) {
        this.segmentToinstancesCreator = segmentToinstancesCreator;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setSchemaToJobDirectorySaver(SchemaToJobDirectorySaver schemaToJobDirectorySaver) {
        this.schemaToJobDirectorySaver = schemaToJobDirectorySaver;
    }

    public void setTrainSetProvider(TrainSetProvider trainSetProvider) {
        this.trainSetProvider = trainSetProvider;
    }

    @Override
    public void execute() {
        printer.print("Executing model training process: ");
        train();
        schemaToJobDirectorySaver.saveSchemaToJobDirectory();
        printer.print("done.\n");
    }

    private void train() {
        printer.print("loading segments...");
        List<Segment> trainSegments = trainSetProvider.getTrainSet();
        Instances trainInstances = segmentToinstancesCreator.createInstancesAndUpdateSegments(trainSegments);
        printer.print("training classifier...");
        Classifier classifier = trainerFactory.getTrainer().train(trainInstances);
        try {
            weka.core.SerializationHelper.write(pathManager.getClassifierPath(), classifier);
        } catch (Exception e) {
            throw new StoringClassifierException("Could not store the trained classifier.", e);
        }
        createTreeGraphIfPossible(classifier, trainSegments);
    }

    private void createTreeGraphIfPossible(Classifier classifier, List<Segment> segments) {
        if (classifier instanceof J48) {
            J48 j48 = (J48) classifier;
            String dotGraph = "";
            try {
                dotGraph = j48.graph();
                TreeNode root = treeFactory.createFromDotGraph(dotGraph);
                root.feed(segments);
                objectMapper.writeValue(new File(pathManager.getTreeGraphPath()), root);
            } catch (Exception e) {
                // No problem. Continue without graph.
            }
        }
    }
}
