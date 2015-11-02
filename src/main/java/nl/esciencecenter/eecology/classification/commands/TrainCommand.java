package nl.esciencecenter.eecology.classification.commands;

import java.util.List;

import com.google.inject.Inject;

import nl.esciencecenter.eecology.classification.commands.exceptions.StoringClassifierException;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.ClassifierDescriptionSaver;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.machinelearning.TrainerFactory;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.TrainSetProvider;
import weka.classifiers.Classifier;
import weka.core.Instances;

public class TrainCommand implements Command {
    @Inject
    private SegmentToInstancesCreator segmentToinstancesCreator;
    @Inject
    private TrainerFactory trainerFactory;
    @Inject
    private Printer printer;
    @Inject
    private TrainSetProvider trainSetProvider;
    @Inject
    private PathManager pathManager;
    @Inject
    private ClassifierDescriptionSaver classifierDescriptionSaver;

    public void setTrainerFactory(TrainerFactory trainerFactory) {
        this.trainerFactory = trainerFactory;
    }

    public void setSegmentToinstancesCreator(SegmentToInstancesCreator segmentToinstancesCreator) {
        this.segmentToinstancesCreator = segmentToinstancesCreator;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public void setTrainSetProvider(TrainSetProvider trainSetProvider) {
        this.trainSetProvider = trainSetProvider;
    }

    public void setClassifierDescriptionSaver(ClassifierDescriptionSaver classifierDescriptionSaver) {
        this.classifierDescriptionSaver = classifierDescriptionSaver;
    }

    @Override
    public void execute() {
        printer.print("Executing model training process: ");
        train();
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
        classifierDescriptionSaver.saveClassifierDescription(classifier, trainSegments);
    }

}
