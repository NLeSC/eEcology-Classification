package nl.esciencecenter.eecology.classification.test.commands;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.commands.TrainCommand;
import nl.esciencecenter.eecology.classification.commands.exceptions.StoringClassifierException;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.ClassifierDescriptionSaver;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.machinelearning.Trainer;
import nl.esciencecenter.eecology.classification.machinelearning.TrainerFactory;
import nl.esciencecenter.eecology.classification.segmentloading.TrainSetProvider;
import weka.classifiers.Classifier;
import weka.core.Instances;

public class TrainCommandTest {
    private TrainCommand trainCommand;
    private PathManager pathManager;
    private final String testClassifierPath = "src/test/java/resources/testclassifier.json";
    private ClassifierDescriptionSaver classifierDescriptionSaver;

    @Test(expected = StoringClassifierException.class)
    public void execute_emptyClassifierOutputPath_throwException() {
        // Arrange
        expect(pathManager.getClassifierPath()).andReturn("");
        replay(pathManager);

        // Act
        trainCommand.execute();

        // Assert
    };

    @Test
    public void execute_nonEmptyClassifierOutputPath_classifierWasSaved() {
        // Arrange
        expect(pathManager.getClassifierPath()).andReturn(testClassifierPath);
        replay(pathManager);

        // Act
        trainCommand.execute();

        // Assert
        assertTrue(Files.exists(Paths.get(testClassifierPath)));
    };

    @Test
    public void execute_nonEmptyClassifierOutputPath_classifierDiscriptionSaverWasCalled() {
        // Arrange
        expect(pathManager.getClassifierPath()).andReturn(testClassifierPath);
        replay(pathManager);

        // Act
        trainCommand.execute();

        // Assert
        assertTrue(Files.exists(Paths.get(testClassifierPath)));
    };

    @Before
    public void setUp() {
        trainCommand = new TrainCommand();
        trainCommand.setPrinter(createNiceMock(Printer.class));
        pathManager = createNiceMock(PathManager.class);
        trainCommand.setPathManager(pathManager);
        trainCommand.setTrainSetProvider(createNiceMock(TrainSetProvider.class));
        trainCommand.setSegmentToinstancesCreator(createNiceMock(SegmentToInstancesCreator.class));
        TrainerFactory trainerFactory = getMockTrainerFactory();
        trainCommand.setTrainerFactory(trainerFactory);
    }

    @After
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(testClassifierPath));
    }

    private TrainerFactory getMockTrainerFactory() {
        TrainerFactory trainerFactory = createNiceMock(TrainerFactory.class);
        expect(trainerFactory.getTrainer()).andReturn(getMockTrainer());
        replay(trainerFactory);
        return trainerFactory;
    }

    private Trainer getMockTrainer() {
        Trainer trainer = new Trainer() {
            @Override
            public Classifier train(Instances instances) {
                return createNiceMock(Classifier.class);
            }
        };
        return trainer;
    }
}
