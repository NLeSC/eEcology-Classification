package nl.esciencecenter.eecology.classification.test.segmentloading;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.dataaccess.SegmentLoader;
import nl.esciencecenter.eecology.classification.segmentloading.FixedNumberDatasetFilter;
import nl.esciencecenter.eecology.classification.segmentloading.Segment;
import nl.esciencecenter.eecology.classification.segmentloading.TrainSetProvider;

import org.junit.Before;
import org.junit.Test;

public class TrainSetProviderTest {
    private TrainSetProvider trainSetProvider;
    private SegmentLoader segmentLoader;
    private FixedNumberDatasetFilter fixedNumberDatasetFilter;

    @Test
    public void getTestSet_objectMapperReadWasCalled() {
        // Arrange

        // Act
        trainSetProvider.getTrainSet();

        // Assert
        verify(segmentLoader);
    }

    @Test
    public void getTestSet_fixedNumberPerClassIsFalse_fixedNumberDatasetFilterWasNotCalled() {
        // Arrange
        trainSetProvider.setClassInstancesNumberFixed(false);
        expect(fixedNumberDatasetFilter.filter(isA(List.class))).andThrow(new AssertionError("Method should not be called"));
        replay(fixedNumberDatasetFilter);

        // Act
        trainSetProvider.getTrainSet();

        // Assert
    }

    @Test
    public void getTestSet_fixedNumberPerClassIsTrue_fixedNumberDatasetFilterWasCalled() {
        // Arrange
        trainSetProvider.setClassInstancesNumberFixed(true);
        expect(fixedNumberDatasetFilter.filter(isA(List.class))).andReturn(null);
        replay(fixedNumberDatasetFilter);

        // Act
        trainSetProvider.getTrainSet();

        // Assert
        verify(fixedNumberDatasetFilter);
    }

    @Before
    public void setUp() {
        trainSetProvider = new TrainSetProvider();
        fixedNumberDatasetFilter = createNiceMock(FixedNumberDatasetFilter.class);
        trainSetProvider.setFixedNumberDataSetFilter(fixedNumberDatasetFilter);
        trainSetProvider.setPathManager(getPathManager());
        segmentLoader = getSegmentLoader();
        trainSetProvider.setSegmentLoader(segmentLoader);
    }

    private SegmentLoader getSegmentLoader() {
        SegmentLoader objectMapper = createNiceMock(SegmentLoader.class);
        expect(objectMapper.loadFromJson(isA(String.class))).andReturn(new LinkedList<Segment>());
        replay(objectMapper);
        return objectMapper;
    }

    private PathManager getPathManager() {
        PathManager pathManager = createNiceMock(PathManager.class);
        expect(pathManager.getTrainSetPath()).andReturn("");
        replay(pathManager);
        return pathManager;
    }
}
