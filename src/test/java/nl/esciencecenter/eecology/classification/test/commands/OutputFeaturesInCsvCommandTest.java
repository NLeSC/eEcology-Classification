package nl.esciencecenter.eecology.classification.test.commands;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.esciencecenter.eecology.classification.commands.OutputFeaturesInCsvCommand;
import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.commands.SegmentsCsvBuilder;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.LoadingMeasurementsException;
import nl.esciencecenter.eecology.classification.dataaccess.SchemaToJobDirectorySaver;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentProvider;

public class OutputFeaturesInCsvCommandTest {
    private OutputFeaturesInCsvCommand outputFeaturesCommand;
    private ObjectMapper objectMapper;
    private final Path testFileLocation = Paths.get("src/test/java/resources/testoutput");

    @Test
    public void execute_zeroSegments_noExceptionThrown() {
        // Arrange
        makeObjectMapperReturn(new LinkedList<Segment>());

        // Act
        outputFeaturesCommand.execute();

        // Assert
    }

    @Test
    public void execute_segments_noExceptionThrown() {
        // Arrange
        makeObjectMapperReturn(getTestSegments());

        // Act
        outputFeaturesCommand.execute();

        // Assert
    }

    @Test
    public void execute_segments_csvBuilderWasCalled() {
        // Arrange
        makeObjectMapperReturn(getTestSegments());
        SegmentsCsvBuilder segmentsCsvBuilder = createNiceMock(SegmentsCsvBuilder.class);
        expect(segmentsCsvBuilder.buildCsv(isA(List.class), isA(List.class), isA(List.class), isA(List.class))).andReturn("");
        replay(segmentsCsvBuilder);
        outputFeaturesCommand.setSegmentsCsvBuilder(segmentsCsvBuilder);

        // Act
        outputFeaturesCommand.execute();

        // Assert
        verify(segmentsCsvBuilder);
    }

    @Test
    public void execute_errorLoadingSegments_noException() {
        // Arrange
        makeObjectMapperReturn(getTestSegments());
        outputFeaturesCommand.setSegmentProvider(new SegmentProvider() {
            @Override
            public List<Segment> getUnannotatedSegments() {
                throw new LoadingMeasurementsException("test", null);
            }
        });

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
        outputFeaturesCommand.setSegmentsCsvBuilder(new SegmentsCsvBuilder() {
            @Override
            public String buildCsv(List<Segment> trainSet, List<Segment> testSet, List<Segment> validationSet,
                    List<Segment> unclassified) {
                return "";
            }
        });
        outputFeaturesCommand.setSchemaToJobFolderSaver(new SchemaToJobDirectorySaver() {
            @Override
            public void saveSchemaToJobDirectory() {
                return;
            }
        });
        outputFeaturesCommand.setSegmentProvider(new SegmentProvider() {
            @Override
            public List<Segment> getUnannotatedSegments() {
                return new LinkedList<Segment>();
            }

            @Override
            public List<Segment> getAnnotatedSegments() {
                return new LinkedList<Segment>();
            }
        });
    }

    @After
    public void cleanUp() throws IOException {
        Files.deleteIfExists(testFileLocation);
    }

    private PathManager getPathManager() {
        PathManager pathManager = createNiceMock(PathManager.class);
        expect(pathManager.getTrainSetPath()).andReturn("");
        expect(pathManager.getTrainSetPath()).andReturn("");
        expect(pathManager.getTestSetPath()).andReturn("");
        expect(pathManager.getFeaturesCsvPath()).andReturn(testFileLocation.toString());
        expect(pathManager.getSchemaJsonPath()).andReturn(testFileLocation.toString());
        expect(pathManager.getValidationSetPath()).andReturn("");
        replay(pathManager);
        return pathManager;
    }

    private void makeObjectMapperReturn(LinkedList<Segment> segments) {
        try {
            expect(objectMapper.readValue(isA(File.class), isA(TypeReference.class))).andReturn(segments).anyTimes();
        } catch (Exception e) {
        }
        replay(objectMapper);
    }

    private LinkedList<Segment> getTestSegments() {
        LinkedList<Segment> segments = new LinkedList<Segment>();
        Segment segment = new Segment(new LinkedList<IndependentMeasurement>());
        segment.setTimeStamp(new DateTime(2014, 6, 3, 15, 30));
        segment.setFeatures(new double[] { 5 }, new String[] { "dummy" });
        segments.add(segment);
        return segments;
    }

}