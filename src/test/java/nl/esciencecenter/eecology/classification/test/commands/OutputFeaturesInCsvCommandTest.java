package nl.esciencecenter.eecology.classification.test.commands;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.commands.OutputFeaturesInCsvCommand;
import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaToJobDirectorySaver;
import nl.esciencecenter.eecology.classification.featureextraction.SegmentToInstancesCreator;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OutputFeaturesInCsvCommandTest {
    private OutputFeaturesInCsvCommand outputFeaturesCommand;
    private ObjectMapper objectMapper;
    private final String testFileLocation = "src/test/java/resources/testoutput";

    @Test
    public void execute_zeroSegments_noExceptionThrown() {
        // Arrange
        try {
            expect(objectMapper.readValue(isA(File.class), isA(TypeReference.class))).andReturn(new LinkedList<Segment>())
                    .anyTimes();
        } catch (Exception e) {
        }
        replay(objectMapper);

        // Act
        outputFeaturesCommand.execute();

        // Assert
    }

    @Test
    public void execute_segments_noExceptionThrown() {
        // Arrange
        LinkedList<Segment> segments = new LinkedList<Segment>();
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        segment.setTimeStamp(new DateTime(2014, 6, 3, 15, 30));
        segment.setFeatures(new double[] { 5 }, new String[] { "dummy" });
        segments.add(segment);

        try {
            expect(objectMapper.readValue(isA(File.class), isA(TypeReference.class))).andReturn(segments).anyTimes();
        } catch (Exception e) {
        }
        replay(objectMapper);

        // Act
        outputFeaturesCommand.execute();

        // Assert
    }

    @Before
    public void setUp() {
        outputFeaturesCommand = new OutputFeaturesInCsvCommand();
        outputFeaturesCommand.setPrinter(createNiceMock(Printer.class));
        PathManager pathManager = getPathManager();
        outputFeaturesCommand.setPathManager(pathManager);
        objectMapper = createNiceMock(ObjectMapper.class);
        outputFeaturesCommand.setObjectMapper(objectMapper);
        outputFeaturesCommand.setSegmentToinstancesCreator(getSegmentToInstanceCreator());
        outputFeaturesCommand.setSchemaToJobFolderSaver(new SchemaToJobDirectorySaver() {
            @Override
            public void saveSchemaToJobDirectory() {
                return;
            }
        });

    }

    private PathManager getPathManager() {
        PathManager pathManager = createNiceMock(PathManager.class);
        expect(pathManager.getTrainSetPath()).andReturn("");
        expect(pathManager.getTrainSetPath()).andReturn("");
        expect(pathManager.getTestSetPath()).andReturn("");
        expect(pathManager.getFeaturesCsvPath()).andReturn(testFileLocation);
        expect(pathManager.getSchemaJsonPath()).andReturn(testFileLocation);
        expect(pathManager.getValidationSetPath()).andReturn("");
        replay(pathManager);
        return pathManager;
    }

    private SegmentToInstancesCreator getSegmentToInstanceCreator() {
        SegmentToInstancesCreator segmentToInstanceCreator = createNiceMock(SegmentToInstancesCreator.class);
        expect(segmentToInstanceCreator.createInstancesAndUpdateSegments(isA(List.class))).andReturn(null);
        replay(segmentToInstanceCreator);
        return segmentToInstanceCreator;
    }

}