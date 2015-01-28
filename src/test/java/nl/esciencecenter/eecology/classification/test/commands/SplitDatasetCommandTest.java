package nl.esciencecenter.eecology.classification.test.commands;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;

import nl.esciencecenter.eecology.classification.commands.Printer;
import nl.esciencecenter.eecology.classification.commands.SplitDatasetCommand;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.machinelearning.DatasetSplitter;
import nl.esciencecenter.eecology.classification.segmentloading.SegmentProvider;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SplitDatasetCommandTest {
    private SplitDatasetCommand splitDatasetCommand;
    private final String testPath = "src/test/java/resources/test";

    @Test
    public void execute_objectMapperWrite3Times() {
        // Arrange
        ObjectMapper objectMapper = getObjectMapper();
        splitDatasetCommand.setObjectMapper(objectMapper);

        // Act
        splitDatasetCommand.execute();

        // Assert
        verify(objectMapper);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = createMock(ObjectMapper.class);
        try {
            objectMapper.writeValue(isA(File.class), isNull(Object.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        expectLastCall().times(3);
        replay(objectMapper);
        return objectMapper;
    };

    @Before
    public void setUp() {
        splitDatasetCommand = new SplitDatasetCommand();
        splitDatasetCommand.setPrinter(createNiceMock(Printer.class));
        DatasetSplitter datasetSplitter = createNiceMock(DatasetSplitter.class);
        replay(datasetSplitter);
        splitDatasetCommand.setDatasetSplitter(datasetSplitter);
        splitDatasetCommand.setPathManager(getMockPathManager());
        splitDatasetCommand.setObjectMapper(createNiceMock(ObjectMapper.class));
        splitDatasetCommand.setSegmentProvider(createNiceMock(SegmentProvider.class));
    }

    private PathManager getMockPathManager() {
        PathManager pathManager = createNiceMock(PathManager.class);
        expect(pathManager.getTrainSetPath()).andReturn(testPath);
        expect(pathManager.getTestSetPath()).andReturn(testPath);
        expect(pathManager.getValidationSetPath()).andReturn(testPath);
        replay(pathManager);
        return pathManager;
    }

}